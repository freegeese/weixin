package com.github.freegeese.weixin.pay.app.dto;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
@Data
@XStreamAlias("xml")
public class AppUnifiedOrderResponse {
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
}
