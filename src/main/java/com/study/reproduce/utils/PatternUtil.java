package com.study.reproduce.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验字符串是否符合规范
 */
public class PatternUtil {

    /**
     * 校验邮箱
     * @param email 需要校验的字符串
     * @return 校验结果
     */
    public static boolean isEmail(String email) {
        String rule = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
        return checkString(rule, email);
    }

    /**
     * 校验网址
     * @param websiteUrl 需要校验的字符串
     * @return 校验结果
     */
    public static boolean isURL(String websiteUrl) {
        String rule = "[a-zA-z]+://[^\\s]*";
        return checkString(rule, websiteUrl);
    }

    /**
     * 校验输入的名称是否包含特殊字符
     * @param name 名称
     * @return 校验结果
     */
    public static boolean isLegal(String name) {
        String rule = "^(?!_)(?!.*?_$)[a-zA-Z0-9_\\u4e00-\\u9fa5]+$";
        return checkString(rule, name);
    }

    private static boolean checkString(String rule, String checkStr) {
        Pattern pattern = Pattern.compile(rule);
        Matcher matcher = pattern.matcher(checkStr);
        return matcher.find();
    }
}
