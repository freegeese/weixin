package com.github.freegeese.weixin.pay.native0.dto;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
@Data
@XStreamAlias("xml")
public class NativeCloseOrderRequest {
	private String appid;
	private String mch_id;
	private String out_trade_no;
	private String nonce_str;
	private String sign;
	private String sign_type;
}
