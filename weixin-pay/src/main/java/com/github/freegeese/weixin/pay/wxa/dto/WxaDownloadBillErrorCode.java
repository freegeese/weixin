package com.github.freegeese.weixin.pay.wxa.dto;
public enum  WxaDownloadBillErrorCode{
	SYSTEMERROR("接口返回错误", "系统超时", "请尝试再次查询。"),
	INVALID_TRANSACTIONID("无效transaction_id", "请求参数未按指引进行填写", "参数错误，请重新检查"),
	PARAM_ERROR("参数错误", "请求参数未按指引进行填写", "参数错误，请重新检查");
	private String description;
	private String reason;
	private String answer;
	WxaDownloadBillErrorCode(String description, String reason, String answer) {
		this.description = description;
		this.reason = reason;
		this.answer = answer;
	}
	public String getDescription() { return description; }
	public String getReason() { return reason; }
	public String getAnswer() { return answer; }
}
