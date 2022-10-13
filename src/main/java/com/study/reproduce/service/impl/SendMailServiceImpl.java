package com.study.reproduce.service.impl;

import com.study.reproduce.service.SendMailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SendMailServiceImpl implements SendMailService {

    @Value("${spring.mail.username}")
    private final String from = "3409444780@qq.com";

    @Resource
    private JavaMailSender javaMailSender;

    @Override
    public boolean sendSimpleMassage(String[] to, String title, String content) {
        // 构建邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(title);
        message.setText(content);
        // 发送邮件
        javaMailSender.send(message);
        log.info("成功发送邮件给{}", Arrays.toString(to));
        return true;
    }

    @Override
    public boolean sendSimpleMassage(String to, String title, String content) {
        return sendSimpleMassage(new String[]{to}, title, content);
    }

    @Override
    public boolean sendAttachmentMessage(String[] to, String title, String content, String fileName, InputStream inputStream) {
        HashMap<String, InputStream> map = new HashMap<>();
        map.put(fileName, inputStream);
        return sendMimeMessage(to, title, content, true, map, false);
    }

    @Override
    public boolean sendAttachmentMessage(String to, String title, String content, String fileName, InputStream inputStream) {
        return sendAttachmentMessage(new String[]{to}, title, content, fileName, inputStream);
    }

    @Override
    public boolean sendHtmlMessage(String[] to, String title, String content) {
        return sendMimeMessage(to, title, content, false, null, true);
    }

    @Override
    public boolean sendHtmlMessage(String to, String title, String content) {
        return sendHtmlMessage(new String[]{to}, title, content);
    }

    @Override
    public boolean sendMimeMessage(String[] to, String title, String content,
                                   boolean attachment, Map<String, InputStream> attachments, boolean html) {
        MimeMessage message = javaMailSender.createMimeMessage();
        //为 true ,代表创建支持替代文本、内联元素和附件的消息
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, attachment);
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            if (attachment && attachments != null) {
                addAttachment(messageHelper, attachments);
            }
            messageHelper.setSubject(title);
            // 设置为 true，启用 html 解析
            messageHelper.setText(content, html);
            javaMailSender.send(message);
            log.info("成功发送邮件给{}", Arrays.toString(to));
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean sendMimeMessage(String[] to, String title, String content, boolean attachment, String fileName, InputStream inputStream, boolean html) {
        HashMap<String, InputStream> map = new HashMap<>();
        map.put(fileName, inputStream);
        return sendMimeMessage(to, title, content, true, map, true);
    }

    @Override
    public boolean sendMimeMessage(String to, String title, String content, boolean attachment, Map<String, InputStream> attachments, boolean html) {
        return sendMimeMessage(new String[]{to}, title, content, attachment, attachments, html);
    }

    private void addAttachment(MimeMessageHelper messageHelper, Map<String, InputStream> attachments) {
        for (Map.Entry<String, InputStream> entry : attachments.entrySet()) {
            ByteArrayResource resource;
            try {
                resource = new ByteArrayResource(IOUtils.toByteArray(entry.getValue()));
                messageHelper.addAttachment(entry.getKey(), resource);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
