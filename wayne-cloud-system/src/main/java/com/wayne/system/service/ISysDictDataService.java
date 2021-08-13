package com.wayne.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.wayne.common.web.domain.request.PageDomain;
import com.wayne.system.domain.SysDictDataData;

import java.util.List;

/**
 * Describe: 字典值服务类
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 * */
public interface ISysDictDataService extends IService<SysDictDataData> {

    /**
     * Describe: 根据条件查询字典类型列表数据
     * Param: SysDictData
     * Return: List<SysDictType>
     * */
    List<SysDictDataData> list(SysDictDataData sysDictData);

    /**
     * 根据字典code获取可用的字典列表数据
     * @param typeCode
     * @return
     */
    List<SysDictDataData> selectByCode(String typeCode);

    /**
     * 刷新字典缓存
     * @param typeCode
     */
    void refreshCacheTypeCode(String typeCode);

    /**
     * Describe: 根据条件查询字典类型列表数据 分页
     * Param: SysDictData
     * Return: PageInfo<SysDictType>
     * */
    PageInfo<SysDictDataData> page(SysDictDataData sysDictData, PageDomain pageDomain);

    /**
     * Describe: 删除 SysDictData 数据
     * Param: SysDictData
     * Return: Boolean
     * */
    Boolean remove(String id);
}
