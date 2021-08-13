package com.wayne.web.service;

import com.wayne.common.plugin.system.domain.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("wayne-system")
public interface SystemService {
    @PostMapping("/system/log/save")
    String saveLog(@RequestBody SysBaseLog log);

    @PutMapping("/system/user/update")
    String updateUser(@RequestBody SysBaseUser user);

    @GetMapping("/system/user/queryByUserName")
    SysBaseUser queryByUserName(@RequestParam("username") String username);

    @GetMapping("/system/user/queryByUserId")
    SysBaseUser queryByUserId(@RequestParam("userId") String userId);

    @GetMapping("/system/user/queryUserRole")
    List<SysBaseRole> getUserRole(@RequestParam("userId") String userId);

    @GetMapping("/system/log/selectTopLoginLog")
    List<SysBaseLog> selectTopLoginLog(@RequestParam("username") String username);

    @GetMapping("/system/role/all")
    List<SysBaseRole> roleAll();

    @GetMapping("/system/role/{roleId}")
    SysBaseRole getRoleById(@PathVariable String roleId);

    @GetMapping("/system/power/{powerId}")
    SysBasePower getPowerById(@PathVariable String powerId);

    @GetMapping("/system/dept/{deptId}")
    SysBaseDept getDeptById(@PathVariable String deptId);

    @GetMapping("/system/dictType/{dictTypeId}")
    SysBaseDictType getDictTypeById(@PathVariable String dictTypeId);

    @GetMapping("/system/dictData/{dictDataId}")
    SysBaseDictData getDictDataById(@PathVariable String dictDataId);

    @GetMapping("/system/config/{configId}")
    SysBaseConfig getConfigById(@PathVariable String configId);

    @GetMapping("/system/config/queryByCode")
    SysBaseConfig getByCode(@RequestParam("code") String code);

    @GetMapping("/system/notice/{id}")
    SysBaseNotice getNoticeById(@PathVariable String id);
}
