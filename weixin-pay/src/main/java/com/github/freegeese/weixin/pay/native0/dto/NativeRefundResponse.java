package com.github.freegeese.weixin.pay.native0.dto;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
@Data
@XStreamAlias("xml")
public class NativeRefundResponse {
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
	private String out_refund_no;
	private String refund_id;
	private Integer refund_fee;
	private Integer settlement_refund_fee;
	private Integer total_fee;
	private Integer settlement_total_fee;
	private String fee_type;
	private Integer cash_fee;
	private String cash_fee_type;
	private Integer cash_refund_fee;
	private String coupon_type_$n;
	private Integer coupon_refund_fee;
	private Integer coupon_refund_fee_$n;
	private Integer coupon_refund_count;
	private String coupon_refund_id_$n;
}
