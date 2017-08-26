package com.github.freegeese.weixin.pay.jsapi.dto;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
@Data
@XStreamAlias("xml")
public class JsapiDownloadBillRequest {
	private String appid;
	private String mch_id;
	private String device_info;
	private String nonce_str;
	private String sign;
	private String sign_type;
	private String bill_date;
	private String bill_type;
	private String tar_type;
}
