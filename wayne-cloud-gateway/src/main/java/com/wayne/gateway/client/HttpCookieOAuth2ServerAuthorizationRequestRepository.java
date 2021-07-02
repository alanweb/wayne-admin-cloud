package com.wayne.gateway.client;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpCookie;
import org.springframework.security.oauth2.client.web.server.ServerAuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Create date 2020/9/22.
 *
 * <p>AuthorizationRequestRepository负责从启动授权请求到收到授权响应时（回调）的持久性OAuth2AuthorizationRequest。
 * 这个难道和上次讨论的那个回调地址的有关？？ https://blog.csdn.net/hadues/article/details/89298510
 *
 * @author evan
 */
public class HttpCookieOAuth2ServerAuthorizationRequestRepository
    implements ServerAuthorizationRequestRepository<OAuth2AuthorizationRequest> {

  private static final Logger logger =
      LoggerFactory.getLogger(HttpCookieOAuth2ServerAuthorizationRequestRepository.class);

  public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
  public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
  private static final long cookieExpireSeconds = 30 * 21 * 60 * 60;

  private final CookieTokenProcessor cookieTokenProcessor;

  public HttpCookieOAuth2ServerAuthorizationRequestRepository(
      CookieTokenProcessor cookieTokenProcessor) {
    this.cookieTokenProcessor = cookieTokenProcessor;
  }

  @Override
  public Mono<OAuth2AuthorizationRequest> loadAuthorizationRequest(ServerWebExchange exchange) {
    HttpCookie httpCookie =
        cookieTokenProcessor.getCookie(
            exchange.getRequest(), OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
    if (null != httpCookie) {
      OAuth2AuthorizationRequest r =
          CookieSerializeUtils.deserialize(httpCookie, OAuth2AuthorizationRequest.class);
      logger.info("load authorization request {}", r.getRedirectUri());

      return Mono.justOrEmpty(r);
    }
    return Mono.empty();
  }

  @Override
  public Mono<Void> saveAuthorizationRequest(
      OAuth2AuthorizationRequest authorizationRequest, ServerWebExchange exchange) {
    if (null == authorizationRequest) {
      cookieTokenProcessor.delete(
          exchange.getRequest(), exchange.getResponse(), OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
      cookieTokenProcessor.delete(
          exchange.getRequest(), exchange.getResponse(), REDIRECT_URI_PARAM_COOKIE_NAME);
      return Mono.empty();
    }

    logger.info("save authorization request {}", JSON.toJSONString(authorizationRequest));

    cookieTokenProcessor.writeCookie(
        exchange.getResponse(),
        OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
        CookieSerializeUtils.serialize(authorizationRequest),
        Duration.ofSeconds(cookieExpireSeconds),
        true);

    String redirectUriAfterLogin =
        exchange.getRequest().getQueryParams().getFirst(REDIRECT_URI_PARAM_COOKIE_NAME);
    if (!StringUtils.isEmpty(redirectUriAfterLogin)) {
      cookieTokenProcessor.writeCookie(
          exchange.getResponse(),
          REDIRECT_URI_PARAM_COOKIE_NAME,
          redirectUriAfterLogin,
          Duration.ofSeconds(cookieExpireSeconds),
          true);
    }
    return Mono.empty();
  }

  @Override
  public Mono<OAuth2AuthorizationRequest> removeAuthorizationRequest(ServerWebExchange exchange) {
    cookieTokenProcessor.delete(
        exchange.getRequest(), exchange.getResponse(), OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
    cookieTokenProcessor.delete(
        exchange.getRequest(), exchange.getResponse(), REDIRECT_URI_PARAM_COOKIE_NAME);
    return this.loadAuthorizationRequest(exchange);
  }
}
