package com.wayne.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wayne.common.plugin.system.domain.SysBaseDictData;
import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Describe: 字典值领域模型
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 * */
@Data
@Alias("SysDictData")
@TableName(value = "Sys_Dict_Data")
public class SysDictDataData extends SysBaseDictData {

    /**
     *  id 编号
     * */
    @TableId
    private String dataId;
}
