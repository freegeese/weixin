package com.github.freegeese.weixin.pay.app.dto;
public enum  AppOrderQueryErrorCode{
	ORDERNOTEXIST("此交易订单号不存在", "查询系统中不存在此交易订单号", "该API只能查提交支付交易返回成功的订单，请商户检查需要查询的订单号是否正确"),
	SYSTEMERROR("系统错误", "后台系统返回错误", "系统异常，请再调用发起查询");
	private String description;
	private String reason;
	private String answer;
	AppOrderQueryErrorCode(String description, String reason, String answer) {
		this.description = description;
		this.reason = reason;
		this.answer = answer;
	}
	public String getDescription() { return description; }
	public String getReason() { return reason; }
	public String getAnswer() { return answer; }
}
