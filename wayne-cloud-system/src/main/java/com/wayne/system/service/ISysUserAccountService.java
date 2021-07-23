package com.wayne.system.service;

import com.wayne.common.plugin.system.domain.SysBaseUser;

/**
 * @Author bin.wei
 * @Date 2021/6/28
 * @Description
 */
public interface ISysUserAccountService {
    SysBaseUser getUserAccount(String username);
}
