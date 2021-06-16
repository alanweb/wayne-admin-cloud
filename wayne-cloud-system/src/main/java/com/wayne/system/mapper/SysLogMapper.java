package com.wayne.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wayne.system.domain.SysLog;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Describe: 日 志 接 口
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@Repository
public interface SysLogMapper extends BaseMapper<SysLog> {

    /**
     * Describe: 根据 operateName 查询日志
     * Param: operateName
     * Return 日志列表
     */
    List<SysLog> selectTopLoginLog(String operateName);
}
