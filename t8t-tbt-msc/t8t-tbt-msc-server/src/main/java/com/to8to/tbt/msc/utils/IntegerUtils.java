package com.to8to.tbt.msc.utils;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.regex.Pattern;

/**
 * @author juntao.guo
 */
public class IntegerUtils {

    private static Pattern pattern = Pattern.compile("^[-\\+]?[\\d]+$");

    /**
     * 检查值的合法性-是否大于等于限定值
     *
     * @param value
     * @param minValue
     * @return
     */
    public static Boolean isMinValue(Integer value, Integer minValue) {
        return value != null && value >= minValue;
    }

    /**
     * 检查值的合法性-是否大于等于限定值
     *
     * @param value
     * @return
     */
    public static Boolean isMinValue(Integer value) {
        return isMinValue(value, 0);
    }

    /**
     * 检查值的合法性-是否大于限定值
     *
     * @param value
     * @return
     */
    public static Boolean isGtLimitValue(Integer value) {
        return value instanceof Integer && value.intValue() > 0;
    }

    /**
     * 检查值的合法性-是否大于限定值
     *
     * @param value
     * @return
     */
    public static Boolean isGtLimitValue(Long value) {
        return value != null && value.intValue() > 0;
    }

    /**
     * 检查值的合法性-是否等于限定值
     *
     * @param value
     * @return
     */
    public static Boolean isEqLimitValue(Integer value){
        return value == null || value.intValue() == 0;
    }

    /**
     * 检查值的合法性-是否等于限定值
     *
     * @param value
     * @return
     */
    public static Boolean isEqLimitValue(Integer value, Integer targetValue){
        return value != null && value.intValue() == targetValue.intValue();
    }

    /**
     * 转换为基础类型并设置默认值
     *
     * @param value
     * @return
     */
    public static int intValueAsDefault(Integer value) {
        if (value instanceof Integer && value.intValue() > 0) {
            return value.intValue();
        }
        return 0;
    }

    /**
     * 转换为基础类型并设置默认值
     *
     * @param value
     * @return
     */
    public static int intValueAsDefault(String value) {
        if (value instanceof String) {
            return Integer.valueOf(value).intValue();
        }
        return 0;
    }

    /**
     * 转换为基础类型并设置默认值
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static int intValueAsDefault(Integer value, int defaultValue) {
        if (value instanceof Integer && value.intValue() > 0) {
            return value.intValue();
        }
        return defaultValue;
    }

    /**
     * 将Long转换为基础类型
     *
     * @param value
     * @return
     */
    public static int intValuefromLong(Long value) {
        if (value instanceof Long) {
            return value.intValue();
        }
        return 0;
    }

    /**
     * 判断字符是否为数字
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        return pattern.matcher(str).matches();
    }
}
