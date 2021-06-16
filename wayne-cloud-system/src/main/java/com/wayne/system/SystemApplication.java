package com.wayne.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author bin.wei
 * @Date 2021/6/9
 * @Description
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class}, scanBasePackages = {"com.wayne"})
@MapperScan(value = "com.wayne.system.mapper")
@EnableDiscoveryClient
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }
}
