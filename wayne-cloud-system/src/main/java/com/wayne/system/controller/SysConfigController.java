package com.wayne.system.controller;

import com.github.pagehelper.PageInfo;
import com.wayne.common.constant.ControllerConstant;
import com.wayne.common.plugin.system.domain.SysBaseConfig;
import com.wayne.common.plugin.system.domain.SysBaseRole;
import com.wayne.common.tools.sequence.SequenceUtil;
import com.wayne.common.web.base.BaseController;
import com.wayne.common.web.domain.request.PageDomain;
import com.wayne.common.web.domain.response.Result;
import com.wayne.common.web.domain.response.module.ResultTable;
import com.wayne.system.domain.SysConfig;
import com.wayne.system.service.ISysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * Describe: 系 统 配 置 控 制 器
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@RestController
@Api(tags = {"全局配置"})
@RequestMapping(ControllerConstant.API_SYSTEM_PREFIX + "config")
public class SysConfigController extends BaseController {

    /**
     * 引入服务
     */
    @Resource
    private ISysConfigService sysConfigService;

    /**
     * Describe: 数据字典列表数据
     * Param: sysConfig
     * Return: ResultTable
     */
    @GetMapping("data")
    public ResultTable data(SysConfig param, PageDomain pageDomain) {
        PageInfo<SysConfig> pageInfo = sysConfigService.page(param, pageDomain);
        return pageTable(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * Describe: 新增字典类型接口
     * Param: sysConfig
     * Return: ResultBean
     */
    @PostMapping("save")
    public Result save(@RequestBody SysConfig sysConfig) {
        sysConfig.setConfigId(SequenceUtil.makeStringId());
        sysConfig.setCreateTime(LocalDateTime.now());
        sysConfig.setConfigType("custom");
        boolean result = sysConfigService.save(sysConfig);
        return decide(result);
    }

    /**
     * Describe: 数据字典类型修改视图
     * Param: sysConfig
     * Return: ModelAndView
     */
    @PutMapping("update")
    public Result update(@RequestBody SysConfig sysConfig) {
        sysConfig.setUpdateTime(LocalDateTime.now());
        boolean result = sysConfigService.updateById(sysConfig);
        return decide(result);
    }

    /**
     * Describe: 数据字典删除
     * Param: sysConfig
     * Return: ModelAndView
     */
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable("id") String id) {
        Boolean result = sysConfigService.remove(id);
        return decide(result);
    }

    /**
     * Describe: 系统配置批量删除接口
     * Param: ids
     * Return: Result
     */
    @DeleteMapping("batchRemove/{ids}")
    @ApiOperation(value = "批量删除系统配置数据")
    public Result batchRemove(@PathVariable String ids) {
        boolean result = sysConfigService.batchRemove(ids.split(","));
        return decide(result);
    }

    /**
     * Describe: 查询配置信息
     * Param: configId
     * Return: SysBaseConfig
     */
    @GetMapping("/{configId}")
    @ApiOperation(value = "获取配置信息", hidden = true)
    public SysBaseConfig queryByConfigId(@PathVariable String configId) {
        return sysConfigService.getById(configId);
    }

    /**
     * Describe: 查询配置信息
     * Param: code
     * Return: SysBaseConfig
     */
    @GetMapping("/queryByCode")
    @ApiOperation(value = "获取配置信息", hidden = true)
    public SysBaseConfig queryByCode(String code) {
        return sysConfigService.getByCode(code);
    }

}
