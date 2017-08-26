package com.github.freegeese.weixin.core;

import com.github.freegeese.weixin.core.dto.*;
import com.github.freegeese.weixin.core.util.HttpUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 微信公众号开发基础API接口，提供一些共用的访问接口
 */
public abstract class WxCoreApi {

    // 获取 access_token
    private static final String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type={grant_type}&appid={appid}&secret={secret}";
    // 获取 ticket
    private static final String GET_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={access_token}&type=jsapi";
    // 获取微信服务器IP地址
    private static final String GET_CALLBACK_IP_URL = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token={access_token}";
    // 创建二维码 ticket
    private static final String CREATE_QRCODE_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token={access_token}";
    // 根据 ticket 换取二维码
    private static final String GET_QRCODE_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket={ticket}";
    // 长连接转短链接
    private static final String CREATE_SHORTURL_URL = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token={access_token}";

    // 有效时间（单位：分钟）
    private static final Integer EXPIRE_TIME = 120;
    // 缓存管理
    private static Cache<Object, Object> cache = null;

    // 初始化缓存管理
    static {
        cache = CacheBuilder.newBuilder()
                // 最大缓存数量
                .maximumSize(100)
                // write后的过期时间
                .expireAfterWrite(EXPIRE_TIME, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 获取访问令牌（Token）
     *
     * @param request
     * @return
     */
    public static GetTokenResponse getToken(GetTokenRequest request) {
        return getToken(request, false);
    }

    /**
     * 获取访问令牌（Token），参数create可指定是否获取一个新的访问令牌
     *
     * @param request
     * @param refresh
     * @return
     */
    public static GetTokenResponse getToken(GetTokenRequest request, boolean refresh) {
        return getFromCache(request, refresh, GET_TOKEN_URL, GetTokenResponse.class);
    }

    /**
     * 获取访问JSAPI临时票据（Ticket）
     *
     * @param request
     * @return
     */
    public static GetTicketResponse getTicket(GetTicketRequest request) {
        return getTicket(request, false);
    }

    /**
     * 获取访问JSAPI临时票据（Ticket），参数create可指定是否获取一个新的票据
     *
     * @param request
     * @param refresh
     * @return
     */
    public static GetTicketResponse getTicket(GetTicketRequest request, boolean refresh) {
        return getFromCache(request, refresh, GET_TICKET_URL, GetTicketResponse.class);
    }

    /**
     * 获取微信服务器IP地址
     *
     * @param access_token
     * @return
     */
    public static List<String> getCallbackIp(String access_token) {
        return getCallbackIp(new GetCallbackIpRequest(access_token)).getIp_list();
    }

    /**
     * 获取微信服务器IP地址
     *
     * @param request
     * @return
     */
    public static GetCallbackIpResponse getCallbackIp(GetCallbackIpRequest request) {
        return HttpUtils.get(GET_CALLBACK_IP_URL, request, GetCallbackIpResponse.class);
    }

    /**
     * 创建二维码 ticket
     *
     * @param request
     * @return
     */
    public static CreateQRCodeTicketResponse createQrcodeTicket(CreateQRCodeTicketRequest request) {
        return HttpUtils.postJson(CREATE_QRCODE_TICKET_URL, request, CreateQRCodeTicketResponse.class);
    }

    /**
     * 根据ticket显示二维码
     *
     * @param ticket
     * @return
     */
    public static byte[] getQRCode(String ticket) {
        return getQRCode(new GetQRCodeRequest(ticket));
    }

    /**
     * 根据ticket显示二维码
     *
     * @param request
     * @return
     */
    public static byte[] getQRCode(GetQRCodeRequest request) {
        return HttpUtils.getForBytes(GET_QRCODE_URL, request);
    }

    /**
     * 长连接转短链接
     *
     * @param request
     * @return
     */
    public static CreateShortUrlResponse createShortUrl(CreateShortUrlRequest request) {
        return HttpUtils.postJson(CREATE_SHORTURL_URL, request, CreateShortUrlResponse.class);
    }

    /**
     * 从缓存获取需要的数据
     *
     * @param key
     * @param refresh
     * @param url
     * @param resultType
     * @param <T>
     * @return
     */
    private static <T> T getFromCache(Object key, boolean refresh, String url, Class<T> resultType) {
        if (refresh) {
            cache.invalidate(key);
        }
        try {
            return (T) cache.get(key, new Callable<T>() {
                @Override
                public T call() throws Exception {
                    return HttpUtils.get(url, key, resultType);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
