package com.github.freegeese.weixin.core.dto;

import lombok.Data;

@Data
public class CreateShortUrlRequest {
    private String access_token;
    private String action = "long2short";
    private String long_url;
}
