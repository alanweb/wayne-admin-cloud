package com.wayne.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wayne.common.plugin.system.domain.SysBasePower;
import com.wayne.common.plugin.system.domain.SysBaseUser;
import com.wayne.common.web.base.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Describe: 用户领域模型
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */

@Getter
@Setter
@Alias("SysUser")
@TableName(value = "sys_user")
public class SysUser extends SysBaseUser {
    private static final long serialVersionUID = 1L;
    /**
     * 编号
     */
    @TableId
    private String userId;
    /**
     * 计算列
     * */
    @TableField(exist = false)
    private String roleIds;
    /**
     * 权限 这里暂时不用 security 的 Authorities
     */
    @TableField(exist = false)
    private List<String> powerCodeList;
}
