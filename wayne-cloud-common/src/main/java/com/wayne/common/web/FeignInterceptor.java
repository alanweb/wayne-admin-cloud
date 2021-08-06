package com.wayne.common.web;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Optional;

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
        if (ArrayUtils.isNotEmpty(cookies)) {
            Optional<Cookie> optional = Arrays.stream(cookies).filter(cookie -> "access_token".equals(cookie.getName())).findFirst();
            if (optional.isPresent()) {
                Cookie accessToken = optional.get();
                template.header(AUTHORIZATION_HEADER, "Bearer " + accessToken.getValue());
            }
        }
    }
}