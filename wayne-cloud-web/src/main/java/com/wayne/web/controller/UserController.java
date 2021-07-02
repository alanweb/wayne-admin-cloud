package com.wayne.web.controller;

import com.wayne.common.web.base.BaseController;
import com.wayne.common.web.domain.response.Result;
import com.wayne.web.domain.UserAccount;
import com.wayne.web.domain.UserAccountResponse;
import com.wayne.web.domain.UserInfoDto;
import com.wayne.web.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author bin.wei
 * @Date 2021/6/28
 * @Description
 */
@RestController
@RequestMapping("/api")
public class UserController extends BaseController {
    @Autowired
    private SystemService systemService;

    /**
     * 通过oauth之后，通过这个接口，获取登陆的用户信息
     *
     * @return
     */
    @GetMapping("/userInfo")
    public ResponseEntity<UserInfoDto> getUserInfo() {
        String loginName = getCurrentUserName();
        UserAccountResponse response = systemService.getUserAccount(loginName);
        if (null != response) {
            return ResponseEntity.ok(createUserDto(response.getData()));
        }
        return ResponseEntity.noContent().build();
    }

    private UserInfoDto createUserDto(UserAccount account) {
        UserInfoDto dto = new UserInfoDto();
        dto.setId(account.getUserId());
        dto.setActivated(true);
        dto.setSub(account.getLoginName());
        dto.setRoles(account.getRoles());
        return dto;
    }
}
