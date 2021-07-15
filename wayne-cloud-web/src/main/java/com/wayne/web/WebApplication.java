package com.wayne.web;

import com.wayne.common.config.proprety.SwaggerProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author bin.wei
 * @Date 2021/6/11
 * @Description
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class},scanBasePackages = {"com.wayne"})
@EnableFeignClients
@EnableDiscoveryClient
@EnableConfigurationProperties(SwaggerProperty.class)
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
