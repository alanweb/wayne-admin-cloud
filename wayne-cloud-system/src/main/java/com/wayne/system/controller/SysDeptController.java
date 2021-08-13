package com.wayne.system.controller;

import com.wayne.common.constant.ControllerConstant;
import com.wayne.common.plugin.system.domain.SysBaseDept;
import com.wayne.common.plugin.system.domain.SysBasePower;
import com.wayne.common.tools.sequence.SequenceUtil;
import com.wayne.common.web.base.BaseController;
import com.wayne.common.web.constants.Enable;
import com.wayne.common.web.domain.response.Result;
import com.wayne.common.web.domain.response.module.ResultTable;
import com.wayne.common.web.domain.response.module.ResultTree;
import com.wayne.system.domain.SysDept;
import com.wayne.system.service.ISysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Describe: 部门管理
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@RestController
@Api(tags = {"组织部门"})
@RequestMapping(ControllerConstant.API_SYSTEM_PREFIX + "dept")
public class SysDeptController extends BaseController {

    /**
     * Describe: 部门模块服务
     */
    @Resource
    private ISysDeptService sysDeptService;

    /**
     * Describe: 获取部门列表数据
     * Param SysDept PageDomain
     * Return 部门列表数据
     */
    @GetMapping("data")
    public ResultTable data(SysDept param) {
        List<SysDept> data = sysDeptService.list(param);
        return dataTable(data);
    }

    /**
     * Describe: 获取部门树状数据结构
     * Param ModelAndView
     * Return ModelAndView
     */
    @GetMapping("tree")
    public ResultTree tree(SysDept param) {
        List<SysDept> data = sysDeptService.list(param);
        return dataTree(data);
    }

    /**
     * Describe: 保存部门信息
     * Param SysDept
     * Return 执行结果
     */
    @PostMapping("save")
    @ApiOperation(value = "保存部门数据")
    public Result save(@RequestBody SysDept SysDept) {
        SysDept.setDeptId(SequenceUtil.makeStringId());
        boolean result = sysDeptService.save(SysDept);
        return decide(result);
    }

    /**
     * Describe: 修改部门信息
     * Param SysDept
     * Return 执行结果
     */
    @PutMapping("update")
    public Result update(@RequestBody SysDept SysDept) {
        boolean result = sysDeptService.updateById(SysDept);
        return decide(result);
    }

    /**
     * Describe: 部门删除接口
     * Param: id
     * Return: Result
     */
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable String id) {
        boolean result = sysDeptService.remove(id);
        return decide(result);
    }

    /**
     * Describe: 部门批量删除接口
     * Param: ids
     * Return: Result
     */
    @DeleteMapping("batchRemove/{ids}")
    public Result batchRemove(@PathVariable String ids) {
        boolean result = sysDeptService.batchRemove(ids.split(","));
        return decide(result);
    }

    /**
     * Describe: 根据 Id 启用部门
     * Param: roleId
     * Return: Result
     */
    @PutMapping("enable")
    public Result enable(@RequestBody SysDept SysDept) {
        SysDept.setStatus(Enable.ENABLE);
        boolean result = sysDeptService.updateById(SysDept);
        return decide(result);
    }

    /**
     * Describe: 根据 Id 禁用部门
     * Param: roleId
     * Return: Result
     */
    @PutMapping("disable")
    public Result disable(@RequestBody SysDept SysDept) {
        SysDept.setStatus(Enable.DISABLE);
        boolean result = sysDeptService.updateById(SysDept);
        return decide(result);
    }
    /**
     * Describe: 查询部门信息
     * Param: deptId
     * Return: SysBaseDept
     */
    @GetMapping("/{deptId}")
    @ApiOperation(value = "获取部门信息", hidden = true)
    public SysBaseDept queryByDeptId(@PathVariable String deptId) {
        return sysDeptService.getById(deptId);
    }

}
