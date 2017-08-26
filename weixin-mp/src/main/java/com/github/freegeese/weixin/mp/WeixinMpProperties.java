package com.github.freegeese.weixin.mp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "weixin.mp")
public class WeixinMpProperties {
    private boolean server;
    private String appid;
    private String secret;
    private String mch_id;
    private String mch_key;
    private final Session session = new Session();

    @Data
    public static class Session {
        private String host;
        private String url;
        private String token;
        private String encodingAESKey;
    }
}
