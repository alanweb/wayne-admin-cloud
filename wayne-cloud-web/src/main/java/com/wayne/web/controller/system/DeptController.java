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
@Api(tags = {"组织部门"})
@RequestMapping("dept")
public class DeptController extends BaseController {

    /**
     * Describe: 基础路径
     */
    private static String MODULE_PATH = "system/dept/" ;

    @Autowired
    private SystemService systemService;

    /**
     * Describe: 获取部门列表视图
     * Return 部门列表视图
     */
    @GetMapping("main")
    public ModelAndView main() {
        return jumpPage(MODULE_PATH + "main");
    }

    /**
     * Describe: 获取部门新增视图
     * Param ModelAndView
     * Return 部门新增视图
     */
    @GetMapping("add")
    public ModelAndView add() {
        return jumpPage(MODULE_PATH + "add");
    }

    /**
     * Describe: 获取部门修改视图
     * Param ModelAndView
     * Return 部门修改视图
     */
    @GetMapping("edit")
    public ModelAndView edit(Model model, String deptId) {
        model.addAttribute("sysDept", systemService.getDeptById(deptId));
        return jumpPage(MODULE_PATH + "edit");
    }

}
