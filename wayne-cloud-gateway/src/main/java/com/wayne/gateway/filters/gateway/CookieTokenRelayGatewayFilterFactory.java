package com.wayne.gateway.filters.gateway;

import static org.springframework.security.oauth2.core.web.reactive.function.OAuth2BodyExtractors.oauth2AccessTokenResponse;


import java.net.URI;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.wayne.gateway.client.CookieOAuth2Helper;
import com.wayne.gateway.client.CookieTokenProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.ExchangeFunctions;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Token Relay Gateway Filter with automatic access token refresh. This can be removed when issue
 * {@see https://github.com/spring-cloud/spring-cloud-security/issues/175} is closed. Implementation
 * based on {@link ServerOAuth2AuthorizedClientExchangeFilterFunction}
 */
@Slf4j
public class CookieTokenRelayGatewayFilterFactory extends AbstractGatewayFilterFactory<CookieTokenRelayGatewayFilterFactory.Config> {

    private final ServerOAuth2AuthorizedClientRepository authorizedClientRepository;
    private final OAuth2AuthorizedClientResolver authorizedClientResolver;
    private final ClientHttpConnector connector;
    private final Clock clock = Clock.systemUTC();
    private final Duration accessTokenExpiresSkew = Duration.ofSeconds(3);
    private final NimbusJwtDecoder jwtDecoder;
    private final CookieTokenProcessor cookieTokenWrite;

    public CookieTokenRelayGatewayFilterFactory(
            ServerOAuth2AuthorizedClientRepository authorizedClientRepository,
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ClientHttpConnector connector,
            String jwkUri,
            CookieTokenProcessor cookieTokenWrite) {
        super(Config.class);
        this.authorizedClientRepository = authorizedClientRepository;
        this.authorizedClientResolver =
                new OAuth2AuthorizedClientResolver(
                        clientRegistrationRepository, authorizedClientRepository);
        this.connector = connector;
        this.jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkUri).build();
        this.jwtDecoder.setJwtValidator(new HttpCookieTokenNoExpiryValidator());
        this.cookieTokenWrite = cookieTokenWrite;
    }

    public GatewayFilter apply() {
        Config config = new Config();
        config.setPath("/");
        return apply(config);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("path");
    }

    public static class Config {
        private String path;

        public Config() {
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) ->
                exchange
                        //????????????
                        .getPrincipal()
                        //?????????????????????OAuth2AuthenticationToken???
                        .filter(principal -> principal instanceof OAuth2AuthenticationToken)
                        //????????????
                        .cast(OAuth2AuthenticationToken.class)
                        //????????????header?????????????????????
                        .map(authentication -> withUserName(exchange, authentication))
                        //OAuth2AuthenticationToken ?????? OAuth2AuthorizedClient |??????refresh token ?????? access token?????? ???????????????access token
                        .flatMap(authentication -> getAccessToken(exchange, authentication))
                        //??????OAuth2AccessToken
                        .map(OAuth2AuthorizedClient::getAccessToken)
                        //???jwt token??? cookies
                        .map(token -> withAuthToken(exchange, token))
                        // TODO: adjustable behavior if empty
                        .defaultIfEmpty(exchange)
                        .flatMap(chain::filter);
    }

    /**
     * ???????????????????????????
     *
     * @param exchange
     * @param authentication
     * @return
     */
    private OAuth2AuthenticationToken withUserName(
            ServerWebExchange exchange, OAuth2AuthenticationToken authentication) {
        String userName = authentication.getPrincipal().getAttribute("user_name");
        String userId = authentication.getPrincipal().getAttribute("user_id");
        exchange
                .mutate()
                .request(
                        r ->
                                r.headers(
                                        headers -> {
                                            headers.put("user_name", Collections.singletonList(userName));
                                            headers.put("user_id", Collections.singletonList(userId));
                                        }))
                .build();
        log.info(" ===> request path is <{}>", exchange.getRequest().getPath());
        log.info(" ===> set user_id <{}> and user_name <{}> to headers", userId, userName);
        return authentication;
    }

    /**
     * ????????????token???Response-Cookie
     *
     * @param exchange
     * @param accessToken
     * @return
     */
    private ServerWebExchange withAuthToken(
            ServerWebExchange exchange, OAuth2AccessToken accessToken) {
        cookieTokenWrite.writeAccessToken(accessToken.getTokenValue(), exchange);
        return exchange
                .mutate()
                .request(r -> r.headers(headers -> headers.setBearerAuth(accessToken.getTokenValue())))
                .build();
    }

    /**
     * ??????refresh ??? access token
     *
     * @param exchange
     * @param clientRegistrationId
     * @param authorizedClient
     * @return
     */
    private Mono<OAuth2AuthorizedClient> refreshIfNecessary(
            ServerWebExchange exchange,
            String clientRegistrationId,
            OAuth2AuthorizedClient authorizedClient) {
        //??????????????????
        if (shouldRefresh(authorizedClient)) {
            //????????????OAuth2AuthorizedClient???
            return createRequest(exchange, clientRegistrationId)
                    .flatMap(r -> refreshAuthorizedClient(authorizedClient, r));
        }
        return Mono.just(authorizedClient);
    }

    /**
     * ??????cookies????????? jwt token?????? ????????? oauth2 ?????????token ????????????OAuth2AuthorizedClient
     *
     * @param exchange
     * @param oAuth2AuthenticationToken
     * @return
     */
    private Mono<OAuth2AuthorizedClient> getAccessToken(
            ServerWebExchange exchange, OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        String clientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        //??????cookie??????access token
        String aToken =
                exchange.getRequest().getCookies().getFirst(CookieOAuth2Helper.ACCESS_TOKEN).getValue();
        //?????????jwt?????? ??????token?????????
        Jwt accessJwt = jwtDecoder.decode(aToken);
        //???????????? OAuth2?????????access token
        OAuth2AccessToken accessToken =
                new OAuth2AccessToken(
                        OAuth2AccessToken.TokenType.BEARER,
                        aToken,
                        accessJwt.getIssuedAt(),
                        accessJwt.getExpiresAt());
        //??????cookie??????refresh token
        String fToken =
                exchange.getRequest().getCookies().getFirst(CookieOAuth2Helper.REFRESH_TOKEN).getValue();
        Jwt refreshJwt = jwtDecoder.decode(fToken);
        //???????????? OAuth2?????????refresh token
        OAuth2RefreshToken refreshToken =
                new OAuth2RefreshToken(refreshJwt.getTokenValue(), refreshJwt.getIssuedAt());
        //???????????????
        Mono<ClientRegistration> clientRegistrationMono =
                authorizedClientResolver.findByRegistrationId(clientRegistrationId);
        //??????principalName
        Mono<String> requestMono =
                createRequest(exchange, clientRegistrationId)
                        .flatMap(request -> Mono.just(request.getAuthentication().getName()));
        return Mono.zip(clientRegistrationMono, requestMono)
                .map(t2 -> new OAuth2AuthorizedClient(t2.getT1(), t2.getT2(), accessToken, refreshToken))
                .flatMap(
                        oAuth2AuthorizedClient ->
                                refreshIfNecessary(exchange, clientRegistrationId, oAuth2AuthorizedClient));
    }

    private Mono<OAuth2AuthorizedClientResolver.Request> createRequest(
            ServerWebExchange exchange, String clientRegistrationId) {
        return authorizedClientResolver.createDefaultedRequest(
                clientRegistrationId, null, exchange);
    }

    /**
     * ??????????????????token
     *
     * @param authorizedClient
     * @return
     */
    private boolean shouldRefresh(OAuth2AuthorizedClient authorizedClient) {
        if (this.authorizedClientRepository == null) {
            return false;
        }
        OAuth2RefreshToken refreshToken = authorizedClient.getRefreshToken();
        if (refreshToken == null) {
            return false;
        }
        Instant now = this.clock.instant();
        Instant expiresAt = authorizedClient.getAccessToken().getExpiresAt();
        if (expiresAt != null && now.isAfter(expiresAt.minus(this.accessTokenExpiresSkew))) {
            return true;
        }
        return false;
    }

    private Mono<OAuth2AuthorizedClient> refreshAuthorizedClient(
            OAuth2AuthorizedClient authorizedClient, OAuth2AuthorizedClientResolver.Request r) {
        ServerWebExchange exchange = r.getExchange();
        Authentication authentication = r.getAuthentication();
        //?????????????????????
        ClientRegistration clientRegistration = authorizedClient.getClientRegistration();
        //token????????????
        String tokenUri = clientRegistration.getProviderDetails().getTokenUri();
        ClientRequest refreshRequest =
                ClientRequest.create(HttpMethod.POST, URI.create(tokenUri))
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .headers(
                                headers ->
                                        headers.setBasicAuth(
                                                clientRegistration.getClientId(), clientRegistration.getClientSecret()))
                        .body(refreshTokenBody(authorizedClient.getRefreshToken().getTokenValue()))
                        .build();
        ExchangeFunction next = ExchangeFunctions.create(connector);
        return next.exchange(refreshRequest)
                .flatMap(refreshResponse -> refreshResponse.body(oauth2AccessTokenResponse()))
                .map(
                        accessTokenResponse -> {
                            OAuth2RefreshToken refreshToken =
                                    Optional.ofNullable(accessTokenResponse.getRefreshToken())
                                            .orElse(authorizedClient.getRefreshToken());
                            return new OAuth2AuthorizedClient(
                                    authorizedClient.getClientRegistration(),
                                    authorizedClient.getPrincipalName(),
                                    accessTokenResponse.getAccessToken(),
                                    refreshToken);
                        })
                .flatMap(
                        result ->
                                this.authorizedClientRepository
                                        .saveAuthorizedClient(result, authentication, exchange)
                                        .thenReturn(result));
    }

    private static BodyInserters.FormInserter<String> refreshTokenBody(String refreshToken) {
        return BodyInserters.fromFormData("grant_type", AuthorizationGrantType.REFRESH_TOKEN.getValue())
                .with("refresh_token", refreshToken);
    }

    /**
     * ?????????token????????????
     */
    private class HttpCookieTokenNoExpiryValidator implements OAuth2TokenValidator<Jwt> {

        private final Duration clockSkew = Duration.of(60, ChronoUnit.SECONDS);
        private final Clock clock = Clock.systemUTC();

        @Override
        public OAuth2TokenValidatorResult validate(Jwt jwt) {
            Assert.notNull(jwt, "jwt cannot be null");
            Instant notBefore = jwt.getNotBefore();
            if (notBefore != null) {
                if (Instant.now(this.clock).plus(clockSkew).isBefore(notBefore)) {
                    OAuth2Error error =
                            new OAuth2Error(
                                    OAuth2ErrorCodes.INVALID_REQUEST,
                                    String.format("Jwt used before %s", jwt.getNotBefore()),
                                    "https://tools.ietf.org/html/rfc6750#section-3.1");
                    return OAuth2TokenValidatorResult.failure(error);
                }
            }
            return OAuth2TokenValidatorResult.success();
        }
    }
}
