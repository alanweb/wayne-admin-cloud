package com.wayne.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wayne.system.domain.SysPower;
import com.wayne.system.mapper.SysPowerMapper;
import com.wayne.system.mapper.SysRolePowerMapper;
import com.wayne.system.service.ISysPowerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;


/**
 * Describe: 权限服务实现类
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@Service
public class SysPowerServiceImpl extends ServiceImpl<SysPowerMapper, SysPower> implements ISysPowerService {

    /**
     * 引入角色权限服务
     */
    @Resource
    private SysRolePowerMapper sysRolePowerMapper;

    /**
     * Describe: 查询权限列表
     * Param: SysPower
     * Return: 执行结果
     */
    @Override
    public List<SysPower> list(SysPower sysPower) {
        return baseMapper.selectList(new QueryWrapper<>(sysPower));
    }

    /**
     * Describe: 根据 ID 删除权限信息
     * Param: id
     * Return: 执行结果
     */
    @Override
    @Transactional
    public boolean removeById(Serializable id) {
        int powerResult = baseMapper.deleteById(id);
        sysRolePowerMapper.deleteByPowerId(id.toString());
        if (powerResult > 0) {
            return true;
        } else {
            return false;
        }
    }
}
