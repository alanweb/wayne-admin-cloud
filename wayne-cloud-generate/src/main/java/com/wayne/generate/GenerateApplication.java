package com.wayne.generate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author bin.wei
 * @Date 2021/6/11
 * @Description
 */
@EnableDiscoveryClient
@ServletComponentScan
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class}, scanBasePackages = {"com.wayne"})
public class GenerateApplication {
    public static void main(String[] args) {
        SpringApplication.run(GenerateApplication.class, args);
    }
}
