package com.wayne.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("登录用户信息")
public class UserAccount {

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("登录名称")
    private String loginName;

    @ApiModelProperty("加密后的密码")
    private String passwordSha;
    @ApiModelProperty("是否活动")
    private boolean isActive;
    @ApiModelProperty("角色信息")
    private List<String> roles;

    @ApiModelProperty("角色id信息")
    private List<String> roleIds;
}
