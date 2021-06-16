package com.wayne.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wayne.common.web.domain.request.PageDomain;
import com.wayne.system.domain.SysNotice;
import com.wayne.system.mapper.SysNoticeMapper;
import com.wayne.system.service.ISysNoticeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * noticeService业务层处理
 *
 * @author jmys
 * @date 2021-03-13
 */
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements ISysNoticeService {

    /**
     * 查询notice列表
     *
     * @param sysNotice notice
     * @return notice
     */
    @Override
    public List<SysNotice> selectSysNoticeList(SysNotice sysNotice) {
        return baseMapper.selectList(new QueryWrapper<>(sysNotice));
    }

    /**
     * 查询notice
     *
     * @param sysNotice  notice
     * @param pageDomain
     * @return notice 分页集合
     */
    @Override
    public PageInfo<SysNotice> selectSysNoticePage(SysNotice sysNotice, PageDomain pageDomain) {
        PageHelper.startPage(pageDomain.getPage(), pageDomain.getLimit());
        List<SysNotice> data = selectSysNoticeList(sysNotice);
        return new PageInfo<>(data);
    }
}
