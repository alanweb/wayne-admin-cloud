package com.wayne.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author bin.wei
 * @Date 2021/6/11
 * @Description
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class},scanBasePackages = {"com.wayne"})
@EnableFeignClients
@EnableDiscoveryClient
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
