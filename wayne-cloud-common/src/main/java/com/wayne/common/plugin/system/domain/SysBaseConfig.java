package com.wayne.common.plugin.system.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SysBaseConfig {

    /**
     * 配置标识
     * */
    private String configId;

    /**
     * 配置名称
     * */
    private String configName;

    /**
     * 配置类型
     * */
    private String configType;

    /**
     * 配置标识
     * */
    private String configCode;

    /**
     * 配置值
     * */
    private String configValue;

}
