package com.wayne.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wayne.common.web.base.BaseDomain;
import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Describe: 角色领域模型
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 * */
@Data
@Alias("SysRole")
@TableName(value = "Sys_Role")
public class SysRole extends BaseDomain {

    /**
     * 编号
     * */
    @TableId
    private String roleId;

    /**
     * 角色名称
     * */
    private String roleName;

    /**
     * 角色值
     * */
    private String roleCode;

    /**
     * 状态
     * */
    private String enable;

    /**
     * 描述
     */
    private String details;

    /**
     * 排序
     * */
    private Integer sort;

    /**
     * 提供前端 显示
     * */
    private boolean checked = false;
}
