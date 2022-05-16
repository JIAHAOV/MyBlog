package com.study.reproduce.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageParam {
    private Integer page;
    private Integer limit;
    private String keyword;
}
