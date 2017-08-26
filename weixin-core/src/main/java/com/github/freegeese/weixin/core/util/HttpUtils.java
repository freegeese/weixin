package com.github.freegeese.weixin.core.util;

import com.alibaba.fastjson.JSON;
import okhttp3.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public abstract class HttpUtils {
    private static OkHttpClient client = new OkHttpClient();
    private static String HTTP_METHOD_GET = "GET";
    private static String HTTP_METHOD_POST = "POST";
    private static String CONTENT_TYPE_JSON = "application/json";
    private static String CONTENT_TYPE_XML = "text/xml";

    /**
     * GET请求
     *
     * @param url
     * @param resultType
     * @param <T>
     * @return
     */
    public static <T> T get(String url, Class<T> resultType) {
        return get(url, null, resultType);
    }

    /**
     * GET请求
     *
     * @param url
     * @param data
     * @param resultType
     * @param <T>
     * @return
     */
    public static <T> T get(String url, Object data, Class<T> resultType) {
        return get(url, null, data, resultType);
    }

    /**
     * GET请求
     *
     * @param url
     * @param heads
     * @param data
     * @param resultType
     * @param <T>
     * @return
     */
    public static <T> T get(String url, Map heads, Object data, Class<T> resultType) {
        return parseResult(getForString(url, heads, data), resultType);
    }

    /**
     * GET请求
     *
     * @param url
     * @param data
     * @return
     */
    public static String getForString(String url, Object data) {
        return getForString(url, null, data);
    }

    /**
     * GET请求
     *
     * @param url
     * @param data
     * @return
     */
    public static byte[] getForBytes(String url, Object data) {
        return getForBytes(url, null, data);
    }

    /**
     * GET请求
     *
     * @param url
     * @param heads
     * @param data
     * @return
     */
    public static String getForString(String url, Map heads, Object data) {
        return getString(url, HTTP_METHOD_GET, null, heads, data);
    }

    /**
     * GET请求
     *
     * @param url
     * @param heads
     * @param data
     * @return
     */
    public static byte[] getForBytes(String url, Map heads, Object data) {
        return getBytes(url, HTTP_METHOD_GET, null, heads, data);
    }

    /**
     * POST请求，请求体：JSON
     *
     * @param url
     * @param resultType
     * @param <T>
     * @return
     */
    public static <T> T postJson(String url, Class<T> resultType) {
        return postJson(url, null, resultType);
    }

    /**
     * POST请求，请求体：JSON
     *
     * @param url
     * @param data
     * @param resultType
     * @param <T>
     * @return
     */
    public static <T> T postJson(String url, Object data, Class<T> resultType) {
        return postJson(url, null, data, resultType);
    }

    /**
     * POST请求，请求体：JSON
     *
     * @param url
     * @param heads
     * @param data
     * @param resultType
     * @param <T>
     * @return
     */
    public static <T> T postJson(String url, Map heads, Object data, Class<T> resultType) {
        return parseResult(postForString(url, CONTENT_TYPE_JSON, heads, data), resultType);
    }

    /**
     * POST请求，请求体：XML
     *
     * @param url
     * @param resultType
     * @param <T>
     * @return
     */
    public static <T> T postXml(String url, Class<T> resultType) {
        return postXml(url, null, resultType);
    }

    /**
     * POST请求，请求体：XML
     *
     * @param url
     * @param data
     * @param resultType
     * @param <T>
     * @return
     */
    public static <T> T postXml(String url, Object data, Class<T> resultType) {
        return postXml(url, null, data, resultType);
    }

    /**
     * POST请求，请求体：XML
     *
     * @param url
     * @param heads
     * @param data
     * @param resultType
     * @param <T>
     * @return
     */
    public static <T> T postXml(String url, Map heads, Object data, Class<T> resultType) {
        return parseResult(postForString(url, CONTENT_TYPE_XML, heads, data), resultType);
    }


    public static String postXmlForString(String url, Object data) {
        return postXmlForString(url, null, data);
    }

    public static String postXmlForString(String url, Map heads, Object data) {
        return postForString(url, CONTENT_TYPE_XML, heads, data);
    }

    public static byte[] postXmlForBytes(String url, Object data) {
        return postXmlForBytes(url, null, data);
    }

    public static byte[] postXmlForBytes(String url, Map heads, Object data) {
        return postForBytes(url, CONTENT_TYPE_XML, heads, data);
    }

    private static String postForString(String url, String contentType, Map heads, Object data) {
        return getString(url, HTTP_METHOD_POST, contentType, heads, data);
    }

    private static byte[] postForBytes(String url, String contentType, Map heads, Object data) {
        return getBytes(url, HTTP_METHOD_POST, contentType, heads, data);
    }


    private static <T> T parseResult(String result, Class<T> resultType) {
        if (null == result) {
            return null;
        }
        result = result.trim();
        if (result.startsWith("<")) {
            return XStreamUtils.toObject(result, resultType);
        }
        if (result.startsWith("{")) {
            return JSON.parseObject(result, resultType);
        }
        throw new RuntimeException("不支持的解析的数据：" + result);
    }


    private static byte[] getBytes(String url, String method, String contentType, Map heads, Object data) {
        try {
            return getBody(url, method, contentType, heads, data).bytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getString(String url, String method, String contentType, Map heads, Object data) {
        try {
            return getBody(url, method, contentType, heads, data).string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ResponseBody getBody(String url, String method, String contentType, Map heads, Object data) {
        return execute(url, method, contentType, heads, data).body();
    }

    private static Response execute(String url, String method, String contentType, Map heads, Object data) {
        // 解析URL
        url = parseUrlParameters(url, data);

        // 创建HTTPRequest对象
        okhttp3.Request.Builder builder = new Request.Builder().url(url);
        // 设置请求头
        if (null != heads && !heads.isEmpty()) {
            for (Object key : heads.keySet()) {
                builder.addHeader(key.toString(), heads.get(key).toString());
            }
        }

        // 设置请求体
        if (!HTTP_METHOD_GET.equalsIgnoreCase(method)) {
            MediaType mediaType = MediaType.parse(contentType);
            String body;
            if (CharSequence.class.isAssignableFrom(data.getClass())) {
                body = data.toString();
            } else {
                String subtype = mediaType.subtype();
                if ("json".equalsIgnoreCase(subtype)) {
                    // JSON 数据
                    body = JSON.toJSONString(data);
                } else if ("xml".equalsIgnoreCase(subtype)) {
                    // XML 数据
                    body = XStreamUtils.toXml(data);
                } else {
                    throw new RuntimeException(String.format("不支持的请求体数据格式：'%s'", data));
                }
            }
            builder.method(method, RequestBody.create(mediaType, body));
        }
        // 发送请求
        try {
            return client.newCall(builder.build()).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String parseUrlParameters(String url, Object data) {
        if (null == data) {
            return url;
        }
        if (!url.matches(".*\\{[^{}]+}.*")) {
            return url;
        }
        Class<?> dataClass = data.getClass();
        if (CharSequence.class.isAssignableFrom(dataClass)) {
            return url;
        }
        try {
            Map urlParameters = Map.class.isAssignableFrom(dataClass) ? (Map) data : (Map) JSON.toJSON(data);
            if (urlParameters.isEmpty()) {
                return url;
            }
            Iterator keyIter = urlParameters.keySet().iterator();
            while (keyIter.hasNext()) {
                Object key = keyIter.next();
                url = url.replace("{" + key + "}", String.valueOf(urlParameters.get(key)));
            }
            return url;
        } catch (Exception e) {
            throw new RuntimeException(String.format("解析URL：'%s' 参数发生异常，参数值：'%s'", url, dataClass), e);
        }
    }

}
