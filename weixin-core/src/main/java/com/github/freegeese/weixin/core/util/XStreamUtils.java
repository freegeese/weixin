package com.github.freegeese.weixin.core.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NameCoder;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;

import java.io.Writer;

/**
 * XML解析工具
 */
public abstract class XStreamUtils {

    private static HierarchicalStreamDriver driver;

    static {
        driver = new Xpp3Driver() {
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out, getNameCoder()) {
                    String CDATA_PREFIX = "<![CDATA[";
                    String CDATA_SUFFIX = "]]>";

                    @Override
                    protected void writeText(QuickWriter writer, String text) {
                        // 数字不需要使用CDATA
                        if (text.matches("[0-9]+")) {
                            writer.write(text);
                            return;
                        }
                        // 已经使用了CDATA
                        if (text.startsWith(CDATA_PREFIX) && text.endsWith(CDATA_SUFFIX)) {
                            writer.write(text);
                            return;
                        }
                        // 其他的
                        writer.write(CDATA_PREFIX + text + CDATA_SUFFIX);
                    }
                };
            }

            /**
             * 避免 _ -> ——
             *
             * @return
             */
            @Override
            protected NameCoder getNameCoder() {
                return new NoNameCoder();
            }
        };
    }

    /**
     * XML 转换为 Bean
     *
     * @param xml
     * @param bean
     */
    public static void toObject(String xml, Object bean) {
        init(bean).fromXML(xml, bean);
    }

    public static <T> T toObject(String xml, Class<T> clazz) {
        try {
            T t = clazz.newInstance();
            toObject(xml, t);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Bean 转换为 XML
     *
     * @param bean
     * @return
     */
    public static String toXml(Object bean) {
        return init(bean).toXML(bean);
    }

    private static XStream init(Object bean) {
        XStream xStream = new XStream(driver);
        xStream.autodetectAnnotations(true);
        Class<?> cls = bean.getClass();
        XStreamAlias alias = cls.getAnnotation(XStreamAlias.class);
        if (null != alias) {
            xStream.alias(alias.value(), cls);
        } else {
            Class<?> superclass = cls.getSuperclass();
            while (Object.class != superclass) {
                alias = superclass.getAnnotation(XStreamAlias.class);
                if (null != alias) {
                    xStream.alias(alias.value(), cls);
                    break;
                }
                superclass = superclass.getSuperclass();
            }
        }
        // 忽略未知的元素
        xStream.ignoreUnknownElements();
        return xStream;
    }
}
