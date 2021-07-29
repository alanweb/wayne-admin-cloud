package com.wayne.system.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class QueryUserParam {
    /**
     * 账户
     */
    private String username;
    /**
     * 姓名
     */
    private String realName;

    /**
     * 所属部门
     */
    private String deptId;
}
