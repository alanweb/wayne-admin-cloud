package com.wayne.auth.controller;

import com.wayne.common.tools.secure.SecurityUtil;
import com.wayne.common.web.base.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author bin.wei
 * @Date 2021/7/6
 * @Description
 */
@RestController
public class LoginController extends BaseController {
    /**
     * Describe: 获取登录视图
     * Param: ModelAndView
     * Return: 登录视图
     */
    @GetMapping("login")
    public ModelAndView login(HttpServletRequest request) {
        if (SecurityUtil.isAuthentication()) {
            //SecureSessionService.expiredSession(request, sessionRegistry);
            return jumpPage("/index");
        } else {
            return jumpPage("/login");
        }
    }

    @GetMapping("index")
    public ModelAndView index(HttpServletRequest request) {
        return jumpPage("/index");
    }
}
