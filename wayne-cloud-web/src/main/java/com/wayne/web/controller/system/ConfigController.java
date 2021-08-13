package com.wayne.web.controller.system;

import com.wayne.common.web.base.BaseController;
import com.wayne.web.service.SystemService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Api(tags = {"系统配置"})
@RequestMapping("config")
public class ConfigController extends BaseController {

    /**
     * Describe: 基础路径
     */
    private static String MODULE_PATH = "system/config/";

    @Autowired
    private SystemService systemService;

    /**
     * Describe: 获取配置视图
     * Return 配置列表视图
     */
    @GetMapping("main")
    public ModelAndView main() {
        return jumpPage(MODULE_PATH + "main");
    }

    /**
     * Describe: 获取配置新增视图
     * Return 配置新增视图
     */
    @GetMapping("add")
    public ModelAndView add() {
        return jumpPage(MODULE_PATH + "add");
    }

    /**
     * Describe: 获取配置修改视图
     * Param configId
     * Return 配置修改视图
     */
    @GetMapping("edit")
    public ModelAndView edit(Model model, String configId) {
        model.addAttribute("sysConfig", systemService.getConfigById(configId));
        return jumpPage(MODULE_PATH + "edit");
    }

}
