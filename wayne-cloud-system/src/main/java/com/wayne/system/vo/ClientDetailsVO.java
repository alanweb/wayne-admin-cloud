package com.wayne.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Create date 2020/9/24.
 *
 * @author evan
 */
@Data
@ToString
@ApiModel("客户端配置信息")
public class ClientDetailsVO implements Serializable {

  @ApiModelProperty("客户端id")
  private String clientId;

  @ApiModelProperty("客户端令牌")
  private String clientSecret;

  @ApiModelProperty("权限")
  private Set<String> scope;

  @ApiModelProperty("资源id")
  private Set<String> resourceIds;

  @ApiModelProperty("当局")
  private Set<String> authorities;

  @ApiModelProperty("授权类型")
  private Set<String> authorizedGrantTypes;

  @ApiModelProperty("web服务重定向uri")
  private Set<String> webServerRedirectUri;

  @ApiModelProperty("自动批准")
  private boolean autoApprove;

  @ApiModelProperty("附加信息")
  private Map<String, Object> additionalInformation;
}
