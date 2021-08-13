package com.wayne.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wayne.common.tools.secure.SecurityUtil;
import com.wayne.common.tools.sequence.SequenceUtil;
import com.wayne.common.web.domain.request.PageDomain;
import com.wayne.system.domain.SysMail;
import com.wayne.system.domain.SysUser;
import com.wayne.system.mapper.SysMailMapper;
import com.wayne.system.service.ISysMailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysMailServiceImpl extends ServiceImpl<SysMailMapper, SysMail> implements ISysMailService {

    @Resource
    private MailAccount mailAccount;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(SysMail sysMail) {
        if (sendMail(sysMail)) {
            sysMail.setMailId(SequenceUtil.makeStringId());
            sysMail.setCreateBy(SecurityUtil.currentUserObj().toString());
            return baseMapper.insert(sysMail) > 0 ? true : false;
        } else {
            return false;
        }
    }

    @Override
    public List<SysMail> list(SysMail sysMail) {
        return baseMapper.selectList(new QueryWrapper<>(sysMail));
    }

    @Override
    public PageInfo<SysMail> page(SysMail sysMail, PageDomain pageDomain) {
        PageHelper.startPage(pageDomain.getPage(), pageDomain.getLimit());
        List<SysMail> sysMails = list(sysMail);
        return new PageInfo<>(sysMails);
    }

    @Override
    public Boolean sendMail(SysMail sysMail) {
        ArrayList<String> tos = CollectionUtil.newArrayList(StrUtil.split(sysMail.getReceiver(), ";"));
        String send = MailUtil.send(mailAccount, tos, ObjectUtil.isEmpty(sysMail.getSubject()) ? null : sysMail.getSubject(), sysMail.getContent(), false);
        return ObjectUtil.isNotEmpty(send);
    }
}
