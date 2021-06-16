package com.wayne.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wayne.system.domain.SysUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Describe: 用户接口层
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 * */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * Describe: 根据 username 查询用户
     * Param: username
     * Return: SysUser
     * */
    SysUser selectByUsername(@Param("username") String username);

    /**
     * Describe: 重置部门
     * Param: deptId
     * Return: Integer
     * */
    Integer resetDeptByDeptId(String deptId);

    /**
     * Describe: 批量重置部门
     * Param: deptIds
     * Return: Integer
     * */
    Integer resetDeptByDeptIds(String[] deptIds);
}
