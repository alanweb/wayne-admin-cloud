package com.wayne.web.service;

import com.wayne.common.plugin.system.domain.SysBaseLog;
import com.wayne.common.plugin.system.domain.SysBaseUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("wayne-system")
public interface SystemService {
    @PostMapping("/system/log/save")
    String saveLog(SysBaseLog log);
    @PostMapping("/system/user/update")
    String updateUser(SysBaseUser user);
    @GetMapping("/system/user/queryByName")
    SysBaseUser queryByUserName(String username);
}
