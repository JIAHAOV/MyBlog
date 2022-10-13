package com.study.reproduce.utils;

import cn.hutool.core.util.StrUtil;
import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

public class CachePageUtil<T> {
    private final int DEFAULT_SIZE = 6;
    /**
     * 总查询数据
     */
    List<T> records;
    /**
     * 页码
     */
    private int page;
    /**
     * 每页结果数
     */
    private int pageSize;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 总数
     */
    private int total;

    public CachePageUtil(int page, int pageSize, @NonNull List<T> records) {
        this.page = page < 0 ? 1 : page;
        this.pageSize = pageSize < 0 ? pageSize = DEFAULT_SIZE : pageSize;
        this.records = records;
        this.total = records.size();
        this.totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
    }

    public List<T> getCurrentPageRecords() {
        long start = (long) (page - 1) * pageSize;
        if (start < 0) {
            start = 0;
        }
        if (start > total) {
            start = (long) (totalPage - 1) * pageSize;
        }
        List<T> collect = records.stream().skip((long) (page - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
        return collect;
    }

    @Override
    public String toString() {
        return StrUtil.format("共有{}条记录，没用{}个，可分为{}页", total, pageSize, totalPage);
    }
}
