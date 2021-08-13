package com.wayne.common.plugin.system.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wayne.common.web.base.BaseDomain;
import lombok.Data;

/**
 * Describe: 字典值领域模型
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SysBaseDictType extends BaseDomain {

    /**
     * 标识
     */
    private String id;

    /**
     * 字典名称
     */
    private String typeName;

    /**
     * 字典类型
     */
    private String typeCode;

    /**
     * 字典描述
     */
    private String description;

    /**
     * 字典可用状态
     */
    private String enable;
    /**
     * 备注
     */
    private String remark;
}
