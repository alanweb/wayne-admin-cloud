package com.wayne.gateway.filters.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

@Component
public class LoggerGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //前置过滤
        Date begin = new Date();
        System.out.println("全局过滤器执行，当前时刻是："+ begin.getTime());
        Mono<Void> mono = chain.filter(exchange);//继续向下执行
        //后置过滤
        Date end = new Date();
        System.out.println("全局过滤器执行，当前时刻是："+ end.getTime()+"\n本次执行耗时："+(end.getTime()-begin.getTime())+"毫秒!");
        return mono;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
