package com.wayne.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wayne.system.domain.SysMenu;
import com.wayne.system.domain.SysPower;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Describe: 系统权限接口
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@Repository
public interface SysPowerMapper extends BaseMapper<SysPower> {


    /**
     * Describe: 根据 username 查询用户权限
     * Param: username
     * Return: SysPower
     */
    List<SysPower> selectByUsername(String username);

    /**
     * Describe: 根据 username 查询用户菜单
     * Param: username
     * Return: ResuMenu
     */
    List<SysMenu> selectMenuByUsername(String username);
}
