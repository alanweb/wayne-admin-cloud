package com.wayne.system.controller;

import com.github.pagehelper.PageInfo;
import com.wayne.common.constant.ControllerConstant;
import com.wayne.common.plugin.system.domain.SysBaseRole;
import com.wayne.common.tools.sequence.SequenceUtil;
import com.wayne.common.tools.string.Convert;
import com.wayne.common.web.base.BaseController;
import com.wayne.common.web.constants.Enable;
import com.wayne.common.web.domain.request.PageDomain;
import com.wayne.common.web.domain.response.Result;
import com.wayne.common.web.domain.response.module.ResultTable;
import com.wayne.common.web.domain.response.module.ResultTree;
import com.wayne.system.domain.SysRole;
import com.wayne.system.service.ISysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Describe: 角 色 控 制 器
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@RestController
@Api(tags = {"系统角色"})
@RequestMapping(ControllerConstant.API_SYSTEM_PREFIX + "role")
public class SysRoleController extends BaseController {

    /**
     * Describe: 角色模块服务
     */
    @Resource
    private ISysRoleService sysRoleService;

    /**
     * Describe: 获取角色列表数据
     * Param SysRole PageDomain
     * Return 角色列表数据
     */
    @GetMapping("data")
    @ApiOperation(value = "获取角色列表数据")
    @PreAuthorize("hasAnyAuthority('sys:role:data')")
    public ResultTable data(PageDomain pageDomain, SysRole param) {
        PageInfo<SysRole> pageInfo = sysRoleService.page(param, pageDomain);
        return pageTable(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * Describe: 保存角色信息
     * Param SysRole
     * Return 执行结果
     */
    @PostMapping("save")
    @ApiOperation(value = "保存角色数据")
    @PreAuthorize("hasAnyAuthority('sys:role:add')")
    public Result save(@RequestBody SysRole sysRole) {
        sysRole.setRoleId(SequenceUtil.makeStringId());
        boolean result = sysRoleService.save(sysRole);
        return decide(result);
    }

    /**
     * Describe: 修改角色信息
     * Param SysRole
     * Return 执行结果
     */
    @PutMapping("update")
    @ApiOperation(value = "修改角色数据")
    @PreAuthorize("hasAnyAuthority('sys:role:edit')")
    public Result update(@RequestBody SysRole sysRole) {
        boolean result = sysRoleService.updateById(sysRole);
        return decide(result);
    }

    /**
     * Describe: 保存角色权限
     * Param RoleId PowerIds
     * Return ResuBean
     */
    @PutMapping("saveRolePower")
    @ApiOperation(value = "保存角色权限数据")
    @PreAuthorize("hasAnyAuthority('sys:role:power')")
    public Result saveRolePower(String roleId, String powerIds) {
        boolean result = sysRoleService.saveRolePower(roleId, Arrays.asList(powerIds.split(",")));
        return decide(result);
    }

    /**
     * Describe: 获取角色权限
     * Param RoleId
     * Return ResuTree
     */
    @GetMapping("getRolePower")
    @ApiOperation(value = "获取角色权限数据")
    @PreAuthorize("hasAnyAuthority('sys:role:power')")
    public ResultTree getRolePower(String roleId) {
        return dataTree(sysRoleService.getRolePower(roleId));
    }

    /**
     * Describe: 用户删除接口
     * Param: id
     * Return: ResuBean
     */
    @DeleteMapping("remove/{id}")
    @ApiOperation(value = "删除角色数据")
    @PreAuthorize("hasAnyAuthority('sys:role:remove')")
    public Result remove(@PathVariable String id) {
        boolean result = sysRoleService.removeById(id);
        return decide(result);
    }

    /**
     * Describe: 用户批量删除接口
     * Param: ids
     * Return: ResuBean
     */
    @DeleteMapping("batchRemove/{ids}")
    @ApiOperation(value = "批量删除角色数据")
    @PreAuthorize("hasAnyAuthority('sys:role:remove')")
    public Result batchRemove(@PathVariable String ids) {
        boolean result = sysRoleService.removeByIds(Arrays.asList(Convert.toStrArray(ids)));
        return decide(result);
    }

    /**
     * Describe: 根据 Id 启用角色
     * Param: roleId
     * Return: ResuBean
     */
    @PutMapping("enable")
    @ApiOperation(value = "启用角色")
    public Result enable(@RequestBody SysRole sysRole) {
        sysRole.setEnable(Enable.ENABLE);
        boolean result = sysRoleService.updateById(sysRole);
        return decide(result);
    }

    /**
     * Describe: 根据 Id 禁用角色
     * Param: roleId
     * Return: ResuBean
     */
    @PutMapping("disable")
    @ApiOperation(value = "禁用角色")
    public Result disable(@RequestBody SysRole sysRole) {
        sysRole.setEnable(Enable.DISABLE);
        boolean result = sysRoleService.updateById(sysRole);
        return decide(result);
    }

    /**
     * Describe: 查询全部角色
     * Param: roleId
     * Return: List<SysBaseRole>
     */
    @GetMapping("/{roleId}")
    @ApiOperation(value = "获取角色信息", hidden = true)
    public SysBaseRole queryByRoleId(@PathVariable String roleId) {
        return sysRoleService.getById(roleId);
    }

    /**
     * Describe: 查询全部角色
     * Param: roleId
     * Return: List<SysBaseRole>
     */
    @GetMapping("all")
    @ApiOperation(value = "全部角色", hidden = true)
    public List<SysBaseRole> all() {
        return sysRoleService.list().stream().collect(Collectors.toList());
    }
}
