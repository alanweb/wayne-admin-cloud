package com.wayne.common.web.base;

import com.wayne.common.plugin.system.domain.SysBaseUser;
import com.wayne.common.plugin.system.service.SysContext;
import com.wayne.common.web.domain.response.ResultController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Describe: 基 础 控 制 器 类
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
public class BaseController extends ResultController {

    @Autowired
    private SysContext sysContext;

    public SysBaseUser getCurrentUser() {
        String userName = getCurrentUserName();
        SysBaseUser user = sysContext.getUserByName(userName);
        Assert.notNull(user, "无法获取当前用户信息");
        return user;
    }

    public String getCurrentUserName() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> userName = request.getHeaders("user_name");
        Assert.isTrue(userName.hasMoreElements(), "无法获取当前用户信息");
        return userName.nextElement();
    }

    public String getCurrentUserId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> userId = request.getHeaders("user_id");
        Assert.isTrue(userId.hasMoreElements(), "无法获取当前用户信息");
        return userId.nextElement();
    }
}
