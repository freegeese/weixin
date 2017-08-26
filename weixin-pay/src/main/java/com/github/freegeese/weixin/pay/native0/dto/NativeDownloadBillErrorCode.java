package com.github.freegeese.weixin.pay.native0.dto;
public enum  NativeDownloadBillErrorCode{
	SYSTEMERROR("下载失败", "系统超时", "请尝试再次查询。"),
	invalid_bill_type("参数错误", "请求参数未按指引进行填写", "参数错误，请重新检查"),
	data_format_error("参数错误", "请求参数未按指引进行填写", "参数错误，请重新检查"),
	missing_parameter("参数错误", "请求参数未按指引进行填写", "参数错误，请重新检查"),
	SIGN_ERROR("参数错误", "请求参数未按指引进行填写", "参数错误，请重新检查"),
	NO_Bill_Exist("账单不存在", "当前商户号没有已成交的订单，不生成对账单", "请检查当前商户号在指定日期内是否有成功的交易。"),
	Bill_Creating("账单未生成", "当前商户号没有已成交的订单或对账单尚未生成", "请先检查当前商户号在指定日期内是否有成功的交易，如指定日期有交易则表示账单正在生成中，请在上午10点以后再下载。"),
	CompressGZip_Error("账单压缩失败", "账单压缩失败，请稍后重试", "账单压缩失败，请稍后重试"),
	UnCompressGZip_Error("账单解压失败", "账单解压失败，请稍后重试", "账单解压失败，请稍后重试");
	private String description;
	private String reason;
	private String answer;
	NativeDownloadBillErrorCode(String description, String reason, String answer) {
		this.description = description;
		this.reason = reason;
		this.answer = answer;
	}
	public String getDescription() { return description; }
	public String getReason() { return reason; }
	public String getAnswer() { return answer; }
}
