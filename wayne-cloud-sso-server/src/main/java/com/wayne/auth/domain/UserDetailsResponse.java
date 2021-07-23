package com.wayne.auth.domain;

import com.wayne.common.plugin.system.domain.SysBaseUser;
import lombok.Data;

/**
 * @Author bin.wei
 * @Date 2021/6/28
 * @Description
 */
@Data
public class UserDetailsResponse {
    private SysBaseUser data;
}
