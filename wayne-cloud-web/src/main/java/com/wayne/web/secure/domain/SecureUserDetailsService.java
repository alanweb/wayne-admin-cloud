package com.wayne.web.secure.domain;

import com.wayne.common.plugin.system.domain.SysBaseUser;
import com.wayne.web.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Describe: Security 用户服务
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@Component
public class SecureUserDetailsService implements UserDetailsService {

    @Autowired
    private SystemService systemService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysBaseUser sysUser = systemService.queryByUserName(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("Account Not Found");
        }
        //List<SysPower> powerList = sysPowerMapper.selectByUsername(username);
        //sysUser.setPowerList(powerList);
        return sysUser;
    }
}
