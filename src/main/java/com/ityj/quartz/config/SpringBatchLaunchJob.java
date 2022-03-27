package com.ityj.quartz.config;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.Instant;

@Slf4j
public class SpringBatchLaunchJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();

        String jobName = jobDataMap.getString("jobName");
        JobLauncher jobLauncher = (JobLauncher) jobDataMap.get("jobLauncher");
        JobLocator jobLocator = (JobLocator) jobDataMap.get("jobLocator");
        JobParameters params = new JobParametersBuilder()
                .addString("TimeStamp", String.valueOf(Instant.now().toEpochMilli()))
                .toJobParameters();
        try {
            Job job = jobLocator.getJob(jobName);
            jobLauncher.run(job, params);
        } catch (Exception e) {
            log.error("Error:", e);
        }

    }
}
