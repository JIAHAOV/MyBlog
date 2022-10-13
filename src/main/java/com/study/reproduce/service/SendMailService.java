package com.study.reproduce.service;

import java.io.InputStream;
import java.util.Map;

public interface SendMailService {

    /**
     * 群发简单邮件
     * @param to 接收者的数组
     * @param title 标题
     * @param content 正文
     */
    boolean sendSimpleMassage(String[] to, String title, String content);

    /**
     * 发送简单邮件
     * @param to 接收者的数组
     * @param title 标题
     * @param content 正文
     */
    boolean sendSimpleMassage(String to, String title, String content);

    /**
     * 群发带一个附件的邮件
     * @param to 接收者的数组
     * @param title 标题
     * @param content 正文
     * @param fileName 附件名
     * @param inputStream 附件
     */
    boolean sendAttachmentMessage(String[] to, String title, String content, String fileName, InputStream inputStream);

    /**
     * 发送带一个附件的邮件
     * @param to 接收者的数组
     * @param title 标题
     * @param content 正文
     * @param fileName 附件名
     * @param inputStream 附件
     */
    boolean sendAttachmentMessage(String to, String title, String content, String fileName, InputStream inputStream);

    /**
     * 群发带有 html 元素的邮件
     * @param to 接收者的数组
     * @param title 标题
     * @param content 正文
     */
    boolean sendHtmlMessage(String[] to, String title, String content);

    /**
     * 发送带有 html 元素的邮件
     * @param to 接收者的数组
     * @param title 标题
     * @param content 正文
     */
    boolean sendHtmlMessage(String to, String title, String content);

    /**
     * 群发自定义要素的邮件
     * @param to 接收者的数组
     * @param title 标题
     * @param content 正文
     * @param attachment 是否添加附件
     * @param attachments 开启附件功能时需要传的附件
     * @param html 是否开启 html 解析
     */
    boolean sendMimeMessage(String[] to, String title, String content, boolean attachment, Map<String, InputStream> attachments, boolean html);

    /**
     * 发送自定义要素的邮件
     * @param to 接收者的数组
     * @param title 标题
     * @param content 正文
     * @param attachment 是否添加附件
     * @param attachments 开启附件功能时需要传的附件
     * @param html 是否开启 html 解析
     */
    boolean sendMimeMessage(String to, String title, String content, boolean attachment, Map<String, InputStream> attachments, boolean html);

    boolean sendMimeMessage(String[] to, String title, String content, boolean attachment, String fileName, InputStream inputStream, boolean html);
}