package com.wayne.auth;

import com.wayne.auth.property.SecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

/**
 * @Author bin.wei
 * @Date 2021/7/5
 * @Description
 */
@EnableJdbcHttpSession
@EnableConfigurationProperties({SecurityProperties.class})
@SpringBootApplication(scanBasePackages = {"com.wayne"})
@EnableFeignClients
@EnableDiscoveryClient
public class AuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }
}
