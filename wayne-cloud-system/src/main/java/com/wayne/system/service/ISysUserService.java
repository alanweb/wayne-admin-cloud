package com.wayne.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.wayne.common.web.domain.request.PageDomain;
import com.wayne.system.domain.SysMenu;
import com.wayne.system.domain.SysRole;
import com.wayne.system.domain.SysUser;

import java.util.List;

/**
 * Describe: 用户服务接口类
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 * */
public interface ISysUserService extends IService<SysUser> {

    /**
     * Describe: 根据条件查询用户列表数据
     * Param: username
     * Return: 返回用户列表数据
     * */
    List<SysUser> list(SysUser param);

    /**
     * Describe: 根据条件查询用户列表数据  分页
     * Param: username
     * Return: 返回分页用户列表数据
     * */
    PageInfo<SysUser> page(SysUser param, PageDomain pageDomain);

    /**
     * Describe: 保存用户角色数据
     * Param: SysUser
     * Return: 操作结果
     * */
    boolean saveUserRole(String userId, List<String> roleIds);

    /**
     * Describe: 获取用户角色数据
     * Param: SysUser
     * Return: 操作结果
     * */
    List<SysRole> getUserRole(String userId);

    /**
     * Describe: 获取用户菜单数据
     * Param: SysUser
     * Return: 操作结果
     * */
    List<SysMenu> getUserMenu(String username);

    /**
     * Describe: 递归获取菜单tree
     * Param: sysMenus
     * Return: 操作结果
     * */
    List<SysMenu> toUserMenu(List<SysMenu> sysMenus, String parentId);

}

