package com.wayne.common.plugin.system.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wayne.common.web.base.BaseDomain;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SysBaseDept extends BaseDomain {
    /**
     * 部门编号
     */
    private String deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门地址
     */
    private String address;

    /**
     * 父级编号
     */
    private String parentId;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态
     */
    private String status;

    /**
     * 排序
     */
    private Integer sort;
}