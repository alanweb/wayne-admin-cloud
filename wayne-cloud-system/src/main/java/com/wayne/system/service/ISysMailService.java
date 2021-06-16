package com.wayne.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.wayne.common.web.domain.request.PageDomain;
import com.wayne.system.domain.SysMail;

import java.util.List;

/**
 * Describe: 邮件服务接口类
 * Author: Heiky
 * CreateTime: 2021/1/13 15:21
 */
public interface ISysMailService extends IService<SysMail> {

    /**
     * Describe: 根据条件查询邮件列表数据
     * Param: sysMail
     * Return: 返回邮件列表数据
     */
    List<SysMail> list(SysMail sysMail);

    /**
     * Describe: 根据条件查询邮件列表数据  分页
     * Param: sysMail
     * Return: 返回分页邮件列表数据
     */
    PageInfo<SysMail> page(SysMail sysMail, PageDomain pageDomain);

    /**
     * Describe: 发送邮件
     * Param: sysMail
     * Return: 操作结果
     */
    Boolean sendMail(SysMail sysMail);
}
