package com.wayne.gateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Create date 2020/8/12.
 *
 * @author evan
 */
@Controller
public class IndexController {

  @Value("${wayne.sso.login-success}")
  private String loginSuccess;

  @GetMapping("/")
  public String portal() {
    if (StringUtils.hasText(loginSuccess)) {
      return "redirect:" + loginSuccess;
    }
    return "index";
  }

  @GetMapping("/test")
  public String index(
      Model model,
      @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
      @AuthenticationPrincipal OAuth2User oauth2User) {
    model.addAttribute("accessToken", authorizedClient.getAccessToken().getTokenValue());
    model.addAttribute("refreshToken", authorizedClient.getRefreshToken().getTokenValue());
    model.addAttribute("userName", oauth2User.getName());
    model.addAttribute("clientName", authorizedClient.getClientRegistration().getClientName());
    model.addAttribute("userAttributes", oauth2User.getAttributes());
    return "index";
  }
}
