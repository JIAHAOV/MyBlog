package com.study.reproduce.model.ov;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class BlogOV {
    private Long blogId;

    private String title;

    private String content;

    private String category;

    private String tags;

    private String blogCoverImage;

    private LocalDateTime createTime;
}
