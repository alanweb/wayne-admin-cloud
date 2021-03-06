package com.wayne.system.controller;

import com.wayne.common.constant.ControllerConstant;
import com.wayne.common.plugin.system.domain.SysBasePower;
import com.wayne.common.plugin.system.domain.SysBaseRole;
import com.wayne.common.tools.sequence.SequenceUtil;
import com.wayne.common.web.base.BaseController;
import com.wayne.common.web.constants.Enable;
import com.wayne.common.web.domain.response.Result;
import com.wayne.common.web.domain.response.module.ResultTable;
import com.wayne.common.web.domain.response.module.ResultTree;
import com.wayne.system.domain.SysPower;
import com.wayne.system.service.ISysPowerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Describe: 权 限 控 制 器
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@RestController
@Api(tags = {"系统权限"})
@RequestMapping(ControllerConstant.API_SYSTEM_PREFIX + "power")
public class SysPowerController extends BaseController {

    /**
     * Describe: 权限模块服务
     */
    @Resource
    private ISysPowerService sysPowerService;

    /**
     * Describe: 获取权限列表数据
     * Param ModelAndView
     * Return 权限列表数据
     */
    @GetMapping("data")
    @PreAuthorize("hasAnyAuthority('sys:power:data')")
    public ResultTable data(SysPower sysPower) {
        return treeTable(sysPowerService.list(sysPower));
    }


    /**
     * Describe: 保存权限信息
     * Param: SysPower
     * Return: ResuBean
     */
    @PostMapping("save")
    @PreAuthorize("hasAnyAuthority('sys:power:add')")
    public Result save(@RequestBody SysPower sysPower) {
        if (Strings.isBlank(sysPower.getParentId())) {
            return failure("请选择上级菜单");
        }
        sysPower.setEnable(Enable.ENABLE);
        sysPower.setPowerId(SequenceUtil.makeStringId());
        boolean result = sysPowerService.save(sysPower);
        return decide(result);
    }

    /**
     * Describe: 修改权限信息
     * Param SysPower
     * Return 执行结果
     */
    @PutMapping("update")
    @PreAuthorize("hasAnyAuthority('sys:power:edit')")
    public Result update(@RequestBody SysPower sysPower) {
        if (Strings.isBlank(sysPower.getParentId())) {
            return failure("请选择上级菜单");
        }
        boolean result = sysPowerService.updateById(sysPower);
        return decide(result);
    }

    /**
     * Describe: 根据 id 进行删除
     * Param id
     * Return ResuTree
     */
    @DeleteMapping("remove/{id}")
    @PreAuthorize("hasAnyAuthority('sys:power:remove')")
    public Result remove(@PathVariable String id) {
        boolean result = sysPowerService.removeById(id);
        return decide(result);
    }

    /**
     * Describe: 获取父级权限选择数据
     * Param sysPower
     * Return ResuTree
     */
    @GetMapping("selectParent")
    public ResultTree selectParent(SysPower sysPower) {
        List<SysPower> list = sysPowerService.list(sysPower);
        SysPower basePower = new SysPower();
        basePower.setPowerName("顶级权限");
        basePower.setPowerId("0");
        basePower.setParentId("-1");
        list.add(basePower);
        return dataTree(list);
    }

    /**
     * Describe: 根据 Id 开启用户
     * Param powerId
     * Return ResuTree
     */
    @PutMapping("enable")
    public Result enable(@RequestBody SysPower sysPower) {
        sysPower.setEnable(Enable.ENABLE);
        boolean result = sysPowerService.updateById(sysPower);
        return decide(result);
    }

    /**
     * Describe: 根据 Id 禁用用户
     * Param powerId
     * Return ResuTree
     */
    @PutMapping("disable")
    public Result disable(@RequestBody SysPower sysPower) {
        sysPower.setEnable(Enable.DISABLE);
        boolean result = sysPowerService.updateById(sysPower);
        return decide(result);
    }

    /**
     * Describe: 查询角色信息
     * Param: roleId
     * Return: SysBaseRole
     */
    @GetMapping("/{powerId}")
    @ApiOperation(value = "获取权限信息", hidden = true)
    public SysBasePower queryByRoleId(@PathVariable String powerId) {
        return sysPowerService.getById(powerId);
    }

}
