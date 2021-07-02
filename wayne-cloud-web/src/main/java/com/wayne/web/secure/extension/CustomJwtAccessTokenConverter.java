package com.wayne.web.secure.extension;

import com.wayne.web.domain.UserAccount;
import com.wayne.web.domain.UserAccountResponse;
import com.wayne.web.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yanliang
 * @date 1/26/2021 1:53 PM
 */
public class CustomJwtAccessTokenConverter extends JwtAccessTokenConverter {

    @Autowired
    private SystemService systemService;

    @Override
    public OAuth2AccessToken enhance(
            OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        UserAccountResponse response = systemService.getUserAccount(authentication.getName());
        UserAccount account = response.getData();
        Map<String, Object> additionalInformation = new LinkedHashMap<>();
        additionalInformation.put("user_id", account.getUserId());
        List<Long> roleIds = account.getRoleIds();
        // 在目前的实现中一个用户默认对应一个角色，但也有可能出现没有角色的情况，这里用-1 标识这种情况。
        additionalInformation.put("role_id", roleIds.isEmpty() ? -1 : roleIds.get(0));
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
        return super.enhance(accessToken, authentication);
    }
}
