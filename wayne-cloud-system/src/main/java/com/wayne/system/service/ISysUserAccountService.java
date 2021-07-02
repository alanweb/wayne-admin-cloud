package com.wayne.system.service;

import com.wayne.system.dto.UserAccount;

/**
 * @Author bin.wei
 * @Date 2021/6/28
 * @Description
 */
public interface ISysUserAccountService {
    UserAccount getUserAccount(String username);
}
