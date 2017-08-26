package com.github.freegeese.weixin.core.test.pay;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 支付代码生成工具
 */
public abstract class PayCodeGenerator {
    private static Map<String, Object> settings;

    static {
        try {
            settings = JSON.parseObject(IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("pay-api-code-generator-settings.json"), Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 代码生成接口
     *
     * @throws Exception
     */
    public static void generate() throws Exception {
        generate(settings);
    }

    /**
     * 代码生成接口
     *
     * @param settings
     * @throws Exception
     */
    public static void generate(Map<String, Object> settings) throws Exception {
        String basePackage = settings.get("basePackage").toString();
        Object subPackage = settings.get("subPackage");
        Object outputDirectory = settings.get("outputDirectory");
        File dir = new File(null == outputDirectory ? "" : outputDirectory.toString()).getAbsoluteFile();

        Map<String, String> payDocumentUrlMap = new TreeMap<>((Map) settings.get("payDocumentUrl"));
        Map<String, String> payCommonApi = new TreeMap<>((Map) settings.get("payCommonApi"));
        for (Map.Entry<String, String> entry : payDocumentUrlMap.entrySet()) {
            String payType = entry.getKey();
            String payDocumentUrl = entry.getValue();
            String payApiNamePrefix = payType.substring(0, 1).toUpperCase() + payType.substring(1);
            System.out.println(payType + "->" + payDocumentUrl);
            Map<String, String> payApiUrls = getPayApiUrls(payDocumentUrl, payCommonApi);
            for (Map.Entry<String, String> payApiUrlEntry : payApiUrls.entrySet()) {
                String payApiName = payApiNamePrefix + payApiUrlEntry.getKey();
                String payApiDocumentUrl = payApiUrlEntry.getValue();
                System.out.println("\t" + payApiName + " -> " + payApiDocumentUrl);
                PayApiCode payApiCode = getPayApiCode(payApiDocumentUrl);
                payApiCode.setApiName(payApiName);

                // 合并配置参数
                Map map = (Map) JSON.toJSON(payApiCode);
                map.putAll(new HashMap(settings));
                payApiCode = JSON.parseObject(JSON.toJSONString(map), PayApiCode.class);

                String pkg = basePackage + "." + ("native".equals(payType) ? payType.concat("0") : payType);
                pkg = (null == subPackage) ? pkg : pkg + "." + subPackage;
                payApiCode.generateAll(dir, pkg);
            }
        }
    }

    public static void getCommonParameters() throws Exception {
        Map<String, String> payDocumentUrlMap = new TreeMap<>((Map) settings.get("payDocumentUrl"));
        Map<String, String> payCommonApi = new TreeMap<>((Map) settings.get("payCommonApi"));
        Set<String> commonsInputParameters = new TreeSet<>();
        Set<String> commonsOutputParameters = new TreeSet<>();
        for (Map.Entry<String, String> entry : payDocumentUrlMap.entrySet()) {
            String payType = entry.getKey();
            String payDocumentUrl = entry.getValue();
            System.out.println(payType + "->" + payDocumentUrl);

            Map<String, String> payApiUrls = getPayApiUrls(payDocumentUrl, payCommonApi);
            for (Map.Entry<String, String> payApiUrlEntry : payApiUrls.entrySet()) {
                String apiName = payApiUrlEntry.getKey();
                if (apiName.endsWith("Notify")) {
                    continue;
                }
                String apiDocumentUrl = payApiUrlEntry.getValue();
                PayApiCode payApiCode = getPayApiCode(apiDocumentUrl);
                Set<String> parameters = new HashSet<>();
                for (PayParameter payParameter : payApiCode.getInputParameters()) {
                    parameters.add(payParameter.getField());
                }
                if (parameters.contains("sign_type")) {
                    System.out.println("\t" + apiName + " -> " + apiDocumentUrl);
                }
                if (commonsInputParameters.isEmpty()) {
                    commonsInputParameters.addAll(parameters);
                } else {
                    commonsInputParameters.retainAll(parameters);
                }

                parameters = new HashSet<>();
                for (PayParameter payParameter : payApiCode.getOutputParameters()) {
                    parameters.add(payParameter.getField());
                }
                if (commonsOutputParameters.isEmpty()) {
                    commonsOutputParameters.addAll(parameters);
                } else {
                    commonsOutputParameters.retainAll(parameters);
                }
            }
        }
        System.out.println("+++++++++++公共输入参数：++++++++++++");
        System.out.println(StringUtils.join(commonsInputParameters, System.lineSeparator()));
        System.out.println("+++++++++++公共输出参数：++++++++++++");
        System.out.println(StringUtils.join(commonsOutputParameters, System.lineSeparator()));
    }

    public static void getApiUrls() throws Exception {
        Set<String> apiUrls = new HashSet<>();
        Map<String, String> payDocumentUrlMap = new TreeMap<>((Map) settings.get("payDocumentUrl"));
        Map<String, String> payCommonApi = new TreeMap<>((Map) settings.get("payCommonApi"));
        for (Map.Entry<String, String> entry : payDocumentUrlMap.entrySet()) {
            String payDocumentUrl = entry.getValue();
            Map<String, String> payApiUrls = getPayApiUrls(payDocumentUrl, payCommonApi);
            for (Map.Entry<String, String> payApiUrlEntry : payApiUrls.entrySet()) {
                String payApiDocumentUrl = payApiUrlEntry.getValue();
                Elements elements = Jsoup.connect(payApiDocumentUrl).get().getElementsContainingOwnText("https://api.mch.weixin.qq.com");
                if (!elements.isEmpty()) {
                    String apiUrl = elements.get(0).text().trim();
                    apiUrl = apiUrl.substring(apiUrl.indexOf("https"), apiUrl.length());
                    apiUrls.add(apiUrl);
                }
            }
        }
        System.out.println(apiUrls.size());
        for (String apiUrl : apiUrls) {
            String[] items = apiUrl.split("/");
            String urlField = items[items.length - 1].toUpperCase() + "_URL";
            System.out.println("String " + urlField + " = " + ("\"" + apiUrl + "\"") + ";");
        }
    }

    /**
     * 根据接口文档地址，生成接口参数代码
     *
     * @param payApiDocumentUrl
     * @return
     * @throws Exception
     */
    public static PayApiCode getPayApiCode(String payApiDocumentUrl) throws Exception {
        PayApiCode code = new PayApiCode();
        code.setApiDocumentUrl(payApiDocumentUrl);

        Document doc = Jsoup.connect(code.getApiDocumentUrl()).get();
        // 请求API
        Elements inputParameters = doc.getElementsContainingOwnText("请求参数");
        if (inputParameters.size() == 0) {
            inputParameters = doc.getElementsContainingOwnText("输入参数");
        }

        if (inputParameters.size() > 0) {
            Element inputParametersParent = inputParameters.get(0).parent().parent();
            code.setInputParameters(parse(filterRows(inputParametersParent), PayParameter.class));

            Element outputParametersParent = inputParametersParent.nextElementSibling();
            code.setOutputParameters(parse(filterRows(outputParametersParent), PayParameter.class));

            Element errorCodeParametersParent = outputParametersParent.nextElementSibling();
            code.setErrorCodes(parse(filterRows(errorCodeParametersParent), PayErrorCode.class));
            return code;
        }
        // 通知API
        Elements notifyInputParameters = doc.getElementsContainingOwnText("通知参数");
        if (notifyInputParameters.size() > 0) {
            Element notifyInputParametersParent = notifyInputParameters.get(0).parent().parent();
            code.setInputParameters(parse(filterRows(notifyInputParametersParent), PayParameter.class));

            Element notifyOutputParametersParent = notifyInputParametersParent.nextElementSibling();
            code.setOutputParameters(parse(filterRows(notifyOutputParametersParent), PayParameter.class));
            return code;
        }

        throw new RuntimeException("不支持的参数解析(没有找到：请求参数 或者 通知参数)：" + payApiDocumentUrl);
    }

    private static Map<String, String> getPayApiUrls(String payDocumentUrl, Map<String, String> apiLabelAndName) throws IOException {
        String urlPrefix = payDocumentUrl.substring(0, payDocumentUrl.indexOf("?"));
        Map<String, String> apiNameAndUrl = new TreeMap<>();

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
        return apiNameAndUrl;
    }

    private static Elements filterRows(Element element) {
        Elements elements = element.select("table tbody tr:not(.a1)");
        // 需要被删除的行
        List<Element> removeRows = new ArrayList<>();
        List<Element> headRows = new ArrayList<>();
        for (Element ele : elements) {
            // 表头行
            if (ele.select("th").size() > 0) {
                headRows.add(ele);
                continue;
            }
            Elements rowspanCols = ele.select("td[rowspan]");
            if (rowspanCols.size() > 0) {
                // TODO 还没有找到好的方案来处理rowspan这种表格，暂时直接删除
                Integer rowspan = Integer.valueOf(rowspanCols.get(0).attr("rowspan"));
                Element nextRow = null;
                for (int i = 0; i < rowspan - 1; i++) {
                    nextRow = (null == nextRow) ? ele.nextElementSibling() : nextRow.nextElementSibling();
                    removeRows.add(nextRow);
                }
            }
        }
        // 删除表头行
        if (!headRows.isEmpty()) {
            elements.removeAll(headRows);
        }

        if (!removeRows.isEmpty()) {
            // 对多选以进行处理
            if (!element.getElementsMatchingOwnText(".选一").isEmpty()) {
                // 对删除行进行处理
                for (Element removeRow : removeRows) {
                    removeRow.insertChildren(5, new Element("td").text("是"), new TextNode(" ", ""));
                }
                return elements;
            }
            elements.removeAll(removeRows);
        }
        return elements;
    }

    private static <T> List<T> parse(Elements rows, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        List<T> results = new ArrayList<>();
        for (Element row : rows) {
            T t = parse(row, clazz);
            if (null != t) {
                results.add(t);
            }
        }
        return results;
    }

    private static <T> T parse(Element row, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        Elements cols = row.select("td");
        if (cols.isEmpty()) {
            return null;
        }
        T t = clazz.newInstance();
        List<String> fieldValues = cols.eachText();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fieldValues.size(); i++) {
            Field field = fields[i];
            field.setAccessible(true);
            field.set(t, fieldValues.get(i).trim());
            field.setAccessible(false);
        }
        return t;
    }

}
