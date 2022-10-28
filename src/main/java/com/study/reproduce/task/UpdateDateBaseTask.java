package com.study.reproduce.task;

import com.study.reproduce.mapper.CronMapper;
import com.study.reproduce.model.domain.Blog;
import com.study.reproduce.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.List;
import java.util.stream.Collectors;

import static com.study.reproduce.constant.RedisConstant.BLOG_VIEW_KEY;

/**
 * 同步 redis 中的浏览量到数据库
 */
@Slf4j
@Configuration
@EnableScheduling
public class UpdateDateBaseTask implements SchedulingConfigurer {

    private final CronMapper cronMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final BlogService blogService;


    public UpdateDateBaseTask(CronMapper cronMapper, StringRedisTemplate stringRedisTemplate, BlogService blogService) {
        this.cronMapper = cronMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.blogService = blogService;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(() -> {

            List<Blog> list = blogService.list();
            List<Blog> blogs = list.stream().filter(blog -> blog.getBlogStatus().equals(1)).collect(Collectors.toList());
            for (Blog blog : blogs) {
                String key = BLOG_VIEW_KEY + blog.getBlogId();
                Long views = stringRedisTemplate.opsForHyperLogLog().size(key);
                blog.setBlogViews(views);
            }
            blogService.updateBatchById(blogs);
            log.info("update views form redis to database, count is {}", blogs.size());

        }, triggerContext -> {
            String cron = cronMapper.selectOne(null).getCron();
            if (cron == null || StringUtils.isBlank(cron)) {
                log.error("cron 语句为空");
                cron = "0 */5 * * * ?";
            }
            return new CronTrigger(cron).nextExecutionTime(triggerContext);
        });

    }
}