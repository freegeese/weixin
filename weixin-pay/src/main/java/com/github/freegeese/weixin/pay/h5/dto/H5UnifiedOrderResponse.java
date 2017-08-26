package com.github.freegeese.weixin.pay.h5.dto;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
@Data
@XStreamAlias("xml")
public class H5UnifiedOrderResponse {
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
	private String mweb_url;
}
