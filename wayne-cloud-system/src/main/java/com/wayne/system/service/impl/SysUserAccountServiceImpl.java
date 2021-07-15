package com.wayne.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wayne.system.domain.SysRole;
import com.wayne.system.domain.SysUser;
import com.wayne.system.vo.UserAccount;
import com.wayne.system.mapper.SysRoleMapper;
import com.wayne.system.mapper.SysUserMapper;
import com.wayne.system.service.ISysUserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
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
    private SysRoleMapper sysRoleMapper;

    @Override
    public UserAccount getUserAccount(String username) {
        if (!StringUtils.hasText(username)) {
            return null;
        }
        SysUser user = sysUserMapper.selectOne(
                new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername, username));
        // 判断账户是否存在
        if (null != user) {
            // 判断账户状态
            UserAccount userAccount = new UserAccount();
            boolean isActive = SysUser.C_NORMAL.equals(user.getStatus());
            userAccount.setLoginName(user.getUsername());
            userAccount.setPasswordSha(user.getPassword());
            userAccount.setActive(isActive);
            userAccount.setUserId(user.getUserId());
            List<String> roles = new ArrayList<>();
            List<String> roleIds = new ArrayList<>();
            getUserRoles(user.getUsername(), roles, roleIds);
            userAccount.setRoles(roles.isEmpty() ? Collections.emptyList() : roles);
            userAccount.setRoleIds(roleIds.isEmpty() ? Collections.emptyList() : roleIds);
            return userAccount;
        }
        return null;
    }

    private void getUserRoles(String username, List<String> roles, List<String> roleIds) {
        List<SysRole> sysRoles = sysRoleMapper.selectByUsername(username);
        roleIds.addAll(sysRoles.stream().map(role->role.getRoleId()).collect(Collectors.toList()));
        roles.addAll(sysRoles.stream().map(role->role.getRoleCode()).collect(Collectors.toList()));
    }
}
