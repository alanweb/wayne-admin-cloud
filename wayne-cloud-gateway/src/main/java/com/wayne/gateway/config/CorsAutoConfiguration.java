package com.wayne.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * 实现基本的跨域请求 https://www.cnblogs.com/hfultrastrong/p/11497321.html
 * https://blog.csdn.net/xiaoshiyiqie/article/details/85697889
 */
@Configuration
public class CorsAutoConfiguration {

  @Bean
  public CorsConfiguration corsConfiguration() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.addAllowedHeader("*"); // 允许任何头
    corsConfiguration.addAllowedMethod("*"); // 允许任何方法（post、get等）
    corsConfiguration.addAllowedOrigin("*"); // 允许任何域名使用
    corsConfiguration.setAllowCredentials(true); // 表明它允许cookies
    corsConfiguration.setMaxAge(600L); // 表明在600秒内，不需要再发送预检验请求
    return corsConfiguration;
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource(CorsConfiguration corsConfiguration) {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration); // 对接口配置跨域设置
    return source;
  }
}
