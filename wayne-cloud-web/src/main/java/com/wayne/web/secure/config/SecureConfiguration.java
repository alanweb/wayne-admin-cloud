package com.wayne.web.secure.config;

import com.wayne.common.config.proprety.SecurityProperty;
import com.wayne.web.secure.domain.SecureUserDetailsService;
import com.wayne.web.secure.domain.SecureUserTokenService;
import com.wayne.web.secure.process.UnauthorizedAuthenticationEntryPoint;
import com.wayne.web.secure.process.*;
import com.wayne.web.secure.provider.WayneTokenEnhancer;
import com.wayne.web.secure.support.SecureCaptchaSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Describe: Security 安全配置
 * Author: 就眠仪式
 * CreateTime: 2019/10/23
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(SecurityProperty.class)
public class SecureConfiguration extends WebSecurityConfigurerAdapter {


    /**
     * 实现userService
     */
    @Resource
    private SecureUserDetailsService securityUserDetailsService;

    /**
     * 密码加密
     */
    @Resource
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DataSource dataSource;

    @Bean
    public WayneTokenEnhancer wayneTokenEnhancer() {
        return new WayneTokenEnhancer();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Bean
    public ClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }

    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    /**
     * 身份认证接口
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityUserDetailsService).passwordEncoder(passwordEncoder);
    }

    @EnableResourceServer
    @Configuration
    public static class AuthResourceServerConfiguration extends ResourceServerConfigurerAdapter {
        /**
         * 配置不拦截url
         */
        @Resource
        private SecurityProperty securityProperty;

        /**
         * 自定义验证码验证
         */
        @Resource
        private SecureCaptchaSupport securityCaptchaSupport;
        /**
         * 登录成功处理类
         */
        @Resource
        private SecureAuthenticationSuccessHandler securityAccessSuccessHandler;

        /**
         * 登录失败处理类
         */
        @Resource
        private SecureAuthenticationFailureHandler securityAccessFailureHandler;


        /**
         * 退出登录处理类
         */
        @Resource
        private SecureLogoutSuccessHandler securityAccessLogoutHandler;

        /**
         * 没有权限处理类
         */
        @Resource
        private SecureAccessDeniedHandler securityAccessDeniedHandler;

        /**
         * 用于统计用户在线
         */
        @Resource
        private SessionRegistry sessionRegistry;

        @Resource
        private SecureRememberMeHandler rememberMeAuthenticationSuccessHandler;
        /**
         * remember me redis持久化
         */
        @Resource
        private SecureUserTokenService securityUserTokenService;

        @Resource
        private SecureSessionExpiredHandler securityExpiredSessionHandler;
        private final TokenStore tokenStore;
        private final CorsFilter corsFilter;

        public AuthResourceServerConfiguration(TokenStore tokenStore, CorsFilter corsFilter) {
            this.tokenStore = tokenStore;
            this.corsFilter = corsFilter;
        }

        /**
         * 安全拦截机制
         *
         * @param http
         * @throws Exception
         */
        @Override
        public void configure(HttpSecurity http) throws Exception {
            // 设置共享变量，目的是想要在登陆成功之后，正确重定向
            http.setSharedObject(RequestCache.class, new HttpSessionRequestCache());
            http.addFilter(corsFilter);
            http.authorizeRequests()
                    .antMatchers(securityProperty.getOpenApi()).permitAll()
                    .and()
                    // 验证码验证类
                    .addFilterBefore(securityCaptchaSupport, UsernamePasswordAuthenticationFilter.class)
                    //认证失败操作类
                    .httpBasic()
                    .authenticationEntryPoint(new UnauthorizedAuthenticationEntryPoint())
                    .and()
                    // 取消跨站请求伪造防护
                    .csrf().disable()
                    // 防止iframe 造成跨域
                    .headers().frameOptions().disable()
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .and()
                    .formLogin()
                    // 登录页面
                    .loginPage("/login")
                    // 登录接口
                    .loginProcessingUrl("/login")
                    // 配置登录成功自定义处理类
                    .successHandler(securityAccessSuccessHandler)
                    // 配置登录失败自定义处理类
                    .failureHandler(securityAccessFailureHandler)
                    .and()
                    .logout()
                    .logoutSuccessHandler(securityAccessLogoutHandler)
                    .deleteCookies("refresh_token", "access_token", "JSESSIONID")
                    .and()
                    .exceptionHandling()
                    // 配置没有权限自定义处理类
                    .accessDeniedHandler(securityAccessDeniedHandler)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/userInfo").permitAll()
                    .and()
                    .rememberMe()
                    .rememberMeParameter("remember-me")
                    .rememberMeCookieName("remember-me-token")
                    .authenticationSuccessHandler(rememberMeAuthenticationSuccessHandler)
                    .tokenRepository(securityUserTokenService)
                    .key(securityProperty.getRememberKey())
                    .and()
                    .sessionManagement()
                    .sessionFixation()
                    .migrateSession()
                    // 在需要使用到session时才创建session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    // 同时登陆多个只保留一个
                    .maximumSessions(securityProperty.getMaximum())
                    .maxSessionsPreventsLogin(false)
                    // 踢出用户操作
                    .expiredSessionStrategy(securityExpiredSessionHandler)
                    // 用于统计在线
                    .sessionRegistry(sessionRegistry);
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.resourceId("uaa").tokenStore(tokenStore);
        }
    }
}
