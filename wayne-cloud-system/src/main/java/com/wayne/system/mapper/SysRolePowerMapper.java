package com.wayne.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wayne.system.domain.SysRolePower;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Describe: 角色权限接口
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 * */
@Repository
public interface SysRolePowerMapper extends BaseMapper<SysRolePower> {

    List<SysRolePower> selectByRoleId(String roleId);

    int batchInsert(List<SysRolePower> sysRolePowers);

    int deleteByRoleId(String roleId);

    int deleteByRoleIds(String[] roleIds);

    int deleteByPowerId(String powerId);

    int deleteByPowerIds(String[] powerIds);
}
