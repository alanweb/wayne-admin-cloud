package com.wayne.auth.process;

import com.wayne.common.plugin.logging.aop.enums.BusinessType;
import com.wayne.common.plugin.logging.aop.enums.LoggingType;
import com.wayne.common.plugin.system.domain.SysBaseLog;
import com.wayne.common.plugin.system.domain.SysBaseUser;
import com.wayne.common.tools.secure.SecurityUtil;
import com.wayne.common.tools.sequence.SequenceUtil;
import com.wayne.auth.service.SystemService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Describe: 自定义 Security 用户未登陆处理类
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@Component
public class SecureAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private RequestCache requestCache = new HttpSessionRequestCache();

    public SecureAuthenticationSuccessHandler() {
    }

    @Resource
    private SystemService systemService;

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SysBaseLog sysLog = new SysBaseLog();
        sysLog.setId(SequenceUtil.makeStringId());
        sysLog.setTitle("登录");
        sysLog.setDescription("登录成功");
        sysLog.setBusinessType(BusinessType.OTHER);
        sysLog.setSuccess(true);
        sysLog.setLoggingType(LoggingType.LOGIN);
        try {
            systemService.saveLog(sysLog);
        }catch (Exception e){
            e.printStackTrace();
        }
        SysBaseUser sysUser = new SysBaseUser();
        sysUser.setUserId(((SysBaseUser) SecurityUtil.currentUser().getPrincipal()).getUserId());
        sysUser.setLastTime(LocalDateTime.now());
        systemService.updateUser(sysUser);
        SysBaseUser currentUser = (SysBaseUser) authentication.getPrincipal();
        currentUser.setLastTime(LocalDateTime.now());
        request.getSession().setAttribute("currentUser", authentication.getPrincipal());
        SavedRequest savedRequest = this.requestCache.getRequest(request, response);
        if (savedRequest == null) {
            super.onAuthenticationSuccess(request, response, authentication);
        } else {
            String targetUrlParameter = this.getTargetUrlParameter();
            if (!this.isAlwaysUseDefaultTargetUrl() && (targetUrlParameter == null || !StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
                this.clearAuthenticationAttributes(request);
                String targetUrl = savedRequest.getRedirectUrl();
                this.logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
                this.getRedirectStrategy().sendRedirect(request, response, targetUrl);
            } else {
                this.requestCache.removeRequest(request, response);
                super.onAuthenticationSuccess(request, response, authentication);
            }
        }
    }

    public void setRequestCache(RequestCache requestCache) {
        this.requestCache = requestCache;
    }
}

