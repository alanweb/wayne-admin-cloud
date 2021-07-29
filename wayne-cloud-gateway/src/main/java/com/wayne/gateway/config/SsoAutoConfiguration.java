package com.wayne.gateway.config;

import com.wayne.gateway.client.*;
import com.wayne.gateway.filters.JwtExpireWebFilter;
import com.wayne.gateway.filters.gateway.CookieTokenRelayGatewayFilterFactory;
import com.wayne.gateway.filters.gateway.WayneRemoveResponseHeaderGatewayFilterFactory;
import com.wayne.gateway.filters.gateway.WayneSetResponseHeaderGatewayFilterFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.util.StringUtils;

import java.net.URI;

/**
 * Create date 2020/8/14.
 *
 * @author evan
 */
@Configuration
@ConditionalOnProperty(name = "wayne.sso.enabled", havingValue = "true", matchIfMissing = false)
@AutoConfigureBefore(
        name =
                "org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientAutoConfiguration")
public class SsoAutoConfiguration {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${spring.security.oauth2.client.provider.uaa.jwk-set-uri}")
    private String jwkUri;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public OAuth2AuthorizationExceptionHandler oAuth2WebExceptionHandler(
            UnauthorizedHtmlWriter unauthorizedHtmlWriter) {
        return new OAuth2AuthorizationExceptionHandler(unauthorizedHtmlWriter);
    }

    @Bean
    public RouteLocator customRouteLocator(
            RouteLocatorBuilder builder, CookieTokenRelayGatewayFilterFactory filterFactory) {
        // @formatter:off
        return builder
                .routes()
                .route(
                        "resource",
                        r ->
                                r.path("/api/resource")
                                        .filters(f -> f.filter(filterFactory.apply()))
                                        .uri("http://localhost:8040"))
                .build();
        // @formatter:on
    }

    @Bean
    public UnauthorizedHtmlWriter unauthorizedHtmlWriter() {
        return new UnauthorizedHtmlWriter();
    }

    public JwtExpireWebFilter jwtExpireWebFilter() {
        JwtExpireWebFilter jwtExpireWebFilter = new JwtExpireWebFilter(jwkUri);
        jwtExpireWebFilter.setUnauthorizedHtmlWriter(unauthorizedHtmlWriter());
        return jwtExpireWebFilter;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity http,
            CookieServerAuthenticationSuccessHandler serverAuthenticationSuccessHandler,
            CookieRedirectServerLogoutSuccessHandler cookieRedirectServerLogoutSuccessHandler,
            HttpCookieServerSecurityContextRepository serverSecurityContextRepository,
            HttpCookieOAuth2ServerAuthorizationRequestRepository
                    httpCookieOAuth2ServerAuthorizationRequestRepository) {
        http.cors();
        http.csrf().disable();
        http.httpBasic().disable();
        // 用于用户登录成功后，重新恢复因为登录被打断的请求
        // http://blog.sina.com.cn/s/blog_6360eb050102vnyc.html
        http.requestCache().requestCache(NoOpServerRequestCache.getInstance());
        http.securityContextRepository(serverSecurityContextRepository);
        http.headers().frameOptions().disable();
        http.logout().logoutSuccessHandler(cookieRedirectServerLogoutSuccessHandler);
        http.authorizeExchange()
                .pathMatchers("/actuator/**", "/test/**", "/client/info", "/account/info", "/**/assets/**")
                .permitAll()
                .anyExchange()
                .authenticated();
        http.oauth2Login()
                .authenticationFailureHandler(
                        new ServerAuthenticationEntryPointFailureHandler(
                                (exchange, e) -> unauthorizedHtmlWriter().write(exchange.getResponse(), e)));
        http.addFilterBefore(jwtExpireWebFilter(), SecurityWebFiltersOrder.REACTOR_CONTEXT);
        http.oauth2Login()
                .authenticationMatcher(
                        new PathPatternParserServerWebExchangeMatcher(
                                contextPath + "/login/oauth2/code/{registrationId}"))
                .authorizationRequestRepository(httpCookieOAuth2ServerAuthorizationRequestRepository)
                .authenticationSuccessHandler(serverAuthenticationSuccessHandler);
        http.oauth2Client();
        return http.build();
    }

    @Bean
    public CookieRedirectServerLogoutSuccessHandler cookieRedirectServerLogoutSuccessHandler(
            final CookieTokenProcessor cookieTokenProcessor,
            @Value("${wayne.sso.logout-success}") String logoutSuccessUrl) {
        CookieRedirectServerLogoutSuccessHandler c =
                new CookieRedirectServerLogoutSuccessHandler(cookieTokenProcessor);
        if (StringUtils.hasText(logoutSuccessUrl)) {
            c.setLogoutSuccessUrl(URI.create(logoutSuccessUrl));
        }
        return c;
    }

    @Bean
    public CookieServerAuthenticationSuccessHandler serverAuthenticationSuccessHandler(
            @Value("${wayne.sso.login-success}") String loginSuccess,
            final CookieTokenProcessor cookieTokenProcessor,
            final ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
        CookieServerAuthenticationSuccessHandler cookieServerAuthenticationSuccessHandler =
                new CookieServerAuthenticationSuccessHandler(
                        authorizedClientRepository, loginSuccess, cookieTokenProcessor);
        return cookieServerAuthenticationSuccessHandler;
    }

    /**
     * SecurityContext 仓储，负责存储和加载 SecurityContext，加载之前需要解析 需要传入一个 jwk-set-uri 用于构造jwt解析器
     *
     * @return
     */
    @Bean
    public HttpCookieServerSecurityContextRepository serverSecurityContextRepository(
            CookieTokenProcessor cookieTokenProcessor) {
        return new HttpCookieServerSecurityContextRepository(jwkUri, cookieTokenProcessor);
    }

    @Bean
    public CookieTokenProcessor cookieTokenProcessor(
            @Value("${wayne.sso.cookie-domain}") String cookieDomain,
            @Value("${wayne.sso.cookie-timeout-seconds}") Integer cookieTimeoutSeconds) {
        return new CookieTokenProcessor(cookieDomain, cookieTimeoutSeconds);
    }

    /**
     * gateway过滤器(主要做续签)
     *
     * @param repository
     * @param clientRegistrationRepository
     * @param connector
     * @param cookieTokenProcessor
     * @return
     */
    @Bean
    public CookieTokenRelayGatewayFilterFactory cookieTokenRelayGatewayFilterFactory(
            ServerOAuth2AuthorizedClientRepository repository,
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ClientHttpConnector connector,
            CookieTokenProcessor cookieTokenProcessor) {
        return new CookieTokenRelayGatewayFilterFactory(
                repository, clientRegistrationRepository, connector, jwkUri, cookieTokenProcessor);
    }

    @Bean
    public WayneSetResponseHeaderGatewayFilterFactory wayneSetResponseHeaderGatewayFilterFactory() {
        return new WayneSetResponseHeaderGatewayFilterFactory();
    }
    @Bean
    public WayneRemoveResponseHeaderGatewayFilterFactory wayneRemoveResponseHeaderGatewayFilterFactory() {
        return new WayneRemoveResponseHeaderGatewayFilterFactory();
    }
    /**
     * 授权请求管理
     *
     * @return
     */
    @Bean
    public HttpCookieOAuth2ServerAuthorizationRequestRepository
    httpCookieOAuth2ServerAuthorizationRequestRepository(
            CookieTokenProcessor cookieTokenProcessor) {
        HttpCookieOAuth2ServerAuthorizationRequestRepository rep =
                new HttpCookieOAuth2ServerAuthorizationRequestRepository(cookieTokenProcessor);
        return rep;
    }
}
