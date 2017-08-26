package com.github.freegeese.weixin.pay.wxa.dto;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
@Data
@XStreamAlias("xml")
public class WxaRefundRequest {
	private String appid;
	private String mch_id;
	private String device_info;
	private String nonce_str;
	private String sign;
	private String sign_type;
	private String transaction_id;
	private String out_trade_no;
	private String out_refund_no;
	private Integer total_fee;
	private Integer refund_fee;
	private String refund_fee_type;
	private String op_user_id;
	private String refund_account;
}
