package com.wayne.auth.domain;

import lombok.Data;

import java.util.List;

@Data
public class UserInfoDto {
  private String id;
  private String sub;
  private List<String> roles;
  private boolean activated = false;
}
