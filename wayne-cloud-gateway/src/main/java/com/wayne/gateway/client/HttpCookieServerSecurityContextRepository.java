package com.wayne.gateway.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Create date 2020/9/22.
 *
 * <p>SecurityContext 仓储，负责存储和加载 SecurityContext，加载之前需要解析
 *
 * @author evan
 */
public class HttpCookieServerSecurityContextRepository implements ServerSecurityContextRepository {

  private static final Logger logger =
      LoggerFactory.getLogger(HttpCookieServerSecurityContextRepository.class);

  private static final String CLIENT_ID = "client_id";
  private static final String USER_NAME = "user_name";

  private final NimbusJwtDecoder jwtDecoder;
  private final CookieTokenProcessor cookieTokenProcessor;

  public HttpCookieServerSecurityContextRepository(
      String jwkUri, CookieTokenProcessor cookieTokenProcessor) {
    this.cookieTokenProcessor = cookieTokenProcessor;
    this.jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkUri).build();
    this.jwtDecoder.setJwtValidator(new JwtTimestampValidator(Duration.ZERO));
  }

  @Override
  public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
    return Mono.empty();
  }

  /**
   * 加载 SecurityContext 时，需要将cookie解析（使用jwt解析）
   *
   * @param exchange
   * @return
   */
  @Override
  public Mono<SecurityContext> load(ServerWebExchange exchange) {

    Jwt jwt = null;

    // 校验access_token
    HttpCookie accessToken =
        exchange.getRequest().getCookies().getFirst(CookieOAuth2Helper.ACCESS_TOKEN);
    if (null != accessToken) {
      String value = accessToken.getValue();
      try {
        jwt = this.jwtDecoder.decode(value);
      } catch (JwtValidationException e) {
        //  过期警告
        logger.warn(e.getLocalizedMessage());
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        delToken(exchange);
        return Mono.empty();
      }
    }

    // 校验refresh_token
    if (jwt == null) {
      HttpCookie refreshToken =
          exchange.getRequest().getCookies().getFirst(CookieOAuth2Helper.REFRESH_TOKEN);
      try {
        jwt = this.jwtDecoder.decode(refreshToken.getValue());
      } catch (NullPointerException e) {
        return Mono.empty();
      } catch (JwtValidationException e) {
        logger.warn(e.getMessage());
        return Mono.empty();
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        delToken(exchange);
        return Mono.empty();
      }
    }

    List<String> authorities = (List<String>) jwt.getClaims().get("authorities");
    List<String> scopes = (List<String>) jwt.getClaims().get("scope");
    Set<GrantedAuthority> authoritiesSet = new HashSet<>();
    for (String authority : authorities) {
      authoritiesSet.add(new SimpleGrantedAuthority(authority));
    }
    for (String scope : scopes) {
      authoritiesSet.add(new SimpleGrantedAuthority("SCOPE_" + scope));
    }

    // 客户端模式下没有user_name,使用client_id替代,是否有更好的方式?
    String nameKey;
    if (jwt.getClaims().containsKey(USER_NAME)) {
      nameKey = USER_NAME;
    } else {
      nameKey = CLIENT_ID;
    }

    DefaultOAuth2User user = new DefaultOAuth2User(authoritiesSet, jwt.getClaims(), nameKey);
    OAuth2AuthenticationToken result =
        new OAuth2AuthenticationToken(user, authoritiesSet, "gateway");
    SecurityContextImpl securityContext = new SecurityContextImpl(result);
    return Mono.justOrEmpty(securityContext);
  }

  private void delToken(ServerWebExchange exchange) {
    cookieTokenProcessor.delete(exchange.getRequest(), exchange.getResponse(), "access_token");
    cookieTokenProcessor.delete(exchange.getRequest(), exchange.getResponse(), "refresh_token");
  }
}
