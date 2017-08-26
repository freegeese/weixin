package com.github.freegeese.weixin.core.test.pay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayErrorCode {
    //  名称	描述	原因	解决方案
    private String name;
    private String description;
    private String reason;
    private String answer;
}
