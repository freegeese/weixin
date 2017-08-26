package com.github.freegeese.weixin.core.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

import java.security.MessageDigest;
import java.util.Map;
import java.util.TreeMap;

/**
 * 微信签名工具类
 */
public class SignUtils {

    public static String signWithMd2(Object parameters) {
        return sign(MessageDigestAlgorithms.MD2, parameters);
    }

    public static String signWithMd5(Object parameters) {
        return sign(MessageDigestAlgorithms.MD5, parameters);
    }

    public static String signWithMd2(Object parameters, String mchKey) {
        return sign(MessageDigestAlgorithms.MD2, parameters, mchKey);
    }

    public static String signWithMd5(Object parameters, String mchKey) {
        return sign(MessageDigestAlgorithms.MD5, parameters, mchKey);
    }

    public static String signWithSha1(Object parameters) {
        return sign(MessageDigestAlgorithms.SHA_1, parameters);
    }

    public static String signWithSha1(Object parameters, String mchKey) {
        return sign(MessageDigestAlgorithms.SHA_1, parameters, mchKey);
    }

    public static String signWithSha256(Object parameters) {
        return sign(MessageDigestAlgorithms.SHA_256, parameters);
    }

    public static String signWithSha256(Object parameters, String mchKey) {
        return sign(MessageDigestAlgorithms.SHA_256, parameters, mchKey);
    }

    public static String signWithSha384(Object parameters) {
        return sign(MessageDigestAlgorithms.SHA_384, parameters);
    }

    public static String signWithSha384(Object parameters, String mchKey) {
        return sign(MessageDigestAlgorithms.SHA_384, parameters, mchKey);
    }

    public static String signWithSha512(Object parameters) {
        return sign(MessageDigestAlgorithms.SHA_512, parameters);
    }

    public static String signWithSha512(Object parameters, String mchKey) {
        return sign(MessageDigestAlgorithms.SHA_512, parameters, mchKey);
    }

    /**
     * 签名
     *
     * @param algorithms
     * @param parameters
     * @return
     */
    public static String sign(String algorithms, Object parameters) {
        if (Map.class.isAssignableFrom(parameters.getClass())) {
            return sign(algorithms, (Map) parameters);
        }
        return sign(algorithms, JSON.parseObject(JSON.toJSONString(parameters)));
    }

    /**
     * 签名
     *
     * @param algorithms
     * @param parameters
     * @param mchKey
     * @return
     */
    public static String sign(String algorithms, Object parameters, String mchKey) {
        if (Map.class.isAssignableFrom(parameters.getClass())) {
            return sign(algorithms, (Map) parameters, mchKey);
        }
        return sign(algorithms, JSON.parseObject(JSON.toJSONString(parameters)), mchKey);
    }

    /**
     * 签名
     *
     * @param algorithms
     * @param parameters
     * @return
     */
    private static String sign(String algorithms, Map<String, Object> parameters) {
        MessageDigest digest = DigestUtils.getDigest(algorithms);
        String signParameters = buildSign(parameters);
        return Hex.encodeHexString(digest.digest(StringUtils.getBytesUtf8(buildSign(parameters))));
    }

    /**
     * 签名
     *
     * @param algorithms
     * @param parameters
     * @param mchKey
     * @return
     */
    private static String sign(String algorithms, Map<String, Object> parameters, String mchKey) {
        MessageDigest digest = DigestUtils.getDigest(algorithms);
        String signParameters = buildSign(parameters, mchKey);
        System.out.println("签名参数：" + signParameters);
        return Hex.encodeHexString(digest.digest(StringUtils.getBytesUtf8(signParameters)));
    }

    /**
     * 构建签名字符串
     *
     * @param parameters
     * @return
     */
    private static String buildSign(Map<String, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Map<String, Object> filteredParameters = filterParameters(parameters);
        for (Map.Entry<String, Object> entry : filteredParameters.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 构建签名字符串
     *
     * @param parameters
     * @param mchKey
     * @return
     */
    private static String buildSign(Map<String, Object> parameters, String mchKey) {
        return buildSign(parameters) + "&key=" + mchKey;
    }

    /**
     * 使用TreeMap对key进行ASCII码排序
     *
     * @param parameters
     * @return
     */
    private static Map<String, Object> filterParameters(Map<String, Object> parameters) {
        TreeMap<String, Object> treeMap = new TreeMap<>(parameters);
        for (Map.Entry<String, Object> entry : treeMap.entrySet()) {
            if (null == entry.getValue()) {
                treeMap.remove(entry.getKey());
            }
        }
        return treeMap;
    }


}
