package com.wayne.auth.service.impl;

import com.wayne.auth.domain.UserAccount;
import com.wayne.auth.domain.UserAccountResponse;
import com.wayne.auth.service.SystemService;
import com.wayne.common.plugin.system.domain.SysBaseUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
            UserAccount acc = getUserAccount(loginName);
            return createSpringSecurityUser(acc);
        } catch (Exception e) {
            throw e;
        }
    }

    private SysBaseUser createSpringSecurityUser(UserAccount user) {
        List<String> defaultAuthorises = new ArrayList<>();
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            defaultAuthorises =
                    user.getRoles().stream().map(String::toUpperCase).collect(Collectors.toList());
        } else {
            defaultAuthorises.add("USER");
        }
        List<GrantedAuthority> grantedAuthorities =
                defaultAuthorises.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        SysBaseUser userDetail = new SysBaseUser();
        userDetail.setUserId(user.getUserId());
        userDetail.setAuthorities(grantedAuthorities);
        userDetail.setUsername(user.getLoginName());
        userDetail.setPassword(user.getPasswordSha());
        userDetail.setRoleIds(StringUtils.join(user.getRoleIds(), ","));
        userDetail.setEnable(user.isActive() ? SysBaseUser.C_NORMAL : SysBaseUser.C_DISABLE);
        userDetail.setStatus(userDetail.getEnable());
        return userDetail;
    }

    /**
     * 获取用户信息
     *
     * @param loginName
     * @return
     */
    public UserAccount getUserAccount(String loginName) {
        UserAccountResponse response = systemService.getUserAccount(loginName);
        UserAccount acc = response.getData();
        if (null == acc) {
            throw new UsernameNotFoundException(
                    "Account " + loginName + " was not found in the database");
        }
        if (!acc.isActive()) {
            log.warn("Account <" + loginName + "> frozen or deleted!");
            throw new UsernameNotFoundException("Account <" + loginName + "> frozen or deleted!");
        }
        log.info("{} load from portal server {}", loginName, acc);
        return acc;
    }

}
