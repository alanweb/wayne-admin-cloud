package com.wayne.gateway.client;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

/**
 * 登陆成功之后，将 Authentication 提取出 access_token, refresh_token Create date 2020/8/18.
 *
 * @author evan
 */
public class CookieServerAuthenticationSuccessHandler
    extends RedirectServerAuthenticationSuccessHandler {

  private final ServerOAuth2AuthorizedClientRepository serverOAuth2AuthorizedClientRepository;
  private final CookieTokenProcessor cookieTokenProcessor;

  public CookieServerAuthenticationSuccessHandler(
      ServerOAuth2AuthorizedClientRepository serverOAuth2AuthorizedClientRepository,
      String location,
      CookieTokenProcessor cookieTokenProcessor) {
    super(location);
    this.serverOAuth2AuthorizedClientRepository = serverOAuth2AuthorizedClientRepository;
    this.cookieTokenProcessor = cookieTokenProcessor;
  }

  @Override
  public Mono<Void> onAuthenticationSuccess(
      WebFilterExchange webFilterExchange, Authentication authentication) {
    // 将access_token  和 refresh_token 写入到 Cookie 中
    return writeCookie(webFilterExchange, authentication)
        .then(super.onAuthenticationSuccess(webFilterExchange, authentication));
  }

  public Mono<Void> writeCookie(
      WebFilterExchange webFilterExchange, Authentication authentication) {
    if (authentication instanceof OAuth2AuthenticationToken) {
      OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
      String registrationId = authenticationToken.getAuthorizedClientRegistrationId();
      return this.serverOAuth2AuthorizedClientRepository
          .loadAuthorizedClient(registrationId, authentication, webFilterExchange.getExchange())
          .flatMap(client -> Mono.defer(() -> this.handleWrite(client, webFilterExchange)));
    }
    return Mono.empty();
  }

  protected Mono<Void> handleWrite(
      OAuth2AuthorizedClient client, WebFilterExchange webFilterExchange) {
    return Mono.fromRunnable(
        () -> {
          OAuth2AccessToken accessToken = client.getAccessToken();
          OAuth2RefreshToken refreshToken = client.getRefreshToken();
          cookieTokenProcessor.writeAccessToken(
              accessToken.getTokenValue(), webFilterExchange.getExchange());
          cookieTokenProcessor.writeRefreshToken(
              refreshToken.getTokenValue(), webFilterExchange.getExchange());
        });
  }
}
