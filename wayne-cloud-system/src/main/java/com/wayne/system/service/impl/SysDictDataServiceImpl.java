package com.wayne.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.wayne.common.tools.spring.SpringUtil;
import com.wayne.common.web.domain.request.PageDomain;
import com.wayne.system.domain.SysDictDataData;
import com.wayne.system.mapper.SysDictDataMapper;
import com.wayne.system.service.ISysDictDataService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Describe: 字典值服务实现类
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictDataData> implements ISysDictDataService {

    public static LoadingCache<String, List<SysDictDataData>> loadingCacheSysDictData = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(600, TimeUnit.SECONDS).build(new CacheLoader<String, List<SysDictDataData>>() {
        @Override
        public List<SysDictDataData> load(String typeCode) {
            SysDictDataMapper tempSysDictDataMapper = SpringUtil.getBean("sysDictDataMapper", SysDictDataMapper.class);
            return tempSysDictDataMapper.selectByCode(typeCode);
        }
    });

    @Override
    public List<SysDictDataData> list(SysDictDataData sysDictData) {
        return baseMapper.selectList(new QueryWrapper<>(sysDictData));
    }

    @Override
    public List<SysDictDataData> selectByCode(String typeCode) {
        try {
            return loadingCacheSysDictData.get(typeCode);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }

    @Override
    public void refreshCacheTypeCode(String typeCode) {
        try {
            loadingCacheSysDictData.refresh(typeCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public PageInfo<SysDictDataData> page(SysDictDataData sysDictData, PageDomain pageDomain) {
        PageHelper.startPage(pageDomain.getPage(), pageDomain.getLimit());
        List<SysDictDataData> list = list(sysDictData);
        return new PageInfo<>(list);
    }

    @Override
    public Boolean remove(String id) {
        SysDictDataData sysDictData = baseMapper.selectById(id);
        if (sysDictData != null) {
            baseMapper.deleteById(id);
        }
        refreshCacheTypeCode(sysDictData.getTypeCode());
        return true;
    }
}
