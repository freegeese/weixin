package com.github.freegeese.weixin.core.dto;

import lombok.Data;

@Data
public class CreateShortUrlResponse extends Response {
    private String short_url;
}
