package com.wayne.common.config;

import com.wayne.common.config.proprety.SwaggerProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * Describe: 接 口 文 档 配 置 文 件
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 * */
@Slf4j
@Configuration
@EnableSwagger2
@ConditionalOnClass(Contact.class)
@EnableConfigurationProperties(SwaggerProperty.class)
public class SwaggerConfig {

    @Resource
    private SwaggerProperty properties;

    @Bean
    public Docket docker(){
        log.info("Read document configuration information");
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName(properties.getGroupName())
                .enable(properties.getEnable())
                .select()
                .apis(RequestHandlerSelectors.basePackage(properties.getScanPackage()))
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfo(
                properties.getTitle(),
                properties.getDescribe() ,
                properties.getVersion(),
                properties.getTermsOfServiceUrl(),properties.getContact(),properties.getLicence(),properties.getLicenceUrl(),
                new ArrayList<>()
        );
    }
}