package com.wayne.auth.config;

import com.wayne.auth.extension.RemoteClientDetailsService;
import com.wayne.auth.extension.UnauthorizedAuthenticationEntryPoint;
import com.wayne.auth.property.SecurityProperties;
import com.wayne.auth.service.impl.WayneUserDetailsService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.DefaultRedirectResolver;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 认证服务器配置
 */
@Configuration
@EnableAuthorizationServer
public class Oauth2ServerConfig extends AuthorizationServerConfigurerAdapter implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;
    @Autowired
    private RemoteClientDetailsService remoteClientDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 客户端访问方式配置数据在数据库中
        clients.withClientDetails(remoteClientDetailsService);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 登陆成功，request_uri 匹配
        DefaultRedirectResolver defaultRedirectResolver = new DefaultRedirectResolver();
        endpoints.redirectResolver(defaultRedirectResolver);

        JdbcAuthorizationCodeServices jdbcAuthorizationCodeServices =
                new JdbcAuthorizationCodeServices(dataSource);
        endpoints.authorizationCodeServices(jdbcAuthorizationCodeServices);

        Collection<TokenEnhancer> tokenEnhancers =
                applicationContext.getBeansOfType(TokenEnhancer.class).values();
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        //配置JWT的内容增强器
        tokenEnhancerChain.setTokenEnhancers(new ArrayList<>(tokenEnhancers));

        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancerChain)
                .reuseRefreshTokens(false); // don't reuse or we will run into session inactivity timeouts
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()") // 任何用户都可以去获取token
                .checkTokenAccess("isAuthenticated()") // 认证过的用户才能去检查token
                .authenticationEntryPoint(new UnauthorizedAuthenticationEntryPoint())
                .allowFormAuthenticationForClients(); // 允许表单认证// 校验token
    }

    /**
     * 对remember me 的token进行持久化
     *
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    /**
     * 注册SessionRegistry
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    /**
     * Apply the token converter (and enhancer) for token store.
     *
     * @return the {@link JwtTokenStore} managing the tokens.
     */
    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair());
        return converter;
    }

    @Bean
    public KeyPair keyPair() {
        SecurityProperties.KeyStore keyStore = securityProperties.getKeyStore();
        //从classpath下的证书中获取秘钥对
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(keyStore.getName()), keyStore.getPassword().toCharArray());
        return keyStoreKeyFactory.getKeyPair(keyStore.getAlias(), keyStore.getSecret().toCharArray());
    }
}