package com.wayne.system.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("wayne.security")
public class SecurityProperty {

    /**
     * 超级管理员不认证
     */
    private boolean superAuthOpen;

    /**
     * 不验证权限用户名
     */
    private String superAdmin;
}