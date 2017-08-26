package com.github.freegeese.weixin.core.test;

import com.github.freegeese.weixin.core.WxCoreApi;
import com.github.freegeese.weixin.core.dto.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class WxCoreApiTests {
    private static String appid = "wxd0e2322f4bb60e49";
    private static String secret = "978c5d0b76ff84a8c526a62197773281";

    @Test
    public void test1() throws Exception {
        GetTokenRequest tokenRequest = new GetTokenRequest(appid, secret);
        GetTokenResponse tokenResponse = WxCoreApi.getToken(tokenRequest);

        GetTicketRequest ticketRequest = new GetTicketRequest(tokenResponse.getAccess_token());
        GetTicketResponse ticketResponse = WxCoreApi.getTicket(ticketRequest);
        System.out.println(ticketResponse.getTicket());
    }

    @Test
    public void test2() throws Exception {
        GetTokenRequest request = new GetTokenRequest(appid, secret);

        HashSet<String> tokens = new HashSet<>();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    String access_token = WxCoreApi.getToken(request).getAccess_token();
                    tokens.add(access_token);
                    System.out.println(access_token);
                }
            }
        };

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            threads.add(new Thread(runnable));
        }
        for (Thread thread : threads) {
            thread.start();
        }

        Thread.sleep(2000);
        Assert.assertEquals(tokens.toArray().length, 1);
    }

    @Test
    public void test3() throws Exception {
        List<String> callbackIp = WxCoreApi.getCallbackIp(getToken());
        System.out.println(callbackIp);
    }

    @Test
    public void test4() throws Exception {
        String long_url = "http://www.51mocai.com/steelbatch/index.jhtml";
        CreateShortUrlRequest request = new CreateShortUrlRequest();
        request.setLong_url(long_url);
        request.setAccess_token(getToken());
        CreateShortUrlResponse response = WxCoreApi.createShortUrl(request);
        System.out.println(response.getShort_url());
    }

    @Test
    public void test5() throws Exception {
        CreateQRCodeTicketRequest request = CreateQRCodeTicketRequest.newInstance(true, true);
        request.setAccess_token(getToken());
        request.setSceneStr("123456");
        request.setExpire_seconds(600000);
        CreateQRCodeTicketResponse response = WxCoreApi.createQrcodeTicket(request);
        System.out.println(response.getTicket());
    }

    private String getToken() {
        GetTokenRequest tokenRequest = new GetTokenRequest();
        tokenRequest.setAppid(appid);
        tokenRequest.setSecret(secret);
        GetTokenResponse tokenResponse = WxCoreApi.getToken(tokenRequest);
        return tokenResponse.getAccess_token();
    }
}
