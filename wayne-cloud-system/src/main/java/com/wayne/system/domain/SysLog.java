package com.wayne.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wayne.common.plugin.logging.aop.enums.BusinessType;
import com.wayne.common.plugin.logging.aop.enums.LoggingType;
import com.wayne.common.plugin.logging.aop.enums.RequestMethod;
import com.wayne.common.plugin.system.domain.SysBaseLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;
import org.apache.ibatis.type.JdbcType;
import org.springframework.data.annotation.Transient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Describe: 日 志 实 体 类
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_logging")
public class SysLog extends SysBaseLog {

    /**
     * 编号
     */
    @TableId
    private String id;

    /**
     * 扩 展 信 息
     */
    @TableField(exist = false)
    private Map<String, String> map = new HashMap<>();
}
