package com.github.freegeese.weixin.pay.native0.dto;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
@Data
@XStreamAlias("xml")
public class NativeBatchQueryCommentRequest {
	private String appid;
	private String mch_id;
	private String nonce_str;
	private String sign;
	private String sign_type;
	private String begin_time;
	private String end_time;
	private String offset;
	private String limit;
}
