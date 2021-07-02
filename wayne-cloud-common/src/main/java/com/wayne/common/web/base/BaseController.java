package com.wayne.common.web.base;

import com.wayne.common.web.domain.response.ResultController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

/**
 * Describe: 基 础 控 制 器 类
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
public class BaseController extends ResultController {

    public Object getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null != authentication && authentication.isAuthenticated()) {
            return authentication.getPrincipal();
        }
        Assert.notNull(null, "无法获取当前用户信息");
        return null;
    }

    public String getCurrentUserName() {
        Object currentUser = getCurrentUser();
        String userName = null;
        if (currentUser instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) currentUser;
            userName = springSecurityUser.getUsername();
        } else if (currentUser instanceof String) {
            userName = (String) currentUser;
        }
        Assert.notNull(userName, "无法获取当前用户信息");
        return userName;
    }
}
