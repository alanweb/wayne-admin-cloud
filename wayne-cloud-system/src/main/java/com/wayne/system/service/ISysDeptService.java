package com.wayne.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.wayne.common.web.domain.request.PageDomain;
import com.wayne.system.domain.SysDept;

import java.util.List;

/**
 * Describe: 部门服务接口类
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 * */
public interface ISysDeptService extends IService<SysDept> {

    /**
     * Describe: 查询部门数据
     * Param: queryRoleParam
     * Return: 操作结果
     * */
    List<SysDept> list(SysDept param);

    /**
     * Describe: 分页查询部门数据
     * Param: queryRoleParam
     * Param: pageDomain
     * Return: 操作结果
     * */
    PageInfo<SysDept> page(SysDept param, PageDomain pageDomain);

    /**
     * Describe: 根据 id 删除部门数据
     * Param: id
     * Return: 操作结果
     * */
    Boolean remove(String id);

    /**
     * Describe: 根据 id 删除部门数据
     * Param: ids
     * Return: 操作结果
     * */
    boolean batchRemove(String[] ids);
}
