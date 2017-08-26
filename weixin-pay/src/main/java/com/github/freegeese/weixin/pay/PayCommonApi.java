package com.github.freegeese.weixin.pay;

import com.alibaba.fastjson.JSON;
import com.github.freegeese.weixin.core.util.HttpUtils;
import com.github.freegeese.weixin.core.util.XStreamUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 公共的支付接口实现
 */
public abstract class PayCommonApi {

    public static final String REFUNDQUERY_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
    public static final String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    public static final String CLOSEORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
    public static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    public static final String REPORT_URL = "https://api.mch.weixin.qq.com/payitil/report";
    public static final String BATCHQUERYCOMMENT_URL = "https://api.mch.weixin.qq.com/billcommentsp/batchquerycomment";
    public static final String DOWNLOADBILL_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";
    public static final String ORDERQUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

    /**
     * 公共输入参数
     */
    private static enum CommonInputParameter {
        appid,      // required
        mch_id,     // required
        nonce_str,  // required
        sign,       // optional
        sign_type,  // optional
    }

    /**
     * 公共输出参数
     */
    private static enum CommonOutputParameter {
        return_code,    // required
        return_msg,     // required
        err_code,       // optional
        err_code_des    // optional
    }

    /**
     * 统一下单
     *
     * @param input
     * @param outputClass
     * @param <I>
     * @param <O>
     * @return
     */
    public static <I, O> O unifiedOrder(I input, Class<O> outputClass) {
        return sendRequest(UNIFIEDORDER_URL, input, outputClass);
    }

    /**
     * 订单查询
     *
     * @param input
     * @param outputClass
     * @param <I>
     * @param <O>
     * @return
     */
    public static <I, O> O orderQuery(I input, Class<O> outputClass) {
        return sendRequest(ORDERQUERY_URL, input, outputClass);
    }

    public static <I, O> O closeOrder(I input, Class<O> outputClass) {
        return sendRequest(CLOSEORDER_URL, input, outputClass);
    }

    public static <I, O> O refund(I input, Class<O> outputClass) {
        return sendRequest(REFUND_URL, input, outputClass);
    }

    public static <I, O> O refundQuery(I input, Class<O> outputClass) {
        return sendRequest(REFUNDQUERY_URL, input, outputClass);
    }

    /**
     * 下载对账单
     *
     * @param input
     * @param outputClass
     * @param <I>
     * @param <O>
     * @return
     */
    public static <I, O> O downloadBill(I input, Class<O> outputClass) {
        String data = HttpUtils.postXmlForString(DOWNLOADBILL_URL, input).trim();
        if (data.startsWith("<")) {
            return XStreamUtils.toObject(data, outputClass);
        }
        String[] lines = data.split(System.lineSeparator());
        // 交易明细表头
        String[] detailsHeads = lines[0].split(",");
        // 交易明细内容
        List<Map> details = new ArrayList<>();
        for (int i = 1; i < lines.length - 2; i++) {
            Map detail = new LinkedHashMap();
            String[] items = lines[i].replace("`", "").split(",");
            for (int j = 0; j < detailsHeads.length; j++) {
                detail.put(detailsHeads[j], items[j]);
            }
            details.add(detail);
        }

        // 交易合计表头
        String[] totalHeads = lines[lines.length - 2].split(",");
        // 交易合计内容
        String[] totalCotnent = lines[lines.length - 1].replace("`", "").split(",");
        Map totals = new LinkedHashMap();
        for (int i = 0; i < totalHeads.length; i++) {
            totals.put(totalHeads[i], totalCotnent[i]);
        }

        Map response = new LinkedHashMap<>();
        response.put("details", details);
        response.put("totals", totals);
        return JSON.parseObject(JSON.toJSONString(response), outputClass);
    }

    public static <I, O> O report(I input, Class<O> outputClass) {
        return sendRequest(REPORT_URL, input, outputClass);
    }

    public static <I, O> O batchQueryComment(I input, Class<O> outputClass) {
        return sendRequest(BATCHQUERYCOMMENT_URL, input, outputClass);
    }

    /**
     * 统一发送post请求
     *
     * @param url
     * @param input
     * @return
     */
    private static <I, O> O sendRequest(String url, I input, Class<O> outputClass) {
        // TODO 错误检查（输入、输出参数类型不匹配）
        Class<?> inputClass = input.getClass();
        // 执行请求
        O output = HttpUtils.postXml(url, input, outputClass);
        return output;
    }
}
