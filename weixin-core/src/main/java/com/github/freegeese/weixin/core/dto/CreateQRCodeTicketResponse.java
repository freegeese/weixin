package com.github.freegeese.weixin.core.dto;

import lombok.Data;

@Data
public class CreateQRCodeTicketResponse extends Response {
    private String ticket;
    private Integer expire_seconds;
    private String url;
}
