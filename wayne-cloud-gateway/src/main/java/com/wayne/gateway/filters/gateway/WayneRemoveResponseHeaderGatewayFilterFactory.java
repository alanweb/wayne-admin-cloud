package com.wayne.gateway.filters.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.springframework.cloud.gateway.support.GatewayToStringStyler.filterToStringCreator;

public class WayneRemoveResponseHeaderGatewayFilterFactory
        extends AbstractGatewayFilterFactory<AbstractGatewayFilterFactory.NameConfig> {

    public WayneRemoveResponseHeaderGatewayFilterFactory() {
        super(NameConfig.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(NAME_KEY);
    }

    @Override
    public GatewayFilter apply(NameConfig config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange,
                                     GatewayFilterChain chain) {
                return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                    HttpHeaders headers = exchange
                            .getResponse().getHeaders();
                    headers = HttpHeaders.writableHttpHeaders(headers);
                    headers.remove(config.getName());
                }));
            }

            @Override
            public String toString() {
                return filterToStringCreator(
                        WayneRemoveResponseHeaderGatewayFilterFactory.this)
                        .append("name", config.getName()).toString();
            }
        };
    }

}