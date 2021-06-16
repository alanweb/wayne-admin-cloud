package com.wayne.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Describe: 用户角色映射关系
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 * */

@Data
@Alias("SysUserRole")
@TableName(value = "sys_user_role")
public class SysUserRole {

    /**
     * 映射标识
     * */
    @TableId
    private String id;

    /**
     * 用户编号
     * */
    private String userId;

    /**
     * 角色编号
     * */
    private String roleId;

}
