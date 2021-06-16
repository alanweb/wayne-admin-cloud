package com.wayne.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wayne.common.plugin.logging.aop.enums.LoggingType;
import com.wayne.common.plugin.logging.aop.enums.RequestMethod;
import com.wayne.common.tools.secure.SecurityUtil;
import com.wayne.common.tools.servlet.ServletUtil;
import com.wayne.system.domain.SysLog;
import com.wayne.system.mapper.SysLogMapper;
import com.wayne.system.service.ISysLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Describe: 日 志 服 务 接 口 实 现
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 * */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper,SysLog> implements ISysLogService {


    @Override
    public boolean save(SysLog sysLog) {
        sysLog.setOperateAddress(ServletUtil.getRemoteHost());
        sysLog.setMethod(ServletUtil.getRequestURI());
        sysLog.setCreateTime(LocalDateTime.now());
        sysLog.setRequestMethod(RequestMethod.valueOf(ServletUtil.getMethod()));
        sysLog.setOperateUrl(ServletUtil.getRequestURI());
        sysLog.setBrowser(ServletUtil.getBrowser());
        sysLog.setRequestBody(ServletUtil.getQueryParam());
        sysLog.setSystemOs(ServletUtil.getSystem());
        sysLog.setOperateName(null != SecurityUtil.currentUser() ? SecurityUtil.currentUser().getName() : "未登录用户");
        int result = baseMapper.insert(sysLog);
        return result > 0;
    }

    @Override
    public List<SysLog> data(LoggingType loggingType,LocalDateTime startTime,LocalDateTime endTime) {
        QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("logging_Type",loggingType);
        queryWrapper.between("create_time",startTime,endTime);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysLog> selectTopLoginLog(String operateName) {
        return baseMapper.selectTopLoginLog(operateName);
    }

}
