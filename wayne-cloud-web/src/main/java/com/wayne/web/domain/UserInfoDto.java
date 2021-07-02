package com.wayne.web.domain;

import lombok.Data;

import java.util.List;

/** A DTO representing a user, with his authorities. */
@Data
public class UserInfoDto {

  private String id;

  private String sub;

  private List<String> roles;

  private boolean activated = false;
}
