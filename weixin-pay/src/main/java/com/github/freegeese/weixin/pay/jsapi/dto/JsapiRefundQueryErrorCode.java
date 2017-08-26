package com.github.freegeese.weixin.pay.jsapi.dto;
public enum  JsapiRefundQueryErrorCode{
	SYSTEMERROR("接口返回错误", "系统超时", "请尝试再次掉调用API。"),
	REFUNDNOTEXIST("退款订单查询失败", "订单号错误或订单状态不正确", "请检查订单号是否有误以及订单状态是否正确，如：未支付、已支付未退款"),
	INVALID_TRANSACTIONID("无效transaction_id", "请求参数未按指引进行填写", "请求参数错误，检查原交易号是否存在或发起支付交易接口返回失败"),
	PARAM_ERROR("参数错误", "请求参数未按指引进行填写", "请求参数错误，请检查参数再调用退款申请"),
	APPID_NOT_EXIST("APPID不存在", "参数中缺少APPID", "请检查APPID是否正确"),
	MCHID_NOT_EXIST("MCHID不存在", "参数中缺少MCHID", "请检查MCHID是否正确"),
	REQUIRE_POST_METHOD("请使用post方法", "未使用post传递参数 ", "请检查请求参数是否通过post方法提交"),
	SIGNERROR("签名错误", "参数签名结果不正确", "请检查签名参数和方法是否都符合签名算法要求"),
	XML_FORMAT_ERROR("XML格式错误", "XML格式错误", "请检查XML参数格式是否正确");
	private String description;
	private String reason;
	private String answer;
	JsapiRefundQueryErrorCode(String description, String reason, String answer) {
		this.description = description;
		this.reason = reason;
		this.answer = answer;
	}
	public String getDescription() { return description; }
	public String getReason() { return reason; }
	public String getAnswer() { return answer; }
}
