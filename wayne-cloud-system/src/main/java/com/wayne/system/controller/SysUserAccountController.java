package com.wayne.system.controller;

import com.wayne.common.constant.ControllerConstant;
import com.wayne.common.web.domain.response.Result;
import com.wayne.system.dto.UserAccount;
import com.wayne.system.service.ISysUserAccountService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Describe: 账 号 控 制 器
 * Author: bin.wei
 * CreateTime: 2019/10/23
 */
@RestController
@Api(tags = {"账号信息"})
@RequestMapping(ControllerConstant.API_SYSTEM_PREFIX + "account")
public class SysUserAccountController {
    @Autowired
    private ISysUserAccountService userAccountService;

    @GetMapping("info")
    public Result<UserAccount> account(String username) {
        UserAccount account = userAccountService.getUserAccount(username);
        if (null != account) {
            return Result.success(account);
        }
        return Result.failure();
    }
}
