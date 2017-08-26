package com.github.freegeese.weixin.pay.jsapi.dto;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
@Data
@XStreamAlias("xml")
public class JsapiPayResultNotifyResponse {
	private String return_code;
	private String return_msg;
}
