package com.wayne.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wayne.common.plugin.system.domain.SysBaseUser;
import com.wayne.system.domain.SysPower;
import com.wayne.system.domain.SysUser;
import com.wayne.system.mapper.SysPowerMapper;
import com.wayne.system.mapper.SysUserMapper;
import com.wayne.system.service.ISysUserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author bin.wei
 * @Date 2021/6/28
 * @Description
 */
@Service
public class SysUserAccountServiceImpl implements ISysUserAccountService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysPowerMapper sysPowerMapper;

    @Override
    public SysBaseUser getUserAccount(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        SysUser user = sysUserMapper.selectOne(
                new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername, username));
        // 判断账户是否存在
        if (null != user) {
            // 判断账户状态
            List<String> powerCodeList = new ArrayList<>();
            getUserPowers(username, powerCodeList);
            user.setPowerCodeList(powerCodeList);
            return user;
        }
        return null;
    }

    private void getUserPowers(String username, List<String> powerList) {
        List<SysPower> sysPowers = sysPowerMapper.selectByUsername(username);
        powerList.addAll(sysPowers.stream().map(power -> power.getPowerCode()).filter(power->!StringUtils.isEmpty(power)).distinct().collect(Collectors.toList()));
    }
}
