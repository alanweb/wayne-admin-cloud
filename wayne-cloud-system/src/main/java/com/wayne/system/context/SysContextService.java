package com.wayne.system.context;

import com.wayne.common.plugin.system.domain.*;
import com.wayne.common.plugin.system.service.SysContext;
import com.wayne.system.domain.*;
import com.wayne.system.mapper.*;
import com.wayne.system.service.ISysDictDataService;
import com.wayne.system.service.ISysLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Describe: 对外开放的公用服务
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@Service
public class SysContextService implements SysContext {

    @Resource
    private ISysLogService sysLogService;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysPowerMapper sysPowerMapper;
    @Resource
    private SysConfigMapper sysConfigMapper;
    @Resource
    private ISysDictDataService iSysDictDataService;
    @Resource
    private SysDictDataMapper sysDictDataMapper;

    @Override
    public SysBaseUser getUserByName(String username) {
        SysBaseUser sysUserModel = sysUserMapper.selectByUsername(username);
        if (sysUserModel != null) {
            List<SysPower> powerList = sysPowerMapper.selectByUsername(username);
            if (powerList != null && powerList.size() > 0) {
                List<String> powerCodeList = powerList.stream().map(power -> power.getPowerCode()).distinct().collect(Collectors.toList());
                sysUserModel.setPowerCodeList(powerCodeList);
            }
        }
        return sysUserModel;
    }

    @Override
    public SysBaseUser getUserById(String id) {
        return sysUserMapper.selectById(id);
    }

    @Override
    public List<SysBaseRole> getRolesByUsername(String username) {
        List<SysRole> roles = sysRoleMapper.selectByUsername(username);
        List<SysBaseRole> sysRoleModelList = new ArrayList<>();
        if (roles != null && roles.size() > 0) {
            for (SysRole sysRole : roles) {
                try {
                    SysBaseRole sysRoleModel = new SysBaseRole();
                    BeanUtils.copyProperties(sysRole, sysRoleModel);
                    sysRoleModelList.add(sysRoleModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return sysRoleModelList;
    }

    @Override
    public List<SysBaseDict> selectDictByCode(String typeCode) {
        List<SysDictData> sysDictDataList = iSysDictDataService.selectByCode(typeCode);
        return buildSysDictDataModel(sysDictDataList);
    }

    @Override
    public List<SysBaseDict> queryTableDictItemsByCode(String table, String text, String code) {
        return buildSysDictDataModel(sysDictDataMapper.queryTableDictItemsByCode(table, text, code));
    }

    @Override
    public List<SysBaseDict> queryTableDictItemsByCodeAndFilter(String table, String text, String code, String filterSql) {
        return buildSysDictDataModel(sysDictDataMapper.queryTableDictItemsByCodeAndFilter(table, text, code, filterSql));
    }

    @Override
    public List<SysBaseDict> queryTableDictByKeys(String table, String text, String code, String[] keyArray) {
        return buildSysDictDataModel(sysDictDataMapper.queryTableDictByKeys(table, text, code, keyArray));
    }

    private List<SysBaseDict> buildSysDictDataModel(List<SysDictData> sysDictDataList) {
        List<SysBaseDict> sysDictDataModelList = new ArrayList<>();
        if (sysDictDataList != null && sysDictDataList.size() > 0) {
            sysDictDataModelList = sysDictDataList.stream().collect(Collectors.toList());
        }
        return sysDictDataModelList;
    }

    @Override
    public String getConfig(String code) {
        SysConfig config = sysConfigMapper.selectByCode(code);
        return config != null ? config.getConfigValue() : "";
    }

    @Override
    public Boolean saveLog(SysBaseLog baseLog) {
        SysLog log = new SysLog();
        BeanUtils.copyProperties(baseLog, log);
        return sysLogService.save(log);
    }
}
