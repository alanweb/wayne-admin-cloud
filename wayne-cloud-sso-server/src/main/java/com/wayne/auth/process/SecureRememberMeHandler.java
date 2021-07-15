package com.wayne.auth.process;

import com.wayne.auth.session.SecureSessionService;
import com.wayne.common.plugin.logging.aop.enums.BusinessType;
import com.wayne.common.plugin.logging.aop.enums.LoggingType;
import com.wayne.common.plugin.system.domain.SysBaseLog;
import com.wayne.common.plugin.system.domain.SysBaseUser;
import com.wayne.common.tools.secure.SecurityUtil;
import com.wayne.common.tools.sequence.SequenceUtil;
import com.wayne.auth.service.SystemService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class SecureRememberMeHandler implements AuthenticationSuccessHandler {
    @Value("${wayne.account.host:http://wayne-gateway.local.wayne.com:8080}")
    private String redirectUrl;

    @Resource
    private SystemService systemService;
    @Resource
    private SessionRegistry sessionRegistry;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        // 记录日志
        SysBaseLog sysLog = new SysBaseLog();
        sysLog.setId(SequenceUtil.makeStringId());
        sysLog.setTitle("Remember Me");
        sysLog.setDescription("登录成功");
        sysLog.setBusinessType(BusinessType.OTHER);
        sysLog.setSuccess(true);
        sysLog.setLoggingType(LoggingType.LOGIN);
        systemService.saveLog(sysLog);

        // 更新用户
        SysBaseUser sysUser = new SysBaseUser();
        // 获取最近登录时间
        LocalDateTime now = LocalDateTime.now();
        sysUser.setUserId(((SysBaseUser) SecurityUtil.currentUser().getPrincipal()).getUserId());
        sysUser.setLastTime(now);
        systemService.updateUser(sysUser);

        SysBaseUser currentUser = (SysBaseUser) authentication.getPrincipal();
        currentUser.setLastTime(now);
        request.getSession().setAttribute("currentUser", currentUser);
        SecureSessionService.expiredSession(request, sessionRegistry);
        // 注册新的SessionInformation
        sessionRegistry.registerNewSession(request.getSession().getId(), authentication.getPrincipal());
        response.sendRedirect(redirectUrl);
    }
}
