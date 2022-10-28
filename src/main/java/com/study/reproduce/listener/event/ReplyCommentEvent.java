package com.study.reproduce.listener.event;

import org.springframework.context.ApplicationEvent;

public class ReplyCommentEvent extends ApplicationEvent {
    public ReplyCommentEvent(Object source) {
        super(source);
    }
}
