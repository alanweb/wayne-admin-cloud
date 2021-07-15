package com.wayne.common.web.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Describe: 基 础 实 体 类
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 * */
@Data
public class BaseDomain implements Serializable {

    /**
     * 创建时间
     * */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 创建人
     * */
    private String createBy;

    /**
     * 修改时间
     * */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 修改人
     * */
    private String updateBy;
}
