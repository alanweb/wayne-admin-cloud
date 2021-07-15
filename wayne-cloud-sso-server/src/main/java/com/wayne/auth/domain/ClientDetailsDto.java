package com.wayne.auth.domain;

/** @Author: baozi @Date: 2020/10/27 16:46 */

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
public class ClientDetailsDto implements Serializable {

  private String clientId;

  private String clientSecret;

  private Set<String> scope;

  private Set<String> resourceIds;

  private Set<String> authorities;

  private Set<String> authorizedGrantTypes;

  private Set<String> webServerRedirectUri;

  private boolean autoApprove;

  private Map<String, Object> additionalInformation;
}
