package com.github.freegeese.weixin.mp.controller;

import com.github.freegeese.weixin.mp.WeixinMpProperties;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Controller
@RequestMapping("${weixin.mp.session.url}")
public class WeixinSessionController {
    @Autowired
    private WeixinMpProperties mpProperties;


    /**
     * 基础配置认证
     *
     * @param verifyRequest
     * @param response
     * @throws IOException
     */
    @GetMapping
    public void verify(VerifyRequest verifyRequest, HttpServletResponse response) throws IOException {
        verifyRequest.setToken(mpProperties.getSession().getToken());
        PrintWriter writer = response.getWriter();
        boolean signature = verifySignature(verifyRequest);
        writer.write(signature ? verifyRequest.getEchostr() : "Verification signature failed");
        writer.close();
    }

    /**
     * 接受微信发送过来的消息
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @PostMapping
    public void messageProccess(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String message = IOUtils.toString(request.getReader());
//        String result = messageProcessor.process(message);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        PrintWriter writer = response.getWriter();
        writer.write("");
        writer.flush();
    }

    /**
     * 签名校验
     *
     * @param verifyRequest
     * @return
     */
    private static boolean verifySignature(VerifyRequest verifyRequest) {
        String token = verifyRequest.getToken();
        String timestamp = verifyRequest.getTimestamp();
        String nonce = verifyRequest.getNonce();
        String[] verifyContent = {token, timestamp, nonce};
        Arrays.sort(verifyContent);
        StringBuilder sb = new StringBuilder();
        for (String s : verifyContent) {
            sb.append(s);
        }
        return DigestUtils.sha1Hex(sb.toString()).equals(verifyRequest.getSignature());
    }

    @Data
    private static class VerifyRequest {
        /**
         * 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
         */
        private String signature;
        // 时间戳
        private String timestamp;
        // 随机数
        private String nonce;
        // 	随机字符串
        private String echostr;
        // 开发者填写的token参数
        private String token;
    }

}
