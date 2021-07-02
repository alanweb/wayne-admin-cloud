package com.wayne.gateway.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import java.time.Duration;

/** @Author: baozi @Date: 2020/10/27 10:21 */
public class CookieTokenProcessor {

  private static final Logger logger = LoggerFactory.getLogger(CookieTokenProcessor.class);

  // 写cookie的域
  private final String cookieDomain;
  // token在cookie中的存活时间
  private final int tokenSessionTimeoutSeconds;

  public CookieTokenProcessor(String cookieDomain, int tokenSessionTimeoutSeconds) {
    this.cookieDomain = cookieDomain;
    this.tokenSessionTimeoutSeconds = tokenSessionTimeoutSeconds;
  }

  /**
   * Access token 由网关控制时间。
   *
   * @param accessToken
   * @param exchange
   */
  public void writeAccessToken(String accessToken, ServerWebExchange exchange) {
    writeCookie(exchange.getResponse(), CookieOAuth2Helper.ACCESS_TOKEN, accessToken, null, false);
  }

  /**
   * Refresh token 由网关控制时间。
   *
   * @param refreshToken
   * @param exchange
   */
  public void writeRefreshToken(String refreshToken, ServerWebExchange exchange) {
    writeCookie(
        exchange.getResponse(), CookieOAuth2Helper.REFRESH_TOKEN, refreshToken, null, false);
  }

  /**
   * 写Cookie
   *
   * @param response
   * @param name
   * @param value
   * @param maxAge
   * @param httpOnly
   */
  public void writeCookie(
      ServerHttpResponse response, String name, String value, Duration maxAge, boolean httpOnly) {
    if (!StringUtils.hasText(value)) {
      return;
    }
    if (maxAge == null) {
      maxAge = Duration.ofSeconds(tokenSessionTimeoutSeconds);
    }
    logger.debug("{}: {}", name, value);
    ResponseCookie cookie =
        ResponseCookie.from(name, value)
            .domain(cookieDomain)
            .httpOnly(httpOnly)
            .maxAge(maxAge)
            .path("/")
            .build();
    response.getHeaders().add(CookieOAuth2Helper.SET_COOKIE, cookie.toString());
  }

  /**
   * @param request
   * @param name
   * @return
   */
  public HttpCookie getCookie(ServerHttpRequest request, String name) {
    return request.getCookies().getFirst(name);
  }

  /**
   * 删除cookie
   *
   * @param name
   */
  public void delete(ServerHttpRequest request, ServerHttpResponse response, String name) {
    HttpCookie hc = request.getCookies().getFirst(name);

    if (null != hc) {
      ResponseCookie cookie =
          ResponseCookie.from(name, "")
              .domain(cookieDomain)
              .httpOnly(true)
              .maxAge(0)
              .path("/")
              .build();
      response.getHeaders().add(CookieOAuth2Helper.SET_COOKIE, cookie.toString());
    }
  }
}
