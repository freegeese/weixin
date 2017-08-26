package com.github.freegeese.weixin.core.dto;

import lombok.Data;

@Data
public class GetTicketResponse extends Response {
    private String ticket;
    private Integer expires_in;
}
