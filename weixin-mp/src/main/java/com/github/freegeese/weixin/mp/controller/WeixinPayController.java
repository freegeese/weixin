package com.github.freegeese.weixin.mp.controller;

import com.alibaba.fastjson.JSON;
import com.github.freegeese.weixin.core.util.*;
import com.github.freegeese.weixin.mp.WeixinMpProperties;
import com.github.freegeese.weixin.mp.WeixinPayProperties;
import com.github.freegeese.weixin.mp.domain.WxPayResultNotify;
import com.github.freegeese.weixin.mp.domain.WxPayUnifiedOrderRequest;
import com.github.freegeese.weixin.mp.domain.WxPayUnifiedOrderResponse;
import com.github.freegeese.weixin.mp.dto.PayResultNotifyResponse;
import com.github.freegeese.weixin.mp.dto.Response;
import com.github.freegeese.weixin.mp.repository.WxPayResultNotifyRepository;
import com.github.freegeese.weixin.mp.repository.WxPayUnifiedOrderRequestRepository;
import com.github.freegeese.weixin.mp.repository.WxPayUnifiedOrderResponseRepository;
import com.github.freegeese.weixin.pay.PayCommonApi;
import com.github.freegeese.weixin.pay.native0.dto.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("weixin/pay")
public class WeixinPayController {
    @Autowired
    WeixinMpProperties mpProperties;

    @Autowired
    WeixinPayProperties payProperties;

    @Autowired
    WxPayUnifiedOrderRequestRepository unifiedOrderRequestRepository;
    @Autowired
    WxPayUnifiedOrderResponseRepository unifiedOrderResponseRepository;
    @Autowired
    WxPayResultNotifyRepository resultNotifyRepository;

    // https://api.mch.weixin.qq.com/pay/downloadbill
    @GetMapping(value = {"downloadbill/{date}"})
    @ResponseBody
    public Object downloadbill(@PathVariable("date") String date) {
        NativeDownloadBillRequest request = new NativeDownloadBillRequest();
        request.setAppid(mpProperties.getAppid());
        request.setMch_id(mpProperties.getMch_id());
        request.setNonce_str(UuidUtils.shortUuid());
        request.setSign_type("MD5");
        request.setBill_type("ALL");
        request.setBill_date(date);
        request.setSign(SignUtils.sign(request.getSign_type(), request, mpProperties.getMch_key()));

        NativeDownloadBillResponse response = PayCommonApi.downloadBill(request, NativeDownloadBillResponse.class);

        return Response.success().data(response);
    }


    @GetMapping(value = {"orderquery"})
    @ResponseBody
    public Object orderquery() {
        NativeOrderQueryRequest orderQueryRequest = new NativeOrderQueryRequest();
        orderQueryRequest.setAppid(mpProperties.getAppid());
        orderQueryRequest.setMch_id(mpProperties.getMch_id());
        /*
        4003252001201708268313633236
        4003252001201708268318671281
        4003252001201708268326646687
        4003252001201708268328646246
        4003252001201708268330677063
         */
        orderQueryRequest.setTransaction_id("4003252001201708268318671281");
        orderQueryRequest.setSign_type("MD5");
        orderQueryRequest.setNonce_str(UuidUtils.shortUuid());
        orderQueryRequest.setSign(SignUtils.sign(orderQueryRequest.getSign_type(), orderQueryRequest, mpProperties.getMch_key()));

        NativeOrderQueryResponse orderQueryResponse = PayCommonApi.orderQuery(orderQueryRequest, NativeOrderQueryResponse.class);
        System.out.println(JSON.toJSONString(orderQueryResponse, true));

        return Response.success().data(orderQueryRequest);
    }

    @GetMapping(value = {"qrcode"})
    @ResponseBody
    public Object getPayQRCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 生成预付单
        String remoteAddr = request.getRemoteAddr();
        // 本地请求，转换IP转换为127.0.0.1
        if ("0:0:0:0:0:0:0:1".equals(remoteAddr)) {
            remoteAddr = "127.0.0.1";
        }
        String tradeNo = UuidUtils.datetimeWithUuid();
        try {
            // 判断是否是已经处理过的交易记录


            // 生成预付单
            NativeUnifiedOrderRequest unifiedOrderRequest = new NativeUnifiedOrderRequest();
            unifiedOrderRequest.setAppid(mpProperties.getAppid());
            unifiedOrderRequest.setMch_id(mpProperties.getMch_id());
            unifiedOrderRequest.setSign_type("MD5");
            unifiedOrderRequest.setBody("扫码支付");
            unifiedOrderRequest.setNonce_str(UuidUtils.shortUuid());
            unifiedOrderRequest.setOut_trade_no(tradeNo);
            unifiedOrderRequest.setSpbill_create_ip(remoteAddr);
            unifiedOrderRequest.setNotify_url("http://wx.freegeese.com/weixin/pay/notify");
            unifiedOrderRequest.setTrade_type("NATIVE");
            unifiedOrderRequest.setProduct_id(UuidUtils.datetimeWithUuid());
            unifiedOrderRequest.setTotal_fee(1);
            unifiedOrderRequest.setSign(SignUtils.sign(unifiedOrderRequest.getSign_type(), unifiedOrderRequest, mpProperties.getMch_key()));
            NativeUnifiedOrderResponse unifiedOrderResponse = PayCommonApi.unifiedOrder(unifiedOrderRequest, NativeUnifiedOrderResponse.class);
            System.out.println(JSON.toJSONString(unifiedOrderResponse, true));

            // 判断生成预付单是否成功
            if (!"SUCCESS".equals(unifiedOrderResponse.getReturn_code())) {
                return Response.failure(unifiedOrderResponse.getReturn_msg());
            }
            if (!"SUCCESS".equals(unifiedOrderResponse.getResult_code())) {
                return Response.failure(unifiedOrderResponse.getErr_code_des());
            }

            // 保存预付单
            WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
            BeanUtils.copyProperties(unifiedOrderRequest, wxPayUnifiedOrderRequest);
            unifiedOrderRequestRepository.save(wxPayUnifiedOrderRequest);

            WxPayUnifiedOrderResponse wxPayUnifiedOrderResponse = new WxPayUnifiedOrderResponse();
            BeanUtils.copyProperties(unifiedOrderResponse, wxPayUnifiedOrderResponse);
            unifiedOrderResponseRepository.save(wxPayUnifiedOrderResponse);

            // 流形式返回
            response.setContentType("image/jpg");
            QRCodeUtils.write(response.getOutputStream(), unifiedOrderResponse.getCode_url());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure(e.getMessage()).data(String.format("根据交易流水号：%s，生成二维码失败", tradeNo));
        }
    }

    /**
     * 统一下单回调通知
     *
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("notify")
    @ResponseBody
    public Object payNotify(HttpServletRequest request) throws IOException {
        String notifyData = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
        WxPayResultNotify wxPayResultNotify = XStreamUtils.toObject(notifyData, WxPayResultNotify.class);
        // 返回状态码
        String returnCode = wxPayResultNotify.getReturn_code();
        if (!"SUCCESS".equals(returnCode)) {
            return PayResultNotifyResponse.failure(wxPayResultNotify.getReturn_msg());
        }

        // 已经支付成功，则不进行处理，返回支付成功
        WxPayResultNotify conditions = new WxPayResultNotify();
        conditions.setTransaction_id(wxPayResultNotify.getTransaction_id());
        conditions.setResult_code("SUCCESS");
        Example<WxPayResultNotify> example = Example.of(conditions);
        WxPayResultNotify successResultNotify = resultNotifyRepository.findOne(example);
        if (null != successResultNotify) {
            return PayResultNotifyResponse.success(String.format("已处理完成的支付记录：%s", successResultNotify.getTransaction_id()));
        }

        // 保存支付结果
        resultNotifyRepository.save(wxPayResultNotify);

        // 业务结果
        String resultCode = wxPayResultNotify.getResult_code();
        if (!"SUCCESS".equals(resultCode)) {
            String errCode = wxPayResultNotify.getErr_code();
            String errCodeDes = wxPayResultNotify.getErr_code_des();
            PayResultNotifyResponse.failure(String.format("支付失败，错误码：%s，错误描述：%s", errCode, errCodeDes));
        }
        return PayResultNotifyResponse.success();
    }

}
