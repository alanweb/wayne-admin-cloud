package com.wayne.common.plugin.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wayne.common.web.base.BaseDomain;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Describe: 用户领域模型
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SysBaseUser extends BaseDomain implements UserDetails {
    public static final String C_DISABLE = "0";
    public static final String C_NORMAL = "1";
    /**
     * 编号
     */
    private String userId;

    /**
     * 账户
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 状态
     */
    private String status;

    /**
     * 姓名
     */
    private String realName;

    /**
     * 邮箱
     */

    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别
     */
    private String sex;

    /**
     * 电话
     */
    private String phone;

    /**
     * 所属部门
     */
    private String deptId;

    /**
     * 是否启用
     */
    private String enable;

    /**
     * 是否登录
     */
    private String login;

    /**
     * 计算列
     */
    private String roleIds;

    private String remark;
    /**
     * 最后一次登录时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastTime;

    /**
     * 权限 这里暂时不用 security 的 Authorities
     */
    private List<String> powerCodeList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (CollectionUtils.isEmpty(powerCodeList))
            return null;
        else {
            return powerCodeList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return C_NORMAL.equals(this.getStatus()) ? true : false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return C_NORMAL.equals(this.getEnable()) ? true : false;
    }
}
