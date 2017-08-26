package com.github.freegeese.weixin.core.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.freegeese.weixin.core.test.pay.PayApiCode;
import com.github.freegeese.weixin.core.test.pay.PayCodeGenerator;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JsoupTests {

    @Test
    public void test1() throws Exception {
        Document doc = Jsoup.connect("https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_2").get();
        Elements elements = doc.select("table");
        for (int i = 0; i < elements.size() - 1; i++) {
            if (i == 0) {
                System.out.println("+++++++++++++++++++++++请求参数+++++++++++++++++++++++");
            } else if (i == 1) {
                System.out.println("+++++++++++++++++++++++响应参数+++++++++++++++++++++++");
            }

            Element element = elements.get(i);
            Elements properties = element.select("tbody tr td:eq(1)");
            Elements types = element.select("tbody tr td:eq(3)");
            for (int j = 0; j < properties.size(); j++) {
                String property = properties.get(j).text().trim();
                String type = types.get(j).text().trim();
                if (type.startsWith("String")) {
                    type = "String";
                } else if (type.startsWith("Int")) {
                    type = "Integer";
                } else {
                    type = "String";
                }
                System.out.println(type + " " + property);
            }
        }
        // 错误吗
        Elements errcodeRows = elements.get(elements.size() - 1).select("table tbody tr");
        for (Element errcodeRow : errcodeRows) {
            Elements cols = errcodeRow.select("td");
            List<String> errCodes = new ArrayList<>();
            for (Element col : cols) {
                errCodes.add(col.text().trim());
            }
            System.out.println(StringUtils.join(errCodes, ","));
        }
    }

    @Test
    public void test2() throws Exception {
        // https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_1
        String apiDocumentUrl = "https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7";
        PayApiCode code = PayCodeGenerator.getPayApiCode(apiDocumentUrl);
        code.setApiName("Notify");
        File dir = new File("src/test/java");
        String pkg = "com.github.freegeese.core.test.pay";
        code.generateInputParameter(dir, pkg);
        code.generateOutputParameter(dir, pkg);
        code.generateErrorCode(dir, pkg);
    }

    @Test
    public void test3() throws Exception {
        List<String> apiLabels = Arrays.asList("统一下单", "查询订单", "关闭订单", "申请退款", "查询退款", "下载对账单", "支付结果通知", "交易保障", "退款结果通知", "拉取订单评价数据");
        List<String> apiNames = Arrays.asList("unifiedorder", "orderquery", "closeorder", "refund", "refundquery", "downloadbill", "payresultnotify", "report", "refundresultnotify", "batchquerycomment");
        Map<String, String> apiLabelAndName = new LinkedHashMap<>();
        for (int i = 0; i < apiLabels.size(); i++) {
            apiLabelAndName.put(apiLabels.get(i), apiNames.get(i));
        }
        String path = "https://pay.weixin.qq.com/wiki/doc/api/jsapi.php";
        String url = path + "?chapter=7_1";
        Document doc = Jsoup.connect(url).get();
        Element apiParent = doc.getElementsContainingOwnText("API列表").get(0);
        Elements apis = apiParent.parent().siblingElements().select("a");
        for (Element api : apis) {
            String apiLabel = api.text().trim();
            if (apiLabelAndName.containsKey(apiLabel)) {
                String href = api.attr("href").trim();
                String apiName = apiLabelAndName.get(apiLabel);

            }
        }
    }

    @Test
    public void test4() throws Exception {
        List<String> payDocumentUrls = Arrays.asList(
                "https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=7_1",
                "https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=6_1",
                "https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_1",
                "https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=15_1",
                "https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_3&index=1"
        );

        Map<Integer, List<String>> map = new LinkedHashMap<>();
        for (String payDocumentUrl : payDocumentUrls) {
            List<String> payApiUrls = getPayApiUrls(payDocumentUrl);
            int index = 0;
            for (String url : payApiUrls) {
                index++;
                List<String> fields = Jsoup.connect(url).get().select("table tbody td:eq(1)").eachText();
                String fieldsStr = StringUtils.join(fields);
                if (map.containsKey(index)) {
                    map.get(index).add(fieldsStr);
                } else {
                    List<String> fieldsList = new ArrayList<>();
                    fieldsList.add(fieldsStr);
                    map.put(index, fieldsList);
                }
            }
        }
        for (Map.Entry<Integer, List<String>> entry : map.entrySet()) {
            System.out.println("+++++++++++++" + entry.getKey() + "++++++++++++");
            for (String s : entry.getValue()) {
                System.out.println(s);
            }
        }
    }

    // 获取支付接口文档URL列表
    private List<String> getPayApiUrls(String payDocumentUrl) throws IOException {
        String urlPrefix = payDocumentUrl.substring(0, payDocumentUrl.indexOf("?"));
        Map<String, String> apiNameAndUrl = new TreeMap<>();

        List<String> apiLabels = Arrays.asList("统一下单", "查询订单", "关闭订单", "申请退款", "查询退款", "下载对账单", "支付结果通知", "交易保障", "退款结果通知", "拉取订单评价数据");
        List<String> apiNames = Arrays.asList("unifiedorder", "orderquery", "closeorder", "refund", "refundquery", "downloadbill", "payresultnotify", "report", "refundresultnotify", "batchquerycomment");
        Map<String, String> apiLabelAndName = new LinkedHashMap<>();
        for (int i = 0; i < apiLabels.size(); i++) {
            apiLabelAndName.put(apiLabels.get(i), String.valueOf((char) (i + 97)) + apiNames.get(i));
        }

        Document doc = Jsoup.connect(payDocumentUrl).get();
        Element apiParent = doc.getElementsContainingOwnText("API列表").get(0);
        Elements apis = apiParent.parent().siblingElements().select("a");

        for (Element api : apis) {
            String apiLabel = api.text().trim();
            if (apiLabelAndName.containsKey(apiLabel)) {
                String href = api.attr("href").trim();
                apiNameAndUrl.put(apiLabelAndName.get(apiLabel), urlPrefix + href);
            }
        }
        return new ArrayList<>(apiNameAndUrl.values());
    }

    @Test
    public void test6() throws Exception {
        String json = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("pay-api-code-generator-settings.json"));
        JSONObject data = JSON.parseObject(json);
        for (Map.Entry<String, Object> entry : data.entrySet()) {

        }
    }
}
