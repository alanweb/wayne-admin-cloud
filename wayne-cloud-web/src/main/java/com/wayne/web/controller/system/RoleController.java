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
@Api(tags = {"角色管理"})
@RequestMapping("role")
public class RoleController extends BaseController {
    @Autowired
    private SystemService systemService;

    /**
     * Describe: 基础路径
     */
    private static String MODULE_PATH = "system/role/";
    /**
     * Describe: 获取角色列表视图
     * Param ModelAndView
     * Return 用户列表视图
     */
    @GetMapping("main")
    @ApiOperation(value = "获取角色列表视图")
    @PreAuthorize("hasAnyAuthority('sys:role:main')")
    public ModelAndView main() {
        return jumpPage(MODULE_PATH + "main");
    }
    /**
     * Describe: 获取角色新增视图
     * Param ModelAndView
     * Return 角色新增视图
     */
    @GetMapping("add")
    @ApiOperation(value = "获取角色新增视图")
    @PreAuthorize("hasAnyAuthority('sys:role:add')")
    public ModelAndView add() {
        return jumpPage(MODULE_PATH + "add");
    }
    /**
     * Describe: 获取角色修改视图
     * Param ModelAndView
     * Return 角色修改视图
     */
    @GetMapping("edit")
    @ApiOperation(value = "获取角色修改视图")
    @PreAuthorize("hasAnyAuthority('sys:role:edit')")
    public ModelAndView edit(ModelAndView modelAndView, String roleId) {
        modelAndView.addObject("sysRole", systemService.getRoleById(roleId));
        modelAndView.setViewName(MODULE_PATH + "edit");
        return modelAndView;
    }
    /**
     * Describe: 获取角色授权视图
     * Param ModelAndView
     * Return ModelAndView
     */
    @GetMapping("power")
    @ApiOperation(value = "获取分配角色权限视图")
    @PreAuthorize("hasAnyAuthority('sys:role:power')")
    public ModelAndView power(Model model, String roleId) {
        model.addAttribute("roleId", roleId);
        return jumpPage(MODULE_PATH + "power");
    }
}
