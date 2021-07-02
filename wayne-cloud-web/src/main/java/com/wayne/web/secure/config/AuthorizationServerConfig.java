package com.wayne.web.secure.config;

import com.wayne.web.secure.extension.CustomJwtAccessTokenConverter;
import com.wayne.web.secure.process.UnauthorizedAuthenticationEntryPoint;
import com.wayne.web.secure.provider.WayneTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.DefaultRedirectResolver;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.util.ArrayList;

/**
 * @author BinWei
 */
@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties({AuthProperties.class})
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthProperties authProperties;
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    @Autowired
    private ClientDetailsService jdbcClientDetailsService;
    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;
    @Autowired
    private ApprovalStore approvalStore;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private WayneTokenEnhancer wayneTokenEnhancer;
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 登陆成功，request_uri 匹配
        DefaultRedirectResolver defaultRedirectResolver = new DefaultRedirectResolver();
        endpoints.redirectResolver(defaultRedirectResolver);
        /*
         扩展返回信息
         注意：这里要是让wayneTokenEnhancer在jwtTokenConverter前面
         不然使用jwt的话会不成功
        * */
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        ArrayList<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(wayneTokenEnhancer);
        delegates.add(jwtAccessTokenConverter);
        enhancerChain.setTokenEnhancers(delegates);
        endpoints
                //Password认证模式需要
                .authenticationManager(authenticationManager)
                //将Token记录到数据库中
                .tokenStore(tokenStore)
                .tokenEnhancer(enhancerChain)
                //将授权码存储到数据库中
                .authorizationCodeServices(authorizationCodeServices)
                //将授权记录存储到数据库中
                .approvalStore(approvalStore)
                .reuseRefreshTokens(false); // don't reuse or we will run into session inactivity timeouts
    }


    /**
     * 配置客户端信息(这里配置的不是登录用户信息 而是可以访问系统的客户端)
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(jdbcClientDetailsService);
    }

    /**
     * 开启表单认证 支持外部接口认证
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //开启表单认证，主要是让/oauth/token支持client_id以及client_secret作登录认证
        security.allowFormAuthenticationForClients()
                //开启 /oauth/token_key 验证端口无权限访问
                .tokenKeyAccess("permitAll()")
                //开启 /oauth/check_token 验证端口无权限访问
                .checkTokenAccess("isAuthenticated()")
                .authenticationEntryPoint(new UnauthorizedAuthenticationEntryPoint());
    }
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
        JwtAccessTokenConverter converter = new CustomJwtAccessTokenConverter();
        converter.setAccessTokenConverter(defaultAccessTokenConverter);
        KeyPair keyPair = keyPair();
        converter.setKeyPair(keyPair);
        return converter;
    }

    @Bean
    public KeyPair keyPair() {
        KeyPair keyPair =
                new KeyStoreKeyFactory(
                        new ClassPathResource(authProperties.getKeyStore().getName()),
                        authProperties.getKeyStore().getPassword().toCharArray())
                        .getKeyPair(authProperties.getKeyStore().getAlias());
        return keyPair;
    }

}