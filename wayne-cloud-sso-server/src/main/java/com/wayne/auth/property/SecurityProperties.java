package com.wayne.auth.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * security 的配置信息
 *
 * @author liuyongtao
 * @create 2019-04-30 9:08
 */
@Data
@ConfigurationProperties(prefix = "wayne.security", ignoreUnknownFields = false)
public class SecurityProperties {

    private final KeyStore keyStore = new KeyStore();
    private final WebClientConfiguration webClientConfiguration = new WebClientConfiguration();
    // 网关白名单配置
    private String[] whites;
    /**
     * 记住密码标识
     */
    private String rememberKey;

    /**
     * 是否允许多账号在线
     */
    private Integer maximum = 1;

    @Data
    public static class KeyStore {
        // name of the keystore in the classpath
        private String name = "tls/wayne.jks";
        // password used to access the key
        private String password = "waynepwd";
        // password used to access the key
        private String secret = "wayneks";
        // name of the alias to fetch
        private String alias = "wayne";
    }

    @Data
    public static class WebClientConfiguration {
        // validity of the short-lived access token in secs (min: 60), don't make it too long
        private int accessTokenValidityInSeconds = 5 * 60;
        // validity of the refresh token in secs (defines the duration of "remember me")
        private int refreshTokenValidityInSecondsForRememberMe = 7 * 24 * 60 * 60;
        private String clientId = "web_app";
        private String secret = "changeit";
    }
}
