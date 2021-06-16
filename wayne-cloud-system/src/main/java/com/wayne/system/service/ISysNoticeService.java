package com.wayne.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.wayne.common.web.domain.request.PageDomain;
import com.wayne.system.domain.SysNotice;

import java.util.List;

/**
 * noticeService接口
 *
 * @author jmys
 * @date 2021-03-13
 */
public interface ISysNoticeService extends IService<SysNotice> {

    /**
     * 查询notice
     *
     * @param sysNotice
     * @param pageDomain
     * @return notice 分页集合
     */
    PageInfo<SysNotice> selectSysNoticePage(SysNotice sysNotice, PageDomain pageDomain);

    /**
     * 查询notice列表
     *
     * @param sysNotice notice
     * @return notice集合
     */
    List<SysNotice> selectSysNoticeList(SysNotice sysNotice);
}
