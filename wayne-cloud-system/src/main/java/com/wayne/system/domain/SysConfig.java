package com.wayne.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wayne.common.plugin.system.domain.SysBaseConfig;
import com.wayne.common.web.base.BaseDomain;
import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Describe: 系 统 配 置
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 * */

@Data
@Alias("SysConfig")
@TableName("sys_config")
public class SysConfig extends SysBaseConfig {

    /**
     * 配置标识
     * */
    @TableId
    private String configId;
}
