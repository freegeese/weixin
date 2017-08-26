package com.github.freegeese.weixin.core.test.pay;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayApiCode {
    // API文档路径
    private String apiDocumentUrl;
    // API名称
    private String apiName;
    // API请求路径
    private String apiUrl;
    // 请求参数
    private List<PayParameter> inputParameters = new ArrayList<>();
    // 返回参数
    private List<PayParameter> outputParameters = new ArrayList<>();
    // 错误码
    private List<PayErrorCode> errorCodes = new ArrayList<>();

    // 文件命名
    private String inputParameterPrefix;
    private String inputParameterSuffix;
    private String outputParameterPrefix;
    private String outputParameterSuffix;
    private String errorCodePrefix;
    private String errorCodeSuffix;

    public String getInputParameterPrefix() {
        return (inputParameterPrefix == null) ? "" : inputParameterPrefix;
    }

    public String getInputParameterSuffix() {
        return (inputParameterSuffix == null) ? "" : inputParameterSuffix;
    }

    public String getOutputParameterPrefix() {
        return (outputParameterPrefix == null) ? "" : outputParameterPrefix;
    }

    public String getOutputParameterSuffix() {
        return (outputParameterSuffix == null) ? "" : outputParameterSuffix;
    }

    public String getErrorCodePrefix() {
        return (errorCodePrefix == null) ? "" : errorCodePrefix;
    }

    public String getErrorCodeSuffix() {
        return (errorCodeSuffix == null) ? "" : errorCodeSuffix;
    }

    public void generateAll(String pkg) throws IOException {
        File dir = new File("").getAbsoluteFile();
        File mavenSourceDir = new File(dir, "src/test/java");
        generateAll(mavenSourceDir.exists() ? mavenSourceDir : dir, pkg);
    }

    public void generateAll(File dir, String pkg) throws IOException {
        generateInputParameter(dir, pkg);
        generateOutputParameter(dir, pkg);
        // 特殊处理 BatchQueryComment
        if (apiName.endsWith("BatchQueryComment")) {
            for (PayErrorCode errorCode : errorCodes) {
                errorCode.setAnswer(errorCode.getReason());
                errorCode.setReason(errorCode.getDescription());
            }
        }
        // 特殊处理 DownloadBill
        if (apiName.endsWith("DownloadBill")) {
            for (PayErrorCode errorCode : errorCodes) {
                if (errorCode.getName().equals("invalid bill_type")) {
                    int index = errorCodes.indexOf(errorCode);
                    List<String> multiple = Arrays.asList("data format error", "missing parameter", "SIGN ERROR");
                    for (String name : multiple) {
                        PayErrorCode payErrorCode = JSON.parseObject(JSON.toJSONString(errorCode), PayErrorCode.class);
                        payErrorCode.setName(name);
                        errorCodes.add(++index, payErrorCode);
                    }
                    break;
                }
            }
        }
        generateErrorCode(dir, pkg);
    }

    public void generateErrorCode(File dir, String pkg) throws IOException {
        if (null == errorCodes || errorCodes.isEmpty()) {
            return;
        }
        // 名称空格处理 " " -> "_"
        for (PayErrorCode errorCode : errorCodes) {
            errorCode.setName(errorCode.getName().replaceAll("\\s+", "_"));
        }

        String className = errorCodePrefix + apiName + errorCodeSuffix;
        List<String> lines = new ArrayList<>();
        lines.add("package " + pkg + ";");
        lines.add("public enum  " + className + "{");
        int size = errorCodes.size();
        int index = 0;
        for (PayErrorCode errorCode : errorCodes) {
            index++;
            lines.add("\t" + errorCode.getName() + "(\"" + errorCode.getDescription() + "\", \"" + errorCode.getReason() + "\", \"" + errorCode.getAnswer() + "\")" + (index == size ? ";" : ",") + "");
        }
        Field[] fields = PayErrorCode.class.getDeclaredFields();
        Map<String, String> fieldNameAndType = new LinkedHashMap<>();
        ArrayList<String> constructorParameters = new ArrayList<>();

        for (int i = 1; i < fields.length; i++) {
            Field field = fields[i];
            String name = field.getName();
            String type = field.getType().getSimpleName();
            fieldNameAndType.put(name, type);
            constructorParameters.add(type + " " + name);
            lines.add("\tprivate " + type + " " + name + ";");
        }
        // 构造方法
        lines.add("\t" + className + "(" + StringUtils.join(constructorParameters, ", ") + ") {");
        for (Map.Entry<String, String> entry : fieldNameAndType.entrySet()) {
            lines.add("\t\tthis." + entry.getKey() + " = " + entry.getKey() + ";");
        }
        lines.add("\t}");

        // getter
        for (Map.Entry<String, String> entry : fieldNameAndType.entrySet()) {
            String name = entry.getKey();
            String type = entry.getValue();
            lines.add("\tpublic " + type + " get" + (name.substring(0, 1).toUpperCase() + name.substring(1)) + "() { return " + name + "; }");
        }
        lines.add("}");
        // write to file
        write(dir, pkg, className, lines);
    }

    public void generateInputParameter(File dir, String pkg) throws IOException {
        String className = inputParameterPrefix + apiName + inputParameterSuffix;
        generatePayParameter(dir, pkg, className, inputParameters);
    }

    public void generateOutputParameter(File dir, String pkg) throws IOException {
        String className = outputParameterPrefix + apiName + outputParameterSuffix;
        generatePayParameter(dir, pkg, className, outputParameters);
    }

    private void generatePayParameter(File dir, String pkg, String className, List<PayParameter> payParameters) throws IOException {
        if (null == payParameters || payParameters.isEmpty()) {
            return;
        }
        List<String> lines = new ArrayList<>();
        lines.add("package " + pkg + ";");
        lines.add("import com.thoughtworks.xstream.annotations.XStreamAlias;");
        lines.add("import lombok.Data;");
        lines.add("@Data");
        lines.add("@XStreamAlias(\"xml\")");
        lines.add("public class " + className + " {");
        for (PayParameter payParameter : payParameters) {
            String type = payParameter.getType();
            String field = payParameter.getField();
            type = type.startsWith("Int") ? "Integer" : "String";
            lines.add("\tprivate " + type + " " + field + ";");
        }
        lines.add("}");
        write(dir, pkg, className, lines);
    }

    private void write(File dir, String pkg, String className, List<String> lines) throws IOException {
        Path path = Paths.get(Paths.get(dir.getAbsolutePath(), pkg.split("\\.")).toFile().getAbsolutePath(), className + ".java");
        File parentFile = path.toFile().getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        Files.write(path, lines);
    }
}
