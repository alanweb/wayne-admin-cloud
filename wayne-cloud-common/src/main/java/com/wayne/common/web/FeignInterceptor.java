package com.wayne.common.web;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import javax.servlet.http.Cookie;
import java.util.Arrays;

/**
 * 支持feign 通过oauth的资源认证
 */
@Component
public class FeignInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Cookie[] cookies = attributes.getRequest().getCookies();
        Cookie accessToken = Arrays.stream(cookies).filter(cookie -> "access_token".equals(cookie.getName())).findFirst().get();
        System.out.println("accessToken:" + accessToken.getValue());
        template.header(AUTHORIZATION_HEADER, "Bearer " + accessToken.getValue());
    }
}