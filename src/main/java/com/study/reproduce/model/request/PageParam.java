package com.study.reproduce.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageParam {
    private Integer page;
    private Integer limit;
    private String keyword;

    public PageParam(Integer page, String keyword) {
        this.page = page;
        this.keyword = keyword;
        this.limit = 9;
    }
}
