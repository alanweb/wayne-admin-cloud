package com.wayne.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wayne.common.tools.sequence.SequenceUtil;
import com.wayne.common.web.domain.request.PageDomain;
import com.wayne.system.domain.SysPower;
import com.wayne.system.domain.SysRole;
import com.wayne.system.domain.SysRolePower;
import com.wayne.system.mapper.SysPowerMapper;
import com.wayne.system.mapper.SysRoleMapper;
import com.wayne.system.mapper.SysRolePowerMapper;
import com.wayne.system.mapper.SysUserRoleMapper;
import com.wayne.system.service.ISysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Describe: 角色服务实现类
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    /**
     * 注入权限服务
     */
    @Resource
    private SysPowerMapper sysPowerMapper;

    /**
     * 注入角色权限服务
     */
    @Resource
    private SysRolePowerMapper sysRolePowerMapper;

    /**
     * 引入用户角色服务
     */
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;


    /**
     * Describe: 查询角色数据
     * Param: QueryRoleParam
     * Return: 操作结果
     */
    @Override
    public List<SysRole> list(SysRole param) {
        return baseMapper.selectList(new QueryWrapper<>(param));
    }

    /**
     * Describe: 查询角色数据 分页
     * Param: QueryRoleParam
     * Return: 操作结果
     */
    @Override
    public PageInfo<SysRole> page(SysRole param, PageDomain pageDomain) {
        PageHelper.startPage(pageDomain.getPage(), pageDomain.getLimit());
        List<SysRole> list = list(param);
        return new PageInfo<>(list);
    }

    /**
     * Describe: 查询角色权限信息
     * Param: id
     * Return: 返回角色信息
     */
    @Override
    public List<SysPower> getRolePower(String roleId) {
        List<SysPower> allPower = sysPowerMapper.selectList(null);
        List<SysRolePower> myPower = sysRolePowerMapper.selectByRoleId(roleId);
        allPower.forEach(sysPower -> {
            myPower.forEach(sysRolePower -> {
                if (sysRolePower.getPowerId().equals(sysPower.getPowerId())) sysPower.setCheckArr("1");
            });
        });
        return allPower;
    }

    /**
     * Describe: 保存角色权限数据
     * Param: roleId powerIds
     * Return: 执行结果
     */
    @Override
    @Transactional
    public Boolean saveRolePower(String roleId, List<String> powerIds) {
        sysRolePowerMapper.deleteByRoleId(roleId);
        List<SysRolePower> rolePowers = new ArrayList<>();
        powerIds.forEach(powerId -> {
            SysRolePower sysRolePower = new SysRolePower();
            sysRolePower.setId(SequenceUtil.makeStringId());
            sysRolePower.setRoleId(roleId);
            sysRolePower.setPowerId(powerId);
            rolePowers.add(sysRolePower);
        });
        int result = sysRolePowerMapper.batchInsert(rolePowers);
        return result > 0;
    }

    /**
     * Describe: 根据 id 删除角色数据
     * Param: id
     * Return: Boolean
     */
    @Override
    @Transactional
    public boolean removeById(Serializable id) {
        baseMapper.deleteById(id);
        sysUserRoleMapper.deleteByRoleId(id.toString());
        sysRolePowerMapper.deleteByRoleId(id.toString());
        return true;
    }

    /**
     * Describe: 根据 id 批量删除角色数据
     * Param: ids
     * Return: Boolean
     */
    @Override
    @Transactional
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        baseMapper.deleteBatchIds(idList);
        String[] ids = new String[idList.size()];
        idList.toArray(ids);
        sysUserRoleMapper.deleteByRoleIds(ids);
        sysRolePowerMapper.deleteByRoleIds(ids);
        return true;
    }
}
