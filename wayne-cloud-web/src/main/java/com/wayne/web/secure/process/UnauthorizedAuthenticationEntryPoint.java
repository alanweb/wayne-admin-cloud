package com.wayne.web.secure.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsProcessor;
import org.springframework.web.cors.DefaultCorsProcessor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create date 2020/9/6.
 *
 * <p>认证入口
 *
 * <p>默认情况下登陆失败会跳转页面，这里自定义，同时判断是否ajax请求，是ajax请求则返回json，否则跳转失败页面
 *
 * @author evan
 */
public class UnauthorizedAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private static final Logger logger =
      LoggerFactory.getLogger(UnauthorizedAuthenticationEntryPoint.class);

  private CorsProcessor corsProcessor = new DefaultCorsProcessor();

  private CorsConfiguration corsConfiguration = corsConfiguration();

  public CorsConfiguration corsConfiguration() {
    CorsConfiguration config = new CorsConfiguration();
    config.addAllowedMethod("*");
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.setAllowCredentials(true);
    return config;
  }

  /**
   * 这里参数request是遇到了认证异常authException用户请求，response是将要返回给客户的相应，
   * 方法commence实现,也就是相应的认证方案逻辑会修改response并返回给用户引导用户进入认证流程。
   *
   * @param request
   * @param response
   * @param authException
   * @throws ServletException
   */
  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException) {

    try {
      corsProcessor.processRequest(corsConfiguration, request, response);
    } catch (IOException e) {
      logger.warn("", e);
    }
    try {
      if (isAjaxRequest(request)) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
      } else {
        response.sendRedirect("/wayne-web/login");
      }
    } catch (IOException e) {
      logger.warn("", e);
    }
  }

  public static boolean isAjaxRequest(HttpServletRequest request) {
    String ajaxFlag = request.getHeader("X-Requested-With");
    return ajaxFlag != null && "XMLHttpRequest".equals(ajaxFlag);
  }
}
