package com.wayne.common.web.support;

import com.wayne.common.config.proprety.AuthProperty;
import com.wayne.common.plugin.system.domain.SysBaseUser;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Describe: 自定义 Security 权限注解实现
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
//@Component
//@EnableConfigurationProperties({AuthProperty.class})
public class SecurePermissionSupport implements PermissionEvaluator {

    @Resource
    private AuthProperty authProperty;

    /**
     * 预留接口
     * Describe: 自定义 Security 权限认证 @hasPermission
     * Param: Authentication
     * Return Boolean
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        SysBaseUser securityUserDetails = (SysBaseUser) authentication.getPrincipal();
        if (authProperty.isSuperAuthOpen() && authProperty.getSuperAdmin().equals(securityUserDetails.getUsername())) {
            return true;
        }
        List<String> powerCodeList = securityUserDetails.getPowerCodeList();
        Set<String> permissions = new HashSet<>();
        for (String powerCode : powerCodeList) {
            permissions.add(powerCode);
        }
        return permissions.contains(permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
