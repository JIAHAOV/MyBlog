package com.study.reproduce.constant;

public interface MailConstant {
    String REPLY_TEMPLATE = "亲爱的{}:\n" +
            "<br>\n" +
            "你在 <a href=\"http://custom-cloud.xyz/blog/{}\">链接</a> 的评论，已被回复: \"{}\"";
    String REPLY_TITLE = "网站回复";
}
