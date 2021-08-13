package com.wayne.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wayne.common.plugin.system.domain.SysBaseDictType;
import com.wayne.common.web.base.BaseDomain;
import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Describe: 字典类型领域模型
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 * */

@Data
@Alias("SysDictType")
@TableName(value = "Sys_Dict_Type")
public class SysDictType extends SysBaseDictType {

    /**
     * 标识
     * */
    @TableId
    private String id;
}
