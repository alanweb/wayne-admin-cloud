package com.wayne.system.controller;

import com.github.pagehelper.PageInfo;
import com.wayne.common.constant.ControllerConstant;
import com.wayne.common.plugin.system.domain.SysBaseDictType;
import com.wayne.common.plugin.system.domain.SysBasePower;
import com.wayne.common.tools.sequence.SequenceUtil;
import com.wayne.common.web.base.BaseController;
import com.wayne.common.web.constants.Enable;
import com.wayne.common.web.domain.request.PageDomain;
import com.wayne.common.web.domain.response.Result;
import com.wayne.common.web.domain.response.module.ResultTable;
import com.wayne.system.domain.SysDictType;
import com.wayne.system.service.ISysDictDataService;
import com.wayne.system.service.ISysDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Describe: 数据字典控制器
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@RestController
@Api(tags = {"数据字典"})
@RequestMapping(ControllerConstant.API_SYSTEM_PREFIX + "dictType")
public class SysDictTypeController extends BaseController {

    @Resource
    private ISysDictTypeService sysDictTypeService;

    /**
     * Describe: 数据字典列表数据
     * Param: sysDictType
     * Return: ResuTable
     */
    @GetMapping("data")
    @PreAuthorize("hasAnyAuthority('sys:dictType:data')")
    public ResultTable data(SysDictType sysDictType, PageDomain pageDomain) {
        PageInfo<SysDictType> pageInfo = sysDictTypeService.page(sysDictType, pageDomain);
        return pageTable(pageInfo.getList(), pageInfo.getTotal());
    }

    @GetMapping("list")
    @PreAuthorize("hasAnyAuthority('sys:dictType:data')")
    public ResultTable list(SysDictType sysDictType, PageDomain pageDomain) {
        List<SysDictType> list = sysDictTypeService.list(sysDictType);
        return dataTable(list);
    }

    /**
     * Describe: 新增字典类型接口
     * Param: sysDictType
     * Return: ResuBean
     */
    @PostMapping("save")
    @PreAuthorize("hasAnyAuthority('sys:dictType:add')")
    public Result save(@RequestBody SysDictType sysDictType) {
        sysDictType.setId(SequenceUtil.makeStringId());
        boolean result = sysDictTypeService.save(sysDictType);
        return decide(result);
    }

    /**
     * Describe: 数据字典类型修改视图
     * Param: sysDictType
     * Return: ModelAndView
     */
    @PutMapping("update")
    @PreAuthorize("hasAnyAuthority('sys:dictType:edit')")
    public Result update(@RequestBody SysDictType sysDictType) {
        boolean result = sysDictTypeService.updateById(sysDictType);
        return decide(result);
    }

    /**
     * Describe: 数据字典删除
     * Param: sysDictType
     * Return: ModelAndView
     */
    @DeleteMapping("remove/{id}")
    @PreAuthorize("hasAnyAuthority('sys:dictType:remove')")
    public Result remove(@PathVariable("id") String id) {
        Boolean result = sysDictTypeService.remove(id);
        return decide(result);
    }

    /**
     * Describe: 根据 Id 启用字典
     * Param dictId
     * Return ResuTree
     */
    @PutMapping("enable")
    public Result enable(@RequestBody SysDictType sysDictType) {
        sysDictType.setEnable(Enable.ENABLE);
        boolean result = sysDictTypeService.updateById(sysDictType);
        return decide(result);
    }

    /**
     * Describe: 根据 Id 禁用字典
     * Param dictId
     * Return ResuTree
     */
    @PutMapping("disable")
    public Result disable(@RequestBody SysDictType sysDictType) {
        sysDictType.setEnable(Enable.DISABLE);
        boolean result = sysDictTypeService.updateById(sysDictType);
        return decide(result);
    }

    /**
     * Describe: 查询字典类型
     * Param: dictTypeId
     * Return: SysBaseDictType
     */
    @GetMapping("/{dictTypeId}")
    @ApiOperation(value = "获取字典类型信息", hidden = true)
    public SysBaseDictType queryByDictTypeId(@PathVariable String dictTypeId) {
        return sysDictTypeService.getById(dictTypeId);
    }

}
