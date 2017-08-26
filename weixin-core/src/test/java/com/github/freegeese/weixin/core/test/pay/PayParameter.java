package com.github.freegeese.weixin.core.test.pay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayParameter {
    // 字段名	    变量名	必填	类型	示例值	描述
    private String label;
    private String field;
    private String required;
    private String type;
    private String example;
    private String description;
}
