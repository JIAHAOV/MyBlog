package com.study.reproduce.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    //总记录数
    private long totalCount;
    //每页记录数
    private int pageSize;
    //总页数
    private int totalPage;
    //当前页数
    private int currPage;
    //列表数据
    private List<T> list;

    public PageResult(long totalCount, int pageSize, int currPage, List<T> list) {
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.list = list;
        this.totalPage = totalCount % pageSize == 0 ? totalPage / pageSize : totalPage / pageSize + 1;
    }
}
