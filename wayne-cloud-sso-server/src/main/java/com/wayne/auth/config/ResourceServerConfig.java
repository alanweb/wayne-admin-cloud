package com.wayne.auth.config;

import com.wayne.auth.extension.UnauthorizedAuthenticationEntryPoint;
import com.wayne.auth.process.*;
import com.wayne.auth.property.SecurityProperties;
import com.wayne.auth.support.SecureCaptchaSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;

@EnableResourceServer
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Resource
    private TokenStore tokenStore;
    @Resource
    private CorsFilter corsFilter;
    @Value("${wayne-account.remember-me.token-max-age:604800}")
    private int tokenValiditySeconds;
    @Resource
    private PersistentTokenRepository persistentTokenRepository;
    @Resource
    private SecureRememberMeHandler rememberMeAuthenticationSuccessHandler;
    /**
     * 配置不拦截url
     */
    @Resource
    private SecurityProperties securityProperties;

    @Resource
    private SecureAuthenticationSuccessHandler secureAuthenticationSuccessHandler;
    @Resource
    private SecureAuthenticationFailureHandler secureAuthenticationFailureHandler;
    @Resource
    private SecureCaptchaSupport secureCaptchaSupport;

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
        SimpleUrlLogoutSuccessHandler simpleUrlLogoutSuccessHandler =
                new SimpleUrlLogoutSuccessHandler();
        simpleUrlLogoutSuccessHandler.setUseReferer(true);
        simpleUrlLogoutSuccessHandler.setTargetUrlParameter("service");
        http.addFilter(corsFilter)
                .authorizeRequests()
                .antMatchers(securityProperties.getWhites()).permitAll()
                // 其他的需要登录后才能访问
                .anyRequest().authenticated()
                .and()
                // 验证码验证类
                .addFilterBefore(secureCaptchaSupport, UsernamePasswordAuthenticationFilter.class)
                .httpBasic()
                .authenticationEntryPoint(new UnauthorizedAuthenticationEntryPoint())
                .and()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .successHandler(secureAuthenticationSuccessHandler)
                //登录失败
                .failureHandler(secureAuthenticationFailureHandler)
                .and()
                .logout()
                .addLogoutHandler(new SecureLogoutHandler())
                .logoutSuccessHandler(simpleUrlLogoutSuccessHandler)
                .deleteCookies("refresh_token", "access_token", "JSESSIONID_YY")
                .and()
                .rememberMe()
                .key(securityProperties.getRememberKey())
                .tokenRepository(persistentTokenRepository)
                .tokenValiditySeconds(tokenValiditySeconds)
                .authenticationSuccessHandler(rememberMeAuthenticationSuccessHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("uaa").tokenStore(tokenStore);
    }
}
