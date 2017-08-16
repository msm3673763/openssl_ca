package com.ucsmy.ucas.ca.utils;

import java.util.Date;
import java.util.UUID;

/**
 * 工具类
 *
 * @author zhengfucheng
 * @version 1.0.0 2017-04-27
 */
public class CaHelper {

    private static final String DASH = "-";
    private static final String BLANK = "";

    /**
     * 生成 32 位的 UUID，去除了破折号
     * @return
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace(DASH, BLANK);
    }


    /**
     * 随机生成 6 位数字
     * @return
     */
    public static String generateRandomPassword() {
        int number = (int)((Math.random()*9+1)*100000);
        return String.valueOf(number);
    }

}
