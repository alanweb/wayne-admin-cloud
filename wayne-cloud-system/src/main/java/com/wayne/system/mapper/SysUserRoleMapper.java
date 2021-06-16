package com.wayne.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wayne.system.domain.SysUserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Describe: 用户角色接口
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 * */
@Repository
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    int batchInsert(List<SysUserRole> sysUserRoles);

    int deleteByUserId(String userId);

    int deleteByUserIds(String[] userIds);

    int deleteByRoleId(String roleId);

    int deleteByRoleIds(String[] roleIds);

    List<SysUserRole> selectByUserId(String userId);
}
