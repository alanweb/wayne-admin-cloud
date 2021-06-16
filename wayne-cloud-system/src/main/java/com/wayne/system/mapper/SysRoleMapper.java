package com.wayne.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wayne.system.domain.SysRole;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Describe: 系统角色接口
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 * */
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * Describe: 根据 username 查询用户权限
     * Param: username
     * Return: SysPower
     * */
    List<SysRole> selectByUsername(String username);
}
