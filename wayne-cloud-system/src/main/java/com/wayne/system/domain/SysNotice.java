package com.wayne.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wayne.common.plugin.system.domain.SysBaseNotice;
import com.wayne.common.web.base.BaseDomain;
import lombok.Data;

/**
 * notice对象 sys_notice
 *
 * @author jmys
 * @date 2021-03-13
 */
@Data
@TableName(value = "sys_notice")
public class SysNotice extends SysBaseNotice {
    /**
     * 编号
     */
    @TableId
    private String id;

    /**
     * 发送人
     */
    @TableField(exist = false)
    private String senderName;

    /**
     * 接收人
     */
    @TableField(exist = false)
    private String acceptName;

}
