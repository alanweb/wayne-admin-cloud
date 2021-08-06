package com.wayne.web.service;

import com.wayne.common.plugin.system.domain.SysBaseLog;
import com.wayne.common.plugin.system.domain.SysBaseRole;
import com.wayne.common.plugin.system.domain.SysBaseUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("wayne-system")
public interface SystemService {
    @PostMapping("/api/system/log/save")
    String saveLog(@RequestBody SysBaseLog log);

    @PutMapping("/api/system/user/update")
    String updateUser(@RequestBody SysBaseUser user);

    @GetMapping("/api/system/user/queryByUserName")
    SysBaseUser queryByUserName(@RequestParam("username") String username);

    @GetMapping("/api/system/user/queryByUserId")
    SysBaseUser queryByUserId(@RequestParam("userId") String userId);

    @GetMapping("/api/system/user/queryUserRole")
    List<SysBaseRole> getUserRole(@RequestParam("userId") String userId);

    @GetMapping("/api/system/log/selectTopLoginLog")
    List<SysBaseLog> selectTopLoginLog(@RequestParam("username") String username);

    @GetMapping("/api/system/role/all")
    List<SysBaseRole> roleAll();

    @GetMapping("/api/system/role/{roleId}")
    SysBaseRole getRoleById(@PathVariable String roleId);
}
