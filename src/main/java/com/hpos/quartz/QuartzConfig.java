package com.hpos.quartz;

import com.hpos.quartz.OrderCleanupJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail orderCleanupJobDetail() {
        return JobBuilder.newJob(OrderCleanupJob.class)
                .withIdentity("orderCleanupJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger orderCleanupTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(orderCleanupJobDetail())
                .withIdentity("orderCleanupTrigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMinutes(5)
                        .repeatForever())
                .build();
    }
}
