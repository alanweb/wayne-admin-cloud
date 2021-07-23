package com.wayne.auth.service.impl;

import com.wayne.auth.domain.UserAccount;
import com.wayne.auth.domain.UserDetailsResponse;
import com.wayne.auth.service.SystemService;
import com.wayne.common.plugin.system.domain.SysBaseUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author bin.wei
 * @Date 2021/7/5
 * @Description
 */
@Service
@Slf4j
public class WayneUserDetailsService implements UserDetailsService {
    @Autowired
    private SystemService systemService;

    @Override
    public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
        try {
            return getUserDetails(loginName);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取用户信息
     *
     * @param loginName
     * @return
     */
    public UserDetails getUserDetails(String loginName) {
        UserDetailsResponse response = systemService.getUserAccount(loginName);
        UserDetails acc = response.getData();
        if (null == acc) {
            throw new UsernameNotFoundException(
                    "Account " + loginName + " was not found in the database");
        }
        if (!acc.isEnabled()) {
            log.warn("Account <" + loginName + "> is disable!");
            throw new UsernameNotFoundException("Account <" + loginName + "> is disable!");
        }
        log.info("{} load from portal server {}", loginName, acc);
        return acc;
    }

}
