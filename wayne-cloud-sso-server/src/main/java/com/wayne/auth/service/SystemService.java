package com.wayne.auth.service;

import com.wayne.auth.domain.ClientDetailsResponse;
import com.wayne.auth.domain.UserDetailsResponse;
import com.wayne.common.plugin.system.domain.SysBaseLog;
import com.wayne.common.plugin.system.domain.SysBaseUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("wayne-system")
public interface SystemService {
    @PostMapping("/api/system/log/save")
    String saveLog(@RequestBody SysBaseLog log);
    @PutMapping("/api/system/user/update")
    String updateUser(@RequestBody SysBaseUser user);
    @GetMapping("/api/system/user/queryByName")
    SysBaseUser queryByName(@RequestParam("username") String username);
    @GetMapping("/api/system/account/info")
    UserDetailsResponse getUserAccount(@RequestParam("username") String username);
    @GetMapping("/api/system/client/detail")
    ClientDetailsResponse getClientDetail(@RequestParam("clientId") String clientId);
}
