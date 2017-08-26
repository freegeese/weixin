package com.github.freegeese.weixin.pay.common;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付对账单
 */
@Data
public class PayBill {
    private List<LinkedHashMap> details;
    private Map totals;
}
