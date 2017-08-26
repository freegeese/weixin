package com.github.freegeese.weixin.core.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;

/**
 * 日期工具类
 */
public abstract class DateUtils {
    /**
     * 解析日期字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static Date parse(String date, String pattern) {
        return DateTime.parse(date, DateTimeFormat.forPattern(pattern)).toDate();
    }

    /**
     * 解析日期字符串
     *
     * @param date
     * @return
     */
    public static Date parse(String date) {
        String dateNumbers = date.replaceAll("[^0-9]", "");
        String pattern = "yyyyMMddHHmmssSSS".substring(0, dateNumbers.length());
        return parse(dateNumbers, pattern);
    }

    /**
     * 格式化日期对象
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        return new DateTime(date).toString(pattern);
    }

    /**
     * 格式化当前日期对象
     *
     * @param pattern
     * @return
     */
    public static String format(String pattern) {
        return format(new Date(), pattern);
    }

    /**
     * 格式化当前日期
     * yyyy-MM-dd
     *
     * @return
     */
    public static String formatYmd() {
        return format("yyyy-MM-dd");
    }

    /**
     * 格式化当前时间
     * HH:mm:ss
     *
     * @return
     */
    public static String formatHms() {
        return format("HH:mm:ss");
    }

    /**
     * 格式化当前日期+时间
     * yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String formatYmdHms() {
        return format("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 格式化当前日期时间戳
     * yyyyMMddHHmmssSSS
     *
     * @return
     */
    public static String formatYmdHmsSSS() {
        return format("yyyyMMddHHmmssSSS");
    }
}


