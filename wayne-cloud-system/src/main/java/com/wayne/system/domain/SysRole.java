package com.wayne.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wayne.common.plugin.system.domain.SysBaseRole;
import com.wayne.common.web.base.BaseDomain;
import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Describe: 角色领域模型
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@Data
@Alias("SysRole")
@TableName(value = "Sys_Role")
public class SysRole extends SysBaseRole {

    /**
     * 编号
     */
    @TableId
    private String roleId;

    private String remark;
}
