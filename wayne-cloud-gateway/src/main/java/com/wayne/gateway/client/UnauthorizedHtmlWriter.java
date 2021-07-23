package com.wayne.gateway.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Slf4j
public class UnauthorizedHtmlWriter implements HttpStatusCodeHtmlWriter {

    @Value("${portal-url}")
    private String portalUrl;

    private String errMsg = "";

    @Override
    public Mono<Void> write(ServerHttpResponse response) {
        DataBuffer buffer = response.bufferFactory().wrap(errMsg.getBytes(StandardCharsets.UTF_8));
        response.getHeaders().set("Content-Type", "text/html");
        return response.writeWith(Flux.just(buffer));
    }

    public Mono<Void> write(ServerHttpResponse response, Exception e) {
        errMsg =
                " <h2> You signed in with another tab or window . Reload to refresh your session. </h2> ";
        if (e instanceof OAuth2AuthenticationException) {
            OAuth2AuthenticationException exception = (OAuth2AuthenticationException) e;
            String errorCode = exception.getError().getErrorCode();
            errMsg = errMsg + " <h1> <a href=\"" + portalUrl + "\"> Reload </a> </h1> ";
            log.warn("身份验证异常：<{}>", errorCode, e.getMessage());
        } else if (e instanceof OAuth2AuthorizationException) {
            OAuth2AuthorizationException exception = (OAuth2AuthorizationException) e;
            String errorCode = exception.getError().getErrorCode();
            errMsg = errMsg + " <h1> <a href=\"" + portalUrl + "\"> Reload </a> </h1> ";
            log.warn("授权失败：<{}>", errorCode);
        } else {
            errMsg = "Unknown exception." + " <h1> <a href=\"" + portalUrl + "\"> Reload </a> </h1> ";
            log.warn("[UnauthorizedJsonWriter] 未知异常: <{}>", e.getClass().getName(), e);
        }
        return write(response);
    }
}
