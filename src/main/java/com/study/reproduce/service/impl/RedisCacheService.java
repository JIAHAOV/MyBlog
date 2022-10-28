package com.study.reproduce.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.study.reproduce.exception.BusinessException;
import com.study.reproduce.model.ov.SimpleBlogInfo;
import com.study.reproduce.service.CacheService;
import com.study.reproduce.utils.PageResult;
import com.study.reproduce.utils.VisitorHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.study.reproduce.constant.RedisConstant.BLOG_VIEW_KEY;

@Service
public class RedisCacheService implements CacheService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 从 redis 中分页查询数据
     * @param key redis 中的键
     * @param currentPage 当前页
     * @param pageSize 每页大小
     * @param clazz 查询对象的类型
     * @param comparator 排序规则
     * @param <T> 查询对象的类型
     * @return 封装的查询结果
     */
    @Override
    public <T> PageResult<T> queryPageResult(String key, Integer currentPage, Integer pageSize, Class<T> clazz, Comparator<? super T> comparator) {
        int from = (currentPage - 1) * pageSize;
        int end = currentPage * pageSize - 1;
        Long size = stringRedisTemplate.opsForZSet().size(key);
        if (size == null || size == 0) {
            throw new BusinessException("缓存中无数据");
        }
        if (end > size) {
            end = -1;
        }
        Set<String> range = stringRedisTemplate.opsForZSet().reverseRange(key, from, end);
        if (range == null) {
            return new PageResult<>(size, pageSize, currentPage, Collections.emptyList());
        }
        List<T> list = range.stream()
                .map(item -> JSONUtil.toBean(item, clazz))
                .sorted(comparator)
                .collect(Collectors.toList());
        return new PageResult<>(size, pageSize, currentPage, list);
    }

    @Override
    public boolean saveSimpleInfo2Cache(String key, Object object) {
        return false;
    }

    @Override
    public <T, ID> T getFromCache(String keyPrefix, ID id, Function<ID, T> dbFallback, Class<T> clazz, Long ttl, TimeUnit timeUnit) {
        String key = keyPrefix + id;
        String s = stringRedisTemplate.opsForValue().get(key);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        if (s == null) {
            T data = dbFallback.apply(id);
            if (data == null) {
                stringRedisTemplate.opsForValue().set(key, "", ttl, timeUnit);
                return null;
            }
            String jsonStr;
            jsonStr = JSON.toJSONString(data);
            stringRedisTemplate.opsForValue().set(key, jsonStr, ttl, timeUnit);
            return data;
        }
        if ("".equals(s)) {
            return null;
        }

        T result;
        result = JSON.parseObject(s, clazz);
//            result = mapper.readValue(s, clazz);
        return result;
    }

    @Override
    public Long incrementView(Long blogId) {
        LocalDate now = LocalDate.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String address = VisitorHolder.getVisitor().getAddress();
        String value = date + "-" + address;
        String key = BLOG_VIEW_KEY + blogId;
        stringRedisTemplate.opsForHyperLogLog().add(key, value);
        return stringRedisTemplate.opsForHyperLogLog().size(key);
    }

    @Override
    public List<SimpleBlogInfo> getNewBlogs() {
        return null;
    }
}
