package com.wayne.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wayne.common.tools.sequence.SequenceUtil;
import com.wayne.common.web.domain.request.PageDomain;
import com.wayne.common.config.proprety.AuthProperty;
import com.wayne.system.domain.SysMenu;
import com.wayne.system.domain.SysRole;
import com.wayne.system.domain.SysUser;
import com.wayne.system.domain.SysUserRole;
import com.wayne.system.mapper.SysPowerMapper;
import com.wayne.system.mapper.SysRoleMapper;
import com.wayne.system.mapper.SysUserMapper;
import com.wayne.system.mapper.SysUserRoleMapper;
import com.wayne.system.service.ISysUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Describe: 用户服务实现类
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    /**
     * 注入用户角色服务
     */
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * 注入角色服务
     */
    @Resource
    private SysRoleMapper sysRoleMapper;

    /**
     * 注入权限服务
     */
    @Resource
    private SysPowerMapper sysPowerMapper;

    /**
     * 超级管理员配置
     */
    //@Resource
    private AuthProperty securityProperty;

    @Override
    public SysUser selectByUsername(String username) {
        return baseMapper.selectByUsername(username);
    }

    /**
     * Describe: 根据条件查询用户列表数据
     * Param: username
     * Return: 返回用户列表数据
     */
    @Override
    public List<SysUser> list(SysUser param) {
        List<SysUser> sysUsers = baseMapper.selectList(new QueryWrapper<>(param));
        return sysUsers;
    }

    /**
     * Describe: 根据条件查询用户列表数据  分页
     * Param: username
     * Return: 返回分页用户列表数据
     */
    @Override
    public PageInfo<SysUser> page(SysUser param, PageDomain pageDomain) {
        PageHelper.startPage(pageDomain.getPage(), pageDomain.getLimit());
        List<SysUser> sysUsers = list(param);
        return new PageInfo<>(sysUsers);
    }

    /**
     * Describe: 根据 id 删除用户数据
     * Param: id
     * Return: Boolean
     */
    @Override
    @Transactional
    public boolean removeById(Serializable id) {
        sysUserRoleMapper.deleteByUserId(id.toString());
        baseMapper.deleteById(id);
        return true;
    }

    /**
     * Describe: 根据 id 批量删除用户数据
     * Param: ids
     * Return: Boolean
     */
    @Override
    @Transactional
    public boolean removeByIds(Collection<? extends Serializable> ids) {
        baseMapper.deleteBatchIds(ids);
        String[] idArr = new String[ids.size()];
        ids.toArray(idArr);
        sysUserRoleMapper.deleteByUserIds(idArr);
        return false;
    }


    /**
     * Describe: 保存用户角色数据
     * Param: SysUser
     * Return: 操作结果
     */
    @Override
    public boolean saveUserRole(String userId, List<String> roleIds) {
        sysUserRoleMapper.deleteByUserId(userId);
        List<SysUserRole> sysUserRoles = new ArrayList<>();
        roleIds.forEach(roleId -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setId(SequenceUtil.makeStringId());
            sysUserRole.setRoleId(roleId);
            sysUserRole.setUserId(userId);
            sysUserRoles.add(sysUserRole);
        });
        int i = sysUserRoleMapper.batchInsert(sysUserRoles);
        return i > 0;
    }

    /**
     * Describe: 获取
     * Param: SysUser
     * Return: 操作结果
     */
    @Override
    public List<SysRole> getUserRole(String userId) {
        List<SysRole> allRole = sysRoleMapper.selectList(null);
        List<SysUserRole> myRole = sysUserRoleMapper.selectByUserId(userId);
        allRole.forEach(sysRole -> {
            myRole.forEach(sysUserRole -> {
                if (sysRole.getRoleId().equals(sysUserRole.getRoleId())) sysRole.setChecked(true);
            });
        });
        return allRole;
    }

    /**
     * Describe: 获取用户菜单
     * Param: username
     * Return: Result
     */
    @Override
    public List<SysMenu> getUserMenu(String username) {
       // String name = !(securityProperty.isSuperAuthOpen() && username.equals(securityProperty.getSuperAdmin())) ? username : "";
        String name ="";
        return sysPowerMapper.selectMenuByUsername(name);
    }

    /**
     * Describe: 递归获取菜单tree
     * Param: sysMenus
     * Return: 操作结果
     */
    @Override
    public List<SysMenu> toUserMenu(List<SysMenu> sysMenus, String parentId) {
        List<SysMenu> list = new ArrayList<>();
        for (SysMenu menu : sysMenus) {
            if (parentId.equals(menu.getParentId())) {
                menu.setChildren(toUserMenu(sysMenus, menu.getId()));
                list.add(menu);
            }
        }
        return list;
    }

}
