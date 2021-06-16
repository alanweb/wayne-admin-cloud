package com.wayne.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wayne.system.domain.SysConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Describe: 系统配置接口
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 * */
@Repository
public interface SysConfigMapper extends BaseMapper<SysConfig> {

    /**
     * Describe: 根据 Code 查询系统配置
     * Param: code
     * Return: SysConfig
     * */
    SysConfig selectByCode(@Param("code") String code);
}
