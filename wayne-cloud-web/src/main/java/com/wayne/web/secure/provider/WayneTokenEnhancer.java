package com.wayne.web.secure.provider;

import com.wayne.common.plugin.system.domain.SysBaseUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.LinkedHashMap;

public class WayneTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Object user =  authentication.getPrincipal();
        if(user instanceof SysBaseUser){
            SysBaseUser granterUser = (SysBaseUser)user;
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("username",granterUser.getUsername());
            map.put("userId", granterUser.getUserId());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(map);
            return accessToken;
        }
        //默认登录方式
        User defaultUser = (User) user;
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("client_id", authentication.getOAuth2Request().getClientId());
        map.put("username", defaultUser.getUsername());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(map);
        return accessToken;
    }
}
