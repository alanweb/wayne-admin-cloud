package com.wayne.auth.support;

import com.alibaba.fastjson.JSON;
import com.wayne.common.tools.servlet.ServletUtil;
import com.wayne.common.tools.string.StringUtil;
import com.wayne.common.web.domain.response.Result;
import com.wayne.auth.extension.UnauthorizedAuthenticationEntryPoint;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登 录 验 证 码 过 滤 器
 *
 * @author John Ming
 * @createTime 2020/11/20
 */
@Component
public class SecureCaptchaSupport extends OncePerRequestFilter implements Filter {

    private final String defaultFilterProcessUrl = "/login";
    private final String method = "POST";

    /**
     * 验 证 码 校 监 逻 辑
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (method.equalsIgnoreCase(request.getMethod()) && defaultFilterProcessUrl.equals(request.getServletPath())) {
            String captcha = ServletUtil.getRequest().getParameter("captcha");
            if (UnauthorizedAuthenticationEntryPoint.isAjaxRequest(request)) {
                if (toAjax(response, captcha)) {
                    return;
                }
            } else
                toFrom(response, captcha);
        }
        chain.doFilter(request, response);
    }

    private void toFrom(HttpServletResponse response, String captcha) throws IOException {
        if (StringUtil.isEmpty(captcha) || !CaptchaUtil.ver(captcha, ServletUtil.getRequest())) {
            response.sendRedirect("/login?error=BAD_CAPTCHA");
        }
    }

    private boolean toAjax(HttpServletResponse response, String captcha) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        if (StringUtil.isEmpty(captcha)) {
            response.getWriter().write(JSON.toJSONString(Result.failure("验证码不能为空!")));
            return true;
        }
        if (!CaptchaUtil.ver(captcha, ServletUtil.getRequest())) {
            response.getWriter().write(JSON.toJSONString(Result.failure("验证码错误!")));
            return true;
        }
        return false;
    }
}