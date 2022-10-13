package com.study.reproduce.schedule;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration    //标识配置类
@EnableScheduling //开启定时任务
public class DataBaseUpdateScheduleTask {

    //每天中文 12 点触发
    @Scheduled(cron = "0 0 12 * * ?")
//    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    private void updateViews() {

    }
}
