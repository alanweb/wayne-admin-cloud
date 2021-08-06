package com.wayne.system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wayne.common.constant.ControllerConstant;
import com.wayne.common.plugin.logging.aop.enums.LoggingType;
import com.wayne.common.plugin.system.domain.SysBaseLog;
import com.wayne.common.web.base.BaseController;
import com.wayne.common.web.domain.request.PageDomain;
import com.wayne.common.web.domain.response.Result;
import com.wayne.common.web.domain.response.module.ResultTable;
import com.wayne.system.domain.SysLog;
import com.wayne.system.service.ISysLogService;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Describe: 日志控制器
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@RestController
@Api(tags = {"系统日志"})
@RequestMapping(ControllerConstant.API_SYSTEM_PREFIX + "log")
public class SysLogController extends BaseController {

    @Resource
    private ISysLogService sysLogService;
    /**
     * 系 统 日 志
     */
    private String MODULE_PATH = "system/log";

    /**
     * Describe: 行为日志视图
     * Param: null
     * Return: ModelAndView
     */
    @GetMapping("main")
    @PreAuthorize("hasAnyAuthority('sys:log:main')")
    public ModelAndView main() {
        return jumpPage(MODULE_PATH + "/main");
    }

    /**
     * Describe: 操作日志数据
     * Param: null
     * Return: ResultTable
     */
    @GetMapping("operateLog")
    @PreAuthorize("hasAnyAuthority('sys:log:operateLog')")
    public ResultTable operateLog(PageDomain pageDomain, LocalDateTime startTime, LocalDateTime endTime) {
        PageHelper.startPage(pageDomain.getPage(), pageDomain.getLimit());
        PageInfo<SysLog> pageInfo = new PageInfo<>(sysLogService.data(LoggingType.OPERATE, startTime, endTime));
        return pageTable(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * Describe: 登录日志数据
     * Param: null
     * Return: ModelAndView
     */
    @GetMapping("loginLog")
    @PreAuthorize("hasAnyAuthority('sys:log:loginLog')")
    public ResultTable loginLog(PageDomain pageDomain, LocalDateTime startTime, LocalDateTime endTime) {
        PageHelper.startPage(pageDomain.getPage(), pageDomain.getLimit());
        PageInfo<SysLog> pageInfo = new PageInfo<>(sysLogService.data(LoggingType.LOGIN, startTime, endTime));
        return pageTable(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * Describe: 日志详情
     * Param: null
     * Return: ModelAndView
     */
    @GetMapping("/info")
    @PreAuthorize("hasAnyAuthority('sys:log:info')")
    public ModelAndView details() {
        return jumpPage(MODULE_PATH + "/info");
    }

    /**
     * Describe: 记录日志
     * Param: null
     * Return: ModelAndView
     */
    @PostMapping("/save")
    public Result save(@RequestBody SysLog log) {
        boolean flag = sysLogService.save(log);
        return flag ? Result.success() : Result.failure();
    }

    @GetMapping("/selectTopLoginLog")
    public List<SysBaseLog> selectTopLoginLog(String username) {
        return sysLogService.selectTopLoginLog(username).stream().collect(Collectors.toList());
    }
}
