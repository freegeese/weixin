package com.github.freegeese.weixin.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetQRCodeRequest {
    private String ticket;
}
