package com.ecare.smartmeal.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * NeighborhoodLongevity
 * <p>
 * Created by xuminmin on 12/21/21.
 * Email: iminminxu@gmail.com
 */
public class NumUtils {

    /**
     * 防止异常的parseInt
     *
     * @param s 转换的字符串
     * @return int值
     */
    public static int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 防止异常的parseDouble
     *
     * @param s 转换的字符串
     * @return double值
     */
    public static double parseDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 解析金额
     *
     * @param amount 金额
     * @return 金额字符串(￥0.00)
     */
    public static String parseAmount(BigDecimal amount) {
        if (amount == null) {
            return "¥0.00";
        }
        return "¥" + new DecimalFormat("0.00").format(amount);
    }

    /**
     * 解析打印的金额
     *
     * @param amount 金额
     * @return 金额字符串(0. # #)
     */
    public static String parsePrintAmount(BigDecimal amount) {
        if (amount == null) {
            return "0";
        }
        return new DecimalFormat("0.##").format(amount);
    }
}
