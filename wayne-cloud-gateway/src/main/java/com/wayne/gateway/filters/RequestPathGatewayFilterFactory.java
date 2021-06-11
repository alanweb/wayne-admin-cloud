package com.wayne.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RequestPathGatewayFilterFactory extends AbstractGatewayFilterFactory<RequestPathGatewayFilterFactory.Config> {
    public RequestPathGatewayFilterFactory(){
        super(Config.class);
    }
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("name", "path");
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().toString();
            System.out.println("路由过滤器：本次请求的地址是="+path+";配置的参数是：【name="+config.getName()+",path="+config.getPath()+"】");
            return chain.filter(exchange);
        };
    }

    public static class Config {
        private String name;
        private String path;
        public Config() {}
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
