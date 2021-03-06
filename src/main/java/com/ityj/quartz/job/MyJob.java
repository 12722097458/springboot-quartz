package com.ityj.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
//@Component
//@EnableScheduling
public class MyJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("MyJob.execute()....");
    }

    //@Scheduled(cron = "0/5 * * * * ?")
    public void testCron() {
        log.info("..................");
    }
}
