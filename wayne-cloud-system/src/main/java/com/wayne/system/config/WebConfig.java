package com.wayne.system.config;

import cn.hutool.extra.mail.MailAccount;
import com.wayne.common.constant.ConfigurationConstant;
import com.wayne.common.plugin.system.service.SysContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @Author bin.wei
 * @Date 2021/6/16
 * @Description
 */
@Configuration
public class WebConfig {
    @Resource
    private SysContext sysContext;

    @Bean
    public MailAccount mailAccount() {
        MailAccount mailAccount = new MailAccount();
        mailAccount.setHost(sysContext.getConfig(ConfigurationConstant.MAIN_HOST));
        mailAccount.setPort(sysContext.getConfig(ConfigurationConstant.MAIN_PORT)==""?0000: Integer.parseInt(sysContext.getConfig(ConfigurationConstant.MAIN_PORT)));
        mailAccount.setFrom(sysContext.getConfig(ConfigurationConstant.MAIN_FROM));
        mailAccount.setUser(sysContext.getConfig(ConfigurationConstant.MAIN_USER));
        mailAccount.setPass(sysContext.getConfig(ConfigurationConstant.MAIN_PASS));
        mailAccount.setCharset(StandardCharsets.UTF_8);
        mailAccount.setAuth(true);
        return mailAccount;
    }
}
