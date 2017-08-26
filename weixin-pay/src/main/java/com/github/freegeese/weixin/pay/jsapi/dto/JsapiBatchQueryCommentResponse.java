package com.github.freegeese.weixin.pay.jsapi.dto;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
@Data
@XStreamAlias("xml")
public class JsapiBatchQueryCommentResponse {
	private String return_code;
	private String return_msg;
	private String result_code;
	private String err_code;
	private String err_code_des;
}
