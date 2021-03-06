package com.wayne.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Describe: 角色权限映射关系
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 * */
@Data
@Alias("SysRolePower")
@TableName(value = "Sys_Role_Power")
public class SysRolePower {

    /**
     * 映射编号
     * */
    private String id;

    /**
     * 角色编号
     * */
    private String roleId;

    /**
     * 权限编号
     * */
    private String powerId;

}
