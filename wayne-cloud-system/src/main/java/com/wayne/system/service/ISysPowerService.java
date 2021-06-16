package com.wayne.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wayne.system.domain.SysPower;

import java.util.List;

/**
 * Describe: 权限服务接口类
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 * */
public interface ISysPowerService extends IService<SysPower> {

    /**
     * Describe: 根据条件查询权限列表数据
     * Param: SysPower
     * Return: 返回用户列表数据
     * */
    List<SysPower> list(SysPower sysPower);
}
