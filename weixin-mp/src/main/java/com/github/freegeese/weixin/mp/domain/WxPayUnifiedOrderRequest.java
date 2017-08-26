package com.github.freegeese.weixin.mp.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "wx_pay_unifiedorder_request")
public class WxPayUnifiedOrderRequest extends BaseEntity<Long> {
    private String appid;
    private String mch_id;
    private String device_info;
    private String nonce_str;
    private String sign;
    private String sign_type;
    private String body;
    private String detail;
    private String attach;
    private String out_trade_no;
    private String fee_type;
    private Integer total_fee;
    private String spbill_create_ip;
    private String time_start;
    private String time_expire;
    private String goods_tag;
    private String notify_url;
    private String trade_type;
    private String product_id;
    private String limit_pay;
    private String openid;
    private String scene_info;
}
