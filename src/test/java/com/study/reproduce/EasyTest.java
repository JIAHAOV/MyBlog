package com.study.reproduce;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EasyTest {
    @Test
    public void test1() {
        String string = "?a";
        String valid = "^[\\u4e00-\\u9fa5_a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(valid);
        Matcher matcher = pattern.matcher(string);
        System.out.println(matcher.find());
    }
}
