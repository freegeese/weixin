package com.github.freegeese.weixin.pay.app.dto;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
@Data
@XStreamAlias("xml")
public class AppPayResultNotifyRequest {
	private String return_code;
	private String return_msg;
	private String appid;
	private String mch_id;
	private String device_info;
	private String nonce_str;
	private String sign;
	private String result_code;
	private String err_code;
	private String err_code_des;
	private String openid;
	private String is_subscribe;
	private String trade_type;
	private String bank_type;
	private Integer total_fee;
	private String fee_type;
	private Integer cash_fee;
	private String cash_fee_type;
	private Integer coupon_fee;
	private Integer coupon_count;
	private String coupon_id_$n;
	private Integer coupon_fee_$n;
	private String transaction_id;
	private String out_trade_no;
	private String attach;
	private String time_end;
}
