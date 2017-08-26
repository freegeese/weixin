package com.github.freegeese.weixin.pay.app.dto;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
@Data
@XStreamAlias("xml")
public class AppRefundQueryResponse {
	private String return_code;
	private String return_msg;
	private String result_code;
	private String err_code;
	private String err_code_des;
	private String appid;
	private String mch_id;
	private String nonce_str;
	private String sign;
	private String transaction_id;
	private String out_trade_no;
	private Integer total_fee;
	private String fee_type;
	private Integer cash_fee;
	private String cash_fee_type;
	private Integer settlement_total_fee;
	private Integer refund_count;
	private String out_refund_no_$n;
	private String refund_id_$n;
	private String refund_channel_$n;
	private Integer refund_fee_$n;
	private Integer coupon_refund_fee_$n;
	private Integer coupon_refund_count_$n;
	private String coupon_refund_id_$n_$m;
	private String coupon_type_$n;
	private Integer coupon_refund_fee_$n_$m;
	private String refund_status_$n;
	private String refund_account_$n;
	private String refund_recv_accout_$n;
	private String refund_success_time_$n;
}
