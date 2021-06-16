package com.wayne.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wayne.common.web.domain.request.PageDomain;
import com.wayne.system.domain.SysDictType;
import com.wayne.system.mapper.SysDictDataMapper;
import com.wayne.system.mapper.SysDictTypeMapper;
import com.wayne.system.service.ISysDictDataService;
import com.wayne.system.service.ISysDictTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Describe: 字典类型服务实现类
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 * */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper,SysDictType> implements ISysDictTypeService {

    @Resource
    private ISysDictDataService iSysDictDataService;

    @Resource
    private SysDictDataMapper sysDictDataMapper;

    /**
     * Describe: 根据条件查询用户列表数据
     * Param: SysDictType
     * Return: List<SysDictType>
     * */
    @Override
    public List<SysDictType> list(SysDictType sysDictType) {
        return baseMapper.selectList(new QueryWrapper<>(sysDictType));
    }

    /**
     * Describe: 根据条件查询用户列表数据 分页
     * Param: SysDictType
     * Return: PageInfo<SysDictType>
     * */
    @Override
    public PageInfo<SysDictType> page(SysDictType sysDictType, PageDomain pageDomain) {
        PageHelper.startPage(pageDomain.getPage(),pageDomain.getLimit());
        List<SysDictType> list = list(sysDictType);
        return new PageInfo<>(list);
    }

    /**
     * Describe: 根据 ID 删除字典类型
     * Param: id
     * Return: Boolean
     * */
    @Override
    public Boolean remove(String id) {
        SysDictType sysDictType =  baseMapper.selectById(id);
        if(sysDictType!=null) {
             baseMapper.deleteById(id);
             sysDictDataMapper.deleteByCode(sysDictType.getTypeCode());
        }
        iSysDictDataService.refreshCacheTypeCode(sysDictType.getTypeCode());
        return true;
    }
}
