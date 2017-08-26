package com.github.freegeese.weixin.pay.native0.dto;
public enum  NativeBatchQueryCommentErrorCode{
	SYSTEMERROR("微信支付内部错误", "微信支付内部错误", "请稍后重试"),
	NO_COMMENT("对应的时间段没有用户的评论数据", "对应的时间段没有用户的评论数据", "请查询其他时间段的评论数据"),
	TIME_EXPIRE("拉取的时间超过3个月", "拉取的时间超过3个月", "请拉取90天内的数据"),
	PARAM_ERROR("参数错误", "参数错误", "请对照文档的请求参数说明检查参数");
	private String description;
	private String reason;
	private String answer;
	NativeBatchQueryCommentErrorCode(String description, String reason, String answer) {
		this.description = description;
		this.reason = reason;
		this.answer = answer;
	}
	public String getDescription() { return description; }
	public String getReason() { return reason; }
	public String getAnswer() { return answer; }
}
