package com.ityj.quartz.job;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class JobTest {
    public static void main(String[] args) throws SchedulerException {
        // scheduler
        // jobDetail
        // trigger
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
                .withIdentity("job01-myjob", "group01-job")
                .build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("Trigger01", "group01-trigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/3 * * * * ?"))
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
    }

}
