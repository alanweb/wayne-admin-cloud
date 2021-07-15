package com.wayne.system.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Create date 2020/9/24.
 *
 * @author evan
 */
@Data
@ToString
@ApiModel("客户端创建入参")
public class ClientDetailsParam implements Serializable {

  @ApiModelProperty("client id")
  private String clientId;

  @ApiModelProperty("clientSecret")
  private String clientSecret;

  @ApiModelProperty("webServerRedirectUri")
  private String webServerRedirectUri;
}
