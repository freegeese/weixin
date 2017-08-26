package com.github.freegeese.weixin.mp.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "wx_pay_unifiedorder_response")
public class WxPayUnifiedOrderResponse extends ResponseEntity<Long> {
    private String return_code;
    private String return_msg;
    private String appid;
    private String mch_id;
    private String device_info;
    private String nonce_str;
    private String sign;
    private String result_code;
    private String err_code;
    private String err_code_des;
    private String trade_type;
    private String prepay_id;
    private String code_url;
}
