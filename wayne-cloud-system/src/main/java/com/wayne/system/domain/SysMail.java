package com.wayne.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Describe: 邮 件 发 送 实 体
 * Author: Heiky
 * CreateTime: 2021/1/13
 */
@Data
@Alias("SysMail")
@TableName(value = "sys_mail")
public class SysMail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 邮件id
     */
    @TableId
    private String mailId;

    /**
     * 接收人（邮箱）
     */
    private String receiver;

    /**
     * 邮件主体
     */
    private String subject;

    /**
     * 邮件正文
     */
    private String content;

    /**
     * 发送人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
