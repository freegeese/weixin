package com.github.freegeese.weixin.pay.wxa.dto;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
@Data
@XStreamAlias("xml")
public class WxaReportResponse {
	private String return_code;
	private String return_msg;
	private String result_code;
}
