package com.wayne.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wayne.common.web.base.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author auto_generate
 * @since 2020-10-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("oauth_client_details")
public class OauthClientDetails  extends BaseDomain {

  private static final long serialVersionUID = -7565215380631346193L;

  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;

  @TableField("client_id")
  private String clientId;

  @TableField("resource_ids")
  private String resourceIds;

  @TableField("client_secret")
  private String clientSecret;

  @TableField("scope")
  private String scope;

  @TableField("authorized_grant_types")
  private String authorizedGrantTypes;

  @TableField("web_server_redirect_uri")
  private String webServerRedirectUri;

  @TableField("authorities")
  private String authorities;

  @TableField("access_token_validity")
  private Integer accessTokenValidity;

  @TableField("refresh_token_validity")
  private Integer refreshTokenValidity;

  @TableField("additional_information")
  private String additionalInformation;

  @TableField("auto_approve")
  private String autoApprove;
}
