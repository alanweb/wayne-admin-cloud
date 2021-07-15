package com.wayne.web.service;

import com.wayne.common.plugin.system.domain.SysBaseLog;
import com.wayne.common.plugin.system.domain.SysBaseUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("wayne-system")
public interface SystemService {
    @PostMapping("/system/log/save")
    String saveLog(@RequestBody SysBaseLog log);

    @PostMapping("/system/user/update")
    String updateUser(@RequestBody SysBaseUser user);

    @GetMapping("/system/user/queryByName")
    SysBaseUser queryByName(@RequestParam("username") String username);

}
