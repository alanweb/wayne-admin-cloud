package com.wayne.gateway.filters;

import com.wayne.gateway.client.CookieOAuth2Helper;
import com.wayne.gateway.client.UnauthorizedHtmlWriter;
import org.springframework.http.HttpCookie;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

public class JwtExpireWebFilter implements WebFilter {
  private static final String RES_TYPE_HEADER = "req-type";

  private UnauthorizedHtmlWriter unauthorizedHtmlWriter;
  private final NimbusJwtDecoder jwtDecoder;

  public JwtExpireWebFilter(String jwkUri) {
    this.jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkUri).build();
    this.jwtDecoder.setJwtValidator(new JwtTimestampValidator(Duration.ZERO));
  }

  public void setUnauthorizedHtmlWriter(UnauthorizedHtmlWriter unauthorizedHtmlWriter) {
    this.unauthorizedHtmlWriter = unauthorizedHtmlWriter;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    List<String> list = exchange.getRequest().getHeaders().get(RES_TYPE_HEADER);
    if (list == null || list.isEmpty()) {
      return chain.filter(exchange);
    }

    if (unauthorizedHtmlWriter != null && isExpire(exchange)) {
      return unauthorizedHtmlWriter.write(exchange.getResponse());
    }
    return chain.filter(exchange);
  }

  /**
   * 判断exchange中的token是否过期 如果access_token与reg=fresh_token同时为null或者同时过期，则返回true
   *
   * @param exchange
   * @return
   */
  private boolean isExpire(ServerWebExchange exchange) {
    HttpCookie accessToken = CookieOAuth2Helper.getAccessTokenCookie(exchange);
    HttpCookie refreshToken = CookieOAuth2Helper.getRefreshTokenCookie(exchange);

    if (accessToken == null) {
      return refreshToken == null;
    } else if (refreshToken != null) {
      return getJwt(accessToken) == null && getJwt(refreshToken) == null;
    }

    return false;
  }

  /**
   * 通过cookie获取jwt，如果cookie不存在或者jwt过期，则返回null
   *
   * @param httpCookie
   * @return
   */
  private Jwt getJwt(HttpCookie httpCookie) {
    try {
      return this.jwtDecoder.decode(httpCookie.getValue());
    } catch (JwtValidationException e) {
      return null;
    }
  }
}
