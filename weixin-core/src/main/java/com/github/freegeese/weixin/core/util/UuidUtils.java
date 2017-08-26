package com.github.freegeese.weixin.core.util;

import java.util.UUID;

public abstract class UuidUtils {

    private static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};


    /**
     * 32位UUID
     *
     * @return
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 8位UUID
     *
     * @return
     */
    public static String shortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = uuid();
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

    /**
     * 日期+8位UUID
     *
     * @return
     */
    public static String dateWithUuid() {
        return DateUtils.format("yyyyMMdd") + shortUuid();
    }

    /**
     * 日期时间+8位UUID
     *
     * @return
     */
    public static String datetimeWithUuid() {
        return DateUtils.format("yyyyMMddHHmmss") + shortUuid();
    }

}
