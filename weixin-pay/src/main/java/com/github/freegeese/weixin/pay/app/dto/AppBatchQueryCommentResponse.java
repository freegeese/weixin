package com.github.freegeese.weixin.pay.app.dto;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
@Data
@XStreamAlias("xml")
public class AppBatchQueryCommentResponse {
	private String return_code;
	private String return_msg;
	private String result_code;
	private String err_code;
	private String err_code_des;
}
