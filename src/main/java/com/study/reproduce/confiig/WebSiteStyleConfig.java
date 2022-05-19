package com.study.reproduce.confiig;


public class WebSiteStyleConfig {
    public static String style = "amaze";

    /**
     * 修改网站风格
     * @param type 类型
     */
    public static void changeStyle(String type) {
        if (type.isEmpty()) {
            return;
        }
        if ("0".equals(type)) {
            style = "amaze";
        }
        if ("1".equals(type)) {
            style = "default";
        }
        if ("2".equals(type)) {
            style = "yummy-jekyll";
        }
    }
}
