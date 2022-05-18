package com.study.reproduce.confiig;

public class WebSiteStyleConfig {
    public static String style = "amaze";

    public static void changeStyle(Integer type) {
        if (type == null) {
            return;
        }
        if (type == 0) {
            style = "amaze";
        }
        if (type == 1) {
            style = "default";
        }
        if (type == 2) {
            style = "yummy-jekyll";
        }
    }
}
