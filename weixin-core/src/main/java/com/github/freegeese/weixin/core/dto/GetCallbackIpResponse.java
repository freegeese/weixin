package com.github.freegeese.weixin.core.dto;

import lombok.Data;

import java.util.List;

@Data
public class GetCallbackIpResponse extends Response {
    private List<String> ip_list;
}
