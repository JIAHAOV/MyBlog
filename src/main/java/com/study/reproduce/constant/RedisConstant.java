package com.study.reproduce.constant;

public interface RedisConstant {
    String BLOG_KEY = "website:myblog:blog:";
    String BLOG_VIEW_KEY = "website:myblog:blog:view:";
    String CATEGORY_KEY = "website:myblog:category";
    String HOT_TAGS_KEY = "website:myblog:index:tags:hot:";
    String HOT_BLOG_KEY = "website:myblog:index:blogs:hot:";
    String NEW_BLOG_KEY = "website:myblog:index:blogs:new:";
    String WEBSITE_CONFIG_KEY = "website:myblog:index:config:";
    String VERIFY_CODE = "website:myblog:check:code:";
    String ADMIN_BLOG_LIST_KEY = "website:myblog:admin:blog:info";
    String BLOG_LIST_KEY = "website:myblog:blog:list";
}
