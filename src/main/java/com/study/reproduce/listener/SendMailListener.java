package com.study.reproduce.listener;

import cn.hutool.core.util.StrUtil;
import com.study.reproduce.listener.event.ReplyCommentEvent;
import com.study.reproduce.model.domain.Comment;
import com.study.reproduce.service.SendMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.study.reproduce.constant.MailConstant.REPLY_TEMPLATE;
import static com.study.reproduce.constant.MailConstant.REPLY_TITLE;

@Slf4j
@Component
public class SendMailListener {
    @Autowired
    private SendMailService sendMailService;

    @Async
    @EventListener
    public void sendMailListener(ReplyCommentEvent event) {
        Comment comment = (Comment) event.getSource();
        log.info("reply comment,comment id is {}", comment.getCommentId());
        String mailMessage = StrUtil.format(REPLY_TEMPLATE,
                comment.getCommentator(), comment.getBlogId(), comment.getReplyBody());
        sendMailService.sendHtmlMessage(comment.getEmail(), REPLY_TITLE, mailMessage);
    }
}
