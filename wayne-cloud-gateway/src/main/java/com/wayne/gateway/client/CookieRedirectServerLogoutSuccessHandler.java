package com.wayne.gateway.client;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import reactor.core.publisher.Mono;

/**
 * Create date 2020/9/3.
 *
 * @author evan
 */
public class CookieRedirectServerLogoutSuccessHandler extends RedirectServerLogoutSuccessHandler {

  private final CookieTokenProcessor cookieTokenProcessor;

  public CookieRedirectServerLogoutSuccessHandler(CookieTokenProcessor cookieTokenProcessor) {
    this.cookieTokenProcessor = cookieTokenProcessor;
  }

  @Override
  public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
    cookieTokenProcessor.delete(
        exchange.getExchange().getRequest(),
        exchange.getExchange().getResponse(),
        CookieOAuth2Helper.ACCESS_TOKEN);
    cookieTokenProcessor.delete(
        exchange.getExchange().getRequest(),
        exchange.getExchange().getResponse(),
        CookieOAuth2Helper.REFRESH_TOKEN);
    return super.onLogoutSuccess(exchange, authentication);
  }
}
