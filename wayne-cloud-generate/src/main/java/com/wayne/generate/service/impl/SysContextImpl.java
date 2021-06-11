package com.wayne.generate.service.impl;

import com.wayne.common.plugin.system.domain.SysBaseDict;
import com.wayne.common.plugin.system.domain.SysBaseLog;
import com.wayne.common.plugin.system.domain.SysBaseRole;
import com.wayne.common.plugin.system.domain.SysBaseUser;
import com.wayne.common.plugin.system.service.SysContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author bin.wei
 * @Date 2021/6/11
 * @Description
 */
@Slf4j
@Service
public class SysContextImpl implements SysContext {
    @Override
    public SysBaseUser getUserByName(String username) {
        return null;
    }

    @Override
    public SysBaseUser getUserById(String id) {
        return null;
    }

    @Override
    public List<SysBaseRole> getRolesByUsername(String username) {
        return null;
    }

    @Override
    public List<SysBaseDict> selectDictByCode(String typeCode) {
        return null;
    }

    @Override
    public List<SysBaseDict> queryTableDictItemsByCode(String table, String text, String code) {
        return null;
    }

    @Override
    public List<SysBaseDict> queryTableDictItemsByCodeAndFilter(String table, String text, String code, String filterSql) {
        return null;
    }

    @Override
    public List<SysBaseDict> queryTableDictByKeys(String table, String text, String code, String[] keyArray) {
        return null;
    }

    @Override
    public String getConfig(String code) {
        return null;
    }

    @Override
    public Boolean saveLog(SysBaseLog logInfo) {
        log.debug("打印一句话{}", logInfo);
        return null;
    }
}
