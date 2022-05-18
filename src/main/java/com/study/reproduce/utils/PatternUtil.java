package com.study.reproduce.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtil {

    public static boolean isEmail(String email) {
        String rule = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
        return checkString(rule, email);
    }

    public static boolean isURL(String websiteUrl) {
        String rule = "[a-zA-z]+://[^\\s]*";
        return checkString(rule, websiteUrl);
    }

    private static boolean checkString(String rule, String checkStr) {
        Pattern pattern = Pattern.compile(rule);
        Matcher matcher = pattern.matcher(checkStr);
        return matcher.find();
    }
}
