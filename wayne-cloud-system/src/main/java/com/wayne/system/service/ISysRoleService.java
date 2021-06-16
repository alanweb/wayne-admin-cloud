package com.wayne.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.wayne.common.web.domain.request.PageDomain;
import com.wayne.system.domain.SysPower;
import com.wayne.system.domain.SysRole;

import java.util.List;

/**
 * Describe: 角色服务接口类
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * Describe: 查询角色数据
     * Param: queryRoleParam
     * Return: 操作结果
     */
    List<SysRole> list(SysRole queryRoleParam);

    /**
     * Describe: 分页查询角色数据
     * Param: queryRoleParam
     * Param: pageDomain
     * Return: 操作结果
     */
    PageInfo<SysRole> page(SysRole param, PageDomain pageDomain);


    /**
     * Describe: 获取角色权限
     * Param: roleId
     * Return: 操作结果
     */
    List<SysPower> getRolePower(String roleId);

    /**
     * Describe: 保存角色权限
     * Param: roleId , powerIds
     * Return: 操作结果
     */
    Boolean saveRolePower(String roleId, List<String> powerIds);

}
