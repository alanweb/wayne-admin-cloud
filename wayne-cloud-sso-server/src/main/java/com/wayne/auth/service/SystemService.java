package com.wayne.auth.service;

import com.wayne.auth.domain.ClientDetailsResponse;
import com.wayne.auth.domain.UserDetailsResponse;
import com.wayne.common.plugin.system.domain.SysBaseLog;
import com.wayne.common.plugin.system.domain.SysBaseUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("wayne-system")
public interface SystemService {
    @PostMapping("/system/log/save")
    String saveLog(@RequestBody SysBaseLog log);
    @PutMapping("/system/user/update")
    String updateUser(@RequestBody SysBaseUser user);
    @GetMapping("/system/user/queryByName")
    SysBaseUser queryByName(@RequestParam("username") String username);
    @GetMapping("/system/account/info")
    UserDetailsResponse getUserAccount(@RequestParam("username") String username);
    @GetMapping("/system/client/detail")
    ClientDetailsResponse getClientDetail(@RequestParam("clientId") String clientId);
}
