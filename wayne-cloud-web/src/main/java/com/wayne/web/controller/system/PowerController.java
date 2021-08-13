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
@Api(tags = {"权限管理"})
@RequestMapping("power")
public class PowerController extends BaseController {
    /**
     * Describe: 基础路径
     * */
    private static String MODULE_PATH = "system/power/" ;

    @Autowired
    private SystemService systemService;

    /**
     * Describe: 获取权限列表视图
     * Return 权限列表视图
     * */
    @GetMapping("main")
    @PreAuthorize("hasAnyAuthority('sys:power:main')")
    public ModelAndView main(){
        return jumpPage(MODULE_PATH + "main");
    }

    /**
     * Describe: 获取权限新增视图
     * Param ModelAndView
     * Return 权限新增视图
     * */
    @GetMapping("add")
    @PreAuthorize("hasAnyAuthority('sys:power:add')")
    public ModelAndView add(){
        return jumpPage(MODULE_PATH + "add");
    }

    /**
     * Describe: 获取权限修改视图
     * Param powerId
     * Return 权限修改视图
     * */
    @GetMapping("edit")
    @PreAuthorize("hasAnyAuthority('sys:power:edit')")
    public ModelAndView edit(Model model, String powerId){
        model.addAttribute("sysPower",systemService.getPowerById(powerId));
        return jumpPage(MODULE_PATH + "edit");
    }

}
