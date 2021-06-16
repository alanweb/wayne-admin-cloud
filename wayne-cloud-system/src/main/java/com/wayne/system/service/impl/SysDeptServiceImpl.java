package com.wayne.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wayne.common.web.domain.request.PageDomain;
import com.wayne.system.domain.SysDept;
import com.wayne.system.mapper.SysDeptMapper;
import com.wayne.system.mapper.SysUserMapper;
import com.wayne.system.service.ISysDeptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    @Resource
    private SysUserMapper sysUserMapper;

    /**
     * Describe: 查询部门数据
     * Param: QueryRoleParam
     * Return: 操作结果
     */
    @Override
    public List<SysDept> list(SysDept param) {
        return baseMapper.selectList(new QueryWrapper<>(param));
    }

    /**
     * Describe: 查询部门数据 分页
     * Param: QueryRoleParam
     * Return: 操作结果
     */
    @Override
    public PageInfo<SysDept> page(SysDept param, PageDomain pageDomain) {
        PageHelper.startPage(pageDomain.getPage(), pageDomain.getLimit());
        List<SysDept> list = list(param);
        return new PageInfo<>(list);
    }



    /**
     * Describe: 根据 id 删除部门数据
     * Param: id
     * Return: Boolean
     */
    @Override
    @Transactional
    public Boolean remove(String id) {
        baseMapper.deleteById(id);
        sysUserMapper.resetDeptByDeptId(id);
        return true;
    }

    /**
     * Describe: 根据 id 批量删除部门数据
     * Param: ids
     * Return: Boolean
     */
    @Override
    @Transactional
    public boolean batchRemove(String[] ids) {
        baseMapper.deleteBatchIds(Arrays.asList(ids));
        sysUserMapper.resetDeptByDeptIds(ids);
        return true;
    }
}
