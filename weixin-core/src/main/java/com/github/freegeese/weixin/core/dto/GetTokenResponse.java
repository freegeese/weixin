package com.github.freegeese.weixin.core.dto;

import lombok.Data;

@Data
public class GetTokenResponse extends Response {
    private String access_token;
    private Integer expires_in;
}
