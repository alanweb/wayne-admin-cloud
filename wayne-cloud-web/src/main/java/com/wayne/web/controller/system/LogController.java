package com.wayne.web.controller.system;

import com.wayne.common.web.base.BaseController;
import com.wayne.web.service.SystemService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Api(tags = {"操作日志"})
@RequestMapping("log")
public class LogController extends BaseController {

    /**
     * Describe: 基础路径
     */
    private static String MODULE_PATH = "system/log/";

    /**
     * Describe: 获取日志列表视图
     * Return 日志列表视图
     */
    @GetMapping("main")
    public ModelAndView main() {
        return jumpPage(MODULE_PATH + "main");
    }

    /**
     * Describe: 日志详情
     * Return: ModelAndView
     */
    @GetMapping("info")
    @PreAuthorize("hasAnyAuthority('sys:log:info')")
    public ModelAndView details() {
        return jumpPage(MODULE_PATH + "info");
    }

}
