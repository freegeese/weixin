package com.github.freegeese.weixin.pay.native0.dto;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
@Data
@XStreamAlias("xml")
public class NativeRefundResultNotifyResponse {
	private String return_code;
	private String return_msg;
}
