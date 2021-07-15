package com.wayne.auth.service.impl;

import com.wayne.auth.domain.ClientDetailsDto;
import com.wayne.auth.domain.ClientDetailsResponse;
import com.wayne.auth.service.SystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

/**
 * @Author bin.wei
 * @Date 2021/7/5
 * @Description
 */
@Service
@Slf4j
public class WayneClientDetailsService {
    @Autowired
    private SystemService systemService;
    public ClientDetailsDto getClientDetailsDto(Object clientId) throws ClientRegistrationException {
        ClientDetailsResponse responseData = null;
        try {
            log.info("get ClientDetails by clientId={}", clientId);
            responseData = systemService.getClientDetail(clientId.toString());
        } catch (Exception e) {
            log.warn("ClientDetails {} was not found in the database", clientId);
            throw new ClientRegistrationException(
                    "ClientDetails " + clientId + " was not found in the database");
        }
        if (null != responseData && null != responseData.getData()) {
            ClientDetailsDto acc = responseData.getData();
            if (null == acc.getClientId() || null == acc.getClientSecret()) {
                log.warn("Account {} was not found in the database, read from portal server", clientId);
                return null;
            }
            return acc;
        }
        return null;
    }
}

