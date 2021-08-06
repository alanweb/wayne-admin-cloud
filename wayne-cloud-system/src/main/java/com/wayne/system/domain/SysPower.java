package com.wayne.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wayne.common.plugin.system.domain.SysBasePower;
import com.wayne.common.web.base.BaseDomain;
import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Describe: 权限领域模型
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 * */
@Data
@Alias("SysPower")
@TableName(value = "sys_power")
public class SysPower extends SysBasePower {

    /**
     * 编号
     * */
    @TableId
    private String powerId;
    /**
     * 计算列 提供给前端组件
     * */
    @TableField(exist = false)
    private String checkArr = "0";
}
