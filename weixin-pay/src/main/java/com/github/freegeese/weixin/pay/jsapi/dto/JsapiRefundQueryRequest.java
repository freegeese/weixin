package com.github.freegeese.weixin.pay.jsapi.dto;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
@Data
@XStreamAlias("xml")
public class JsapiRefundQueryRequest {
	private String appid;
	private String mch_id;
	private String nonce_str;
	private String sign;
	private String sign_type;
	private String transaction_id;
	private String out_trade_no;
	private String out_refund_no;
	private String refund_id;
}
