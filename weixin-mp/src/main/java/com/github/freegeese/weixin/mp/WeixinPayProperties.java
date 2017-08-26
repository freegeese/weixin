package com.github.freegeese.weixin.mp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "weixin.pay")
public class WeixinPayProperties {
    private String host;
    private String jsapiPath;
    private String jsapiUrl;
    private String jsapiNotify;

    private String nativePath;
    private String nativeUrl;
    private String nativeNotify;
}
