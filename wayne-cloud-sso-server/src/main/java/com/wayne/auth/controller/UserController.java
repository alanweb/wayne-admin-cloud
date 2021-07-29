package com.wayne.auth.controller;

import com.wayne.auth.service.impl.WayneUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class UserController {
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object currentUser = null;
        if (null != authentication && authentication.isAuthenticated()) {
            currentUser = authentication.getPrincipal();
        }
        String loginName = null;
        if (currentUser instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) currentUser;
            loginName = springSecurityUser.getUsername();
        } else if (currentUser instanceof String) {
            loginName = (String) currentUser;
        }
        UserDetails userDetails = wayneUserDetailsService.getUserDetails(loginName);
        if (null != userDetails) {
            return ResponseEntity.ok(userDetails);
        }
        return ResponseEntity.notFound().build();
    }
}
