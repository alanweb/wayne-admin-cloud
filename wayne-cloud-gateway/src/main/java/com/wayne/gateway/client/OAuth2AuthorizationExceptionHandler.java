package com.wayne.gateway.client;

import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

public class OAuth2AuthorizationExceptionHandler implements WebExceptionHandler {
  private UnauthorizedHtmlWriter unauthorizedHtmlWriter;

  public OAuth2AuthorizationExceptionHandler(UnauthorizedHtmlWriter unauthorizedHtmlWriter) {
    this.unauthorizedHtmlWriter = unauthorizedHtmlWriter;
  }

  @Override
  public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
    if (ex.getClass() == OAuth2AuthorizationException.class) {
      return unauthorizedHtmlWriter.write(
          exchange.getResponse(), (OAuth2AuthorizationException) ex);
    }
    return Mono.error(ex);
  }
}
