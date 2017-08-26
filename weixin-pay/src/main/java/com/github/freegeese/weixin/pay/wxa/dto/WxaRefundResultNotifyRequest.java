package com.github.freegeese.weixin.pay.wxa.dto;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
@Data
@XStreamAlias("xml")
public class WxaRefundResultNotifyRequest {
	private String return_code;
	private String return_msg;
	private String appid;
	private String mch_id;
	private String nonce_str;
	private String req_info;
	private String transaction_id;
	private String out_trade_no;
	private String refund_id;
	private String out_refund_no;
	private Integer total_fee;
	private Integer settlement_total_fee;
	private Integer refund_fee;
	private Integer settlement_refund_fee;
	private String refund_status;
	private String success_time;
	private String refund_recv_accout;
	private String refund_account;
	private String refund_request_source;
}
