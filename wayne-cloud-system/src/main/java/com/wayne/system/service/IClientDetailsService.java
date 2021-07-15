package com.wayne.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wayne.system.domain.OauthClientDetails;
import com.wayne.system.param.ClientDetailsParam;
import com.wayne.system.vo.ClientDetailsVO;

import java.util.List;

/**
 * @Author bin.wei
 * @Date 2021/7/7
 * @Description
 */
public interface IClientDetailsService extends IService<OauthClientDetails> {
    void addClientDetails(ClientDetailsParam clientDetailsParam);

    void updateClientDetails(ClientDetailsParam clientDetailsParam);

    void removeClientDetails(String clientId);

    List<ClientDetailsVO> listClientDetails(String clientId, String webServerRedirectUri);

    ClientDetailsVO getClientDetails(String clientId);
}
