package com.wayne.web.controller.system;

import com.wayne.common.web.base.BaseController;
import com.wayne.web.service.SystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Api(tags = {"电子邮件"})
@RequestMapping("mail")
public class MailController extends BaseController {

    /**
     * Describe: 基础路径
     */
    private static String MODULE_PATH = "system/mail/" ;

    /**
     * Describe: 邮件管理页面
     * Return: ModelAndView
     */
    @GetMapping("/main")
    @ApiOperation(value = "邮件管理页面")
    @PreAuthorize("hasAnyAuthority('sys:mail:main')")
    public ModelAndView main() {
        return jumpPage(MODULE_PATH + "main");
    }

    /**
     * Describe: 邮件发送页面
     * Return: ModelAndView
     */
    @GetMapping("/add")
    @ApiOperation(value = "邮件发送页面")
    @PreAuthorize("hasAnyAuthority('sys:mail:add')")
    public ModelAndView add() {
        return jumpPage(MODULE_PATH + "add");
    }
}
