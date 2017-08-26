package com.github.freegeese.weixin.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCallbackIpRequest {
    private String access_token;
}
