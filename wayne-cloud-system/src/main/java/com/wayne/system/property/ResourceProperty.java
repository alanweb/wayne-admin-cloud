package com.wayne.system.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "wayne.resource",ignoreUnknownFields = false)
public class ResourceProperty {
    private String[] whites;
}
