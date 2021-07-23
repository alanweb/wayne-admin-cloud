package com.wayne.common.config.proprety;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "wayne.auth",ignoreUnknownFields = false)
public class AuthProperty {

    /**
     * 超级管理员不认证
     */
    private boolean superAuthOpen = false;

    /**
     * 不验证权限用户名
     */
    private String superAdmin = "admin";
}