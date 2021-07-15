package com.wayne.auth.process;

import com.wayne.common.exception.auth.CaptchaException;
import com.wayne.common.plugin.logging.aop.enums.BusinessType;
import com.wayne.common.plugin.logging.aop.enums.LoggingType;
import com.wayne.common.plugin.system.domain.SysBaseLog;
import com.wayne.common.tools.sequence.SequenceUtil;
import com.wayne.common.web.domain.response.Result;
import com.wayne.auth.service.SystemService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Describe: 自定义 Security 用户登录失败处理类
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@Component
public class SecureAuthenticationFailureHandler extends ExceptionMappingAuthenticationFailureHandler {

    /**
     * 引 入 日 志 服 务
     */
    @Resource
    private SystemService systemService;

    public SecureAuthenticationFailureHandler() {
        super();
        String defaultFailureUrl = "/login?error";
        Map<String, String> failureUrlMap = new HashMap<>();
        failureUrlMap.put(
                InternalAuthenticationServiceException.class.getName(),
                defaultFailureUrl + "=INTERNAL_AUTHENTICATION_SERVICE");
        failureUrlMap.put(
                BadCredentialsException.class.getName(), defaultFailureUrl + "=BAD_CREDENTIALS");
        super.setDefaultFailureUrl(defaultFailureUrl);
        super.setExceptionMappings(failureUrlMap);
    }

    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        Result result = Result.failure(500, "登录失败");
        if (e instanceof CaptchaException) {
            result.setMsg("验证码有误");
        }
        if (e instanceof UsernameNotFoundException) {
            result.setMsg("用户名不存在");
        }
        if (e instanceof LockedException) {
            result.setMsg("用户冻结");
        }
        if (e instanceof BadCredentialsException) {
            result.setMsg("账户密码不正确");
        }
        if (e instanceof DisabledException) {
            result.setMsg("用户未启用");
        }
        SysBaseLog sysLog = new SysBaseLog();
        sysLog.setId(SequenceUtil.makeStringId());
        sysLog.setTitle("登录");
        sysLog.setDescription(result.getMsg());
        sysLog.setBusinessType(BusinessType.OTHER);
        sysLog.setSuccess(false);
        sysLog.setLoggingType(LoggingType.LOGIN);
        systemService.saveLog(sysLog);
        super.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
    }
}
