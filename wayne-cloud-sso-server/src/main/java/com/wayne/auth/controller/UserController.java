package com.wayne.auth.controller;

import com.wayne.auth.service.impl.WayneUserDetailsService;
import com.wayne.common.web.base.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create date 2020/7/26.
 *
 * @author evan
 */
@RestController
@RequestMapping("/api")
public class UserController extends BaseController {
    private final WayneUserDetailsService wayneUserDetailsService;

    public UserController(WayneUserDetailsService wayneUserDetailsService) {
        this.wayneUserDetailsService = wayneUserDetailsService;
    }

    /**
     * 通过oauth之后，通过这个接口，获取登陆的用户信息
     *
     * @return
     */
    @GetMapping("/userInfo")
    public ResponseEntity<UserDetails> getUserInfo() {
        String loginName = getCurrentUserName();
        UserDetails userDetails = wayneUserDetailsService.getUserDetails(loginName);
        if (null != userDetails) {
            return ResponseEntity.ok(userDetails);
        }
        return ResponseEntity.notFound().build();
    }
}
