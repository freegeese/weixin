package com.github.freegeese.weixin.pay.app.dto;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
@Data
@XStreamAlias("xml")
public class AppCloseOrderResponse {
	private String return_code;
	private String return_msg;
	private String appid;
	private String mch_id;
	private String nonce_str;
	private String sign;
	private String err_code;
	private String err_code_des;
}
