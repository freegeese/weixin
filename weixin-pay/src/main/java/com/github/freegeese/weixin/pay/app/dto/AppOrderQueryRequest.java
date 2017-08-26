package com.github.freegeese.weixin.pay.app.dto;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
@Data
@XStreamAlias("xml")
public class AppOrderQueryRequest {
	private String appid;
	private String mch_id;
	private String transaction_id;
	private String out_trade_no;
	private String nonce_str;
	private String sign;
}
