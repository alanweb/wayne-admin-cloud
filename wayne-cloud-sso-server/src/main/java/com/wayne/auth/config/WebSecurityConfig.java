package com.wayne.auth.config;

import com.wayne.auth.extension.UnauthorizedAuthenticationEntryPoint;
import com.wayne.auth.service.impl.WayneUserDetailsService;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SpringSecurity配置
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
        implements InitializingBean {

    @Autowired
    private WayneUserDetailsService wayneUserDetailsService;
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    /**
     * 资源访问规则
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        HttpSecurity http = getHttp();
        if (null != http) {
            http.exceptionHandling().authenticationEntryPoint(new UnauthorizedAuthenticationEntryPoint());
        }
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            // 用于身份验证
            authenticationManagerBuilder
                    .userDetailsService(wayneUserDetailsService)
                    .passwordEncoder(passwordEncoder());
        } catch (Exception e) {
            throw new BeanInitializationException("Security configuration failed", e);
        }
    }
}