package com.wayne.web.domain;

import lombok.Data;

import java.util.List;

@Data
public class UserAccount {

    private String userId;

    private String loginName;

    private String passwordSha;

    private boolean isActive;

    private List<String> roles;

    private List<Long> roleIds;
}
