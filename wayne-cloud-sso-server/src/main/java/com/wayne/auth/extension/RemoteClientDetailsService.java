package com.wayne.auth.extension;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Sets;
import com.wayne.auth.domain.ClientDetailsDto;
import com.wayne.auth.property.SecurityProperties;
import com.wayne.auth.service.impl.WayneClientDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Create date 2020/9/24.
 *
 * @author evan
 */
@Component
public class RemoteClientDetailsService implements ClientDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(RemoteClientDetailsService.class);

    /**
     * Access tokens will not expire any earlier than this.
     */
    private static final int MIN_ACCESS_TOKEN_VALIDITY_SECS = 60;

    private static final String PORTAL_CLIENT_ID = "wayne_gateway";

    private final String redirectUri;
    private final SecurityProperties securityProperties;
    private final LoadingCache<String, ClientDetailsDto> clientDetailsDtoCache;

    public RemoteClientDetailsService(
            @Value("${wayne.account.default-client-redirect-uri}") String redirectUri,
            SecurityProperties securityProperties,
            WayneClientDetailsService wayneClientDetailsService) {
        this.redirectUri = redirectUri;
        this.securityProperties = securityProperties;
        this.clientDetailsDtoCache =
                CacheBuilder.newBuilder()
                        .expireAfterAccess(Duration.ofMinutes(1).getSeconds(), TimeUnit.SECONDS)
                        .build(new CacheLoader<String, ClientDetailsDto>() {
                            @Override
                            public ClientDetailsDto load(final String s) {
                                return wayneClientDetailsService.getClientDetailsDto(s);
                            }
                        });
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        ClientDetailsDto dto = null;
        try {
            dto = this.clientDetailsDtoCache.get(clientId);
        } catch (ExecutionException e) {
            logger.warn("", e);
        }

        int accessTokenValidity =
                securityProperties.getWebClientConfiguration().getAccessTokenValidityInSeconds();
        accessTokenValidity = Math.max(accessTokenValidity, MIN_ACCESS_TOKEN_VALIDITY_SECS);
        int refreshTokenValidity =
                securityProperties.getWebClientConfiguration().getRefreshTokenValidityInSecondsForRememberMe();
        refreshTokenValidity = Math.max(refreshTokenValidity, accessTokenValidity);

        if (null != dto) {
            BaseClientDetails result = new BaseClientDetails();
            result.setClientId(dto.getClientId());
            result.setClientSecret(dto.getClientSecret());
            result.setAuthorizedGrantTypes(dto.getAuthorizedGrantTypes());
            result.setAccessTokenValiditySeconds(accessTokenValidity);
            result.setRefreshTokenValiditySeconds(refreshTokenValidity);
            if (PORTAL_CLIENT_ID.equals(dto.getClientId()) && !StringUtils.isEmpty(redirectUri)) {
                result.setRegisteredRedirectUri(Sets.newHashSet(redirectUri));
            } else {
                result.setRegisteredRedirectUri(Sets.newHashSet(dto.getWebServerRedirectUri()));
            }
            result.setScope(dto.getScope());
            result.setAuthorities(
                    AuthorityUtils.createAuthorityList(
                            dto.getAuthorities().toArray(new String[dto.getAuthorities().size()])));
            result.setResourceIds(dto.getResourceIds());
            result.setAdditionalInformation(dto.getAdditionalInformation());
            if (dto.isAutoApprove()) {
                result.setAutoApproveScopes(dto.getScope());
            } else {
                result.setAutoApproveScopes(dto.getScope());
            }
            return result;
        }
        throw new ClientRegistrationException(
                "ClientDetails " + clientId + " was not found in the RESTful Service");
    }
}
