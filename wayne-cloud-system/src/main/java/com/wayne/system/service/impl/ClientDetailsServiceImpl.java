package com.wayne.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import com.wayne.common.exception.SystemException;
import com.wayne.system.domain.OauthClientDetails;
import com.wayne.system.mapper.ClientDetailsMapper;
import com.wayne.system.param.ClientDetailsParam;
import com.wayne.system.service.IClientDetailsService;
import com.wayne.system.vo.ClientDetailsVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Create date 2020/9/24.
 *
 * @author evan
 */
@Service
@Slf4j
public class ClientDetailsServiceImpl extends ServiceImpl<ClientDetailsMapper, OauthClientDetails> implements IClientDetailsService {

    public static final String DEFAULT_RESOURCE = "uaa,oauth2-resource,apis";
    public static final String DEFAULT_SCOPE = "api-read,api-write";
    public static final String DEFAULT_AUTHORIZED_GRANT_TYPES =
            "password,implicit,authorization_code,refresh_token";

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * @param clientDetailsParam
     */
    @Transactional
    @Override
    public void addClientDetails(ClientDetailsParam clientDetailsParam) {
        OauthClientDetails clientId =
                baseMapper.selectOne(
                        new QueryWrapper<OauthClientDetails>()
                                .lambda()
                                .eq(OauthClientDetails::getClientId, clientDetailsParam.getClientId()));
        if (null != clientId) {
            throw new SystemException("client_id is exits");
        }
        if (StringUtils.isEmpty(clientDetailsParam.getClientSecret())) {
            throw new SystemException("client secret is null or empty");
        }
        log.info("add client details: {}", clientDetailsParam);
        String clientSecretEncoded = passwordEncoder.encode(clientDetailsParam.getClientSecret());

        OauthClientDetails clientDetails = new OauthClientDetails();
        clientDetails.setClientId(clientDetailsParam.getClientId());
        clientDetails.setClientSecret(clientSecretEncoded);
        clientDetails.setResourceIds(DEFAULT_RESOURCE);
        clientDetails.setScope(DEFAULT_SCOPE);
        clientDetails.setAuthorities("USER");
        clientDetails.setAuthorizedGrantTypes(DEFAULT_AUTHORIZED_GRANT_TYPES);
        clientDetails.setAutoApprove("true");
        clientDetails.setWebServerRedirectUri(clientDetailsParam.getWebServerRedirectUri());
        clientDetails.setAdditionalInformation("");
        clientDetails.setAccessTokenValidity(0);
        clientDetails.setRefreshTokenValidity(0);

        int result = baseMapper.insert(clientDetails);
        if (result > 0) {
            log.info("add client details success {}", clientDetails);
        } else {
            log.warn("add client details failed {}", clientDetails);
        }
    }

    @Override
    public void updateClientDetails(ClientDetailsParam clientDetailsParam) {
        log.info("update client details: {}", clientDetailsParam);
        String secret = clientDetailsParam.getClientSecret();
        String requestUri = clientDetailsParam.getWebServerRedirectUri();
        if (StringUtils.isAllEmpty(requestUri, secret)) {
            throw new SystemException("request_uri,secret is all empty");
        }
        OauthClientDetails oauthClientDetails =
                baseMapper.selectOne(
                        new QueryWrapper<OauthClientDetails>()
                                .lambda()
                                .eq(OauthClientDetails::getClientId, clientDetailsParam.getClientId()));
        if (null == oauthClientDetails) {
            throw new SystemException("client_id not found");
        }
        oauthClientDetails.setWebServerRedirectUri(requestUri);

        if (StringUtils.isNotEmpty(secret)&&!oauthClientDetails.equals(oauthClientDetails.getClientSecret())) {
            secret =  passwordEncoder.encode(secret);
            oauthClientDetails.setClientSecret(secret);
        }
        int result = baseMapper.updateById(oauthClientDetails);
        if (result > 0) {
            log.info("update client details success {}", oauthClientDetails);
        } else {
            log.warn("update client details failed {}", oauthClientDetails);
        }
    }
    @Override
    public void removeClientDetails(String clientId) {
        log.info("remove client detail: {}", clientId);
        int result =
                baseMapper.delete(
                        new QueryWrapper<OauthClientDetails>()
                                .lambda()
                                .eq(OauthClientDetails::getClientId, clientId));
        if (result > 0) {
            log.info("remove client detail success {}", clientId);
        } else {
            log.warn("remove client detail failed {}", clientId);
        }
    }
    @Override
    public ClientDetailsVO getClientDetails(String clientId) {
        log.debug("get client details");
        OauthClientDetails clientDetails =
                baseMapper.selectOne(
                        new QueryWrapper<OauthClientDetails>()
                                .lambda()
                                .eq(OauthClientDetails::getClientId, clientId));
        if (null != clientDetails) {
            return to(clientDetails);
        }
        return null;
    }
    @Override
    public List<ClientDetailsVO> listClientDetails(String clientId, String webServerRedirectUri) {
        log.debug("list client details");
        List<OauthClientDetails> clientDetails =
                baseMapper.selectList(
                        new QueryWrapper<OauthClientDetails>()
                                .lambda()
                                .eq(!StringUtils.isEmpty(clientId), OauthClientDetails::getClientId, clientId)
                                .eq(
                                        !StringUtils.isEmpty(webServerRedirectUri),
                                        OauthClientDetails::getWebServerRedirectUri,
                                        webServerRedirectUri));
        return clientDetails.stream().map(this::to).collect(Collectors.toList());
    }

    private ClientDetailsVO to(OauthClientDetails details) {
        ClientDetailsVO dto = new ClientDetailsVO();
        dto.setClientId(details.getClientId());
        dto.setClientSecret(details.getClientSecret());
        dto.setAdditionalInformation(new HashMap<>());
        dto.setAuthorities(sep(details.getAuthorities()));
        dto.setAuthorizedGrantTypes(sep(details.getAuthorizedGrantTypes()));
        dto.setScope(sep(details.getScope()));
        dto.setResourceIds(sep(details.getResourceIds()));
        dto.setAutoApprove(true);
        dto.setWebServerRedirectUri(sep(details.getWebServerRedirectUri()));
        return dto;
    }

    private Set<String> sep(String str) {
        if (StringUtils.isEmpty(str)) {
            return Collections.emptySet();
        }
        return Sets.newHashSet(str.split(","));
    }
}
