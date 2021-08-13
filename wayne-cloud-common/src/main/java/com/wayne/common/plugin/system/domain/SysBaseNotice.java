package com.wayne.common.plugin.system.domain;

import com.wayne.common.web.base.BaseDomain;
import lombok.Data;

@Data
public class SysBaseNotice extends BaseDomain {
    /** 编号 */
    private String id;

    /** 标题 */
    private String title;

    /** 内容 */
    private String content;

    /** 发送人 */
    private String sender;

    /** 发送人 */
    private String senderName;

    /** 接收者 */
    private String accept;

    /** 接收人 */
    private String acceptName;

    /** 类型 */
    private String type;
}
