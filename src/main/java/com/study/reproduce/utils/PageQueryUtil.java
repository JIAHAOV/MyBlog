package com.study.reproduce.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageQueryUtil {
    //当前页码
    private Integer page;
    //每页条数
    private Integer limit;
    //本页开始的条数
    private Integer start;

    public PageQueryUtil(Integer page, Integer limit) {
        this.page = page;
        this.limit = limit;
        this.start = (page - 1) * limit;
    }
}
