package com.study.reproduce.service;

import com.study.reproduce.model.ov.SimpleBlogInfo;
import com.study.reproduce.utils.PageResult;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public interface CacheService {
    <T> PageResult<T> queryPageResult(String key, Integer currentPage, Integer pageSize,
                                      Class<T> clazz, Comparator<? super T> comparator);

    boolean saveSimpleInfo2Cache(String key, Object object);

    <T, ID> T getFromCache(String keyPrefix, ID id, Function<ID, T> dbFallback, Class<T> clazz, Long ttl, TimeUnit timeUnit);

    Long incrementView(Long blogId);

    List<SimpleBlogInfo> getNewBlogs();
}
