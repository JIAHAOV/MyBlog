package com.study.reproduce.confiig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Order
@Configuration
public class PreKeyRedisConfig {
    public static final String DEFAULT_PREFIX = "test:";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    class PrefixRedisSerializer extends StringRedisSerializer {
        @Override
        public byte[] serialize(String s) {
            if (s == null) {
                return null;
            }
            // 这里加上你需要加上的key前缀
            String realKey = DEFAULT_PREFIX + s;
            return super.serialize(realKey);
        }
        @Override
        public String deserialize(byte[] bytes) {
            String s = bytes == null ? null : new String(bytes);
            int index = s.indexOf(DEFAULT_PREFIX);
            if (index != -1) {
                return s.substring(index + 2);
            }
            return s;
        }
    }

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        StringRedisSerializer serializer = new PrefixRedisSerializer();
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.afterPropertiesSet();
        stringRedisTemplate.setKeySerializer(serializer);
        stringRedisTemplate.afterPropertiesSet();
        return serializer;
    }
}
