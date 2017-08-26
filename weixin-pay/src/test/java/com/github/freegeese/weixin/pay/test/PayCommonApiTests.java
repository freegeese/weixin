package com.github.freegeese.weixin.pay.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.freegeese.weixin.pay.native0.dto.NativeUnifiedOrderRequest;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PayCommonApiTests {

    @Test
    public void test1() throws Exception {
        NativeUnifiedOrderRequest request = new NativeUnifiedOrderRequest();
        String json = JSON.toJSONString(request, SerializerFeature.WRITE_MAP_NULL_FEATURES, SerializerFeature.PrettyFormat);
        System.out.println(json);
    }

    @Test
    public void test2() throws Exception {
        List<String> payTypes = Arrays.asList("h5", "jsapi", "native", "wxa", "app");
        String basePackage = "com.github.freegeese.weixin.pay.{payType}.dto";
        String names = null;
        for (String payType : payTypes) {
            String pkg = basePackage.replace("{payType}", payType);
            pkg = pkg.replace("native", "native0");
            // AppRefundResultNotifyRequest PayResultNotifyRequest
            String className = pkg + "." + payType.substring(0, 1).toUpperCase() + payType.substring(1) + "RefundResultNotifyRequest";
//            String className = pkg + "." + payType.substring(0, 1).toUpperCase() + payType.substring(1) + "PayResultNotifyResponse";
            Class<?> cls = Class.forName(className);
            Field[] fields = cls.getDeclaredFields();
            Set<String> fieldNames = new TreeSet<>();
            System.out.println("+++++++++" + cls + "+++++++++");
            for (Field field : fields) {
                fieldNames.add(field.getName());
            }
            String apiParameters = StringUtils.join(fieldNames.toArray(), System.lineSeparator());
            if (null == names) {
                names = apiParameters;
            } else if (names.equals(apiParameters)) {
                System.out.println(names);
            }
        }
    }
}
