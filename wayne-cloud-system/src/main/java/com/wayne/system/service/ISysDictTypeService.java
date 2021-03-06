package com.wayne.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.wayne.common.web.domain.request.PageDomain;
import com.wayne.system.domain.SysDictType;

import java.util.List;

/**
 * Describe: 数据字典类型服务类
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 * */
public interface ISysDictTypeService extends IService<SysDictType> {

    /**
     * Describe: 根据条件查询字典类型列表数据
     * Param: SysDictType
     * Return: List<SysDictType>
     * */
    List<SysDictType> list(SysDictType sysDictType);

    /**
     * Describe: 根据条件查询字典类型列表数据 分页
     * Param: SysDictType
     * Return: PageInfo<SysDictType>
     * */
    PageInfo<SysDictType> page(SysDictType sysDictType, PageDomain pageDomain);
    /**
     * Describe: 删除 SysDictType 数据
     * Param: SysDictType
     * Return: Boolean
     * */
    Boolean remove(String id);
}
