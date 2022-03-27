package com.ityj.quartz.config;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class SpringQuartzConfig {

    @Value("#{'${quartz.job.jobName}'.split('\\|')}")
    private String[] jobNameArr;

    @Value("#{'${quartz.job.cronExpression}'.split('\\|')}")
    private String[] cronExpressionArr;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobLocator jobLocator;

    @Bean
    public SchedulerFactoryBean getSchedulerFactoryBean() {
        log.info("Into getSchedulerFactoryBean()...");
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        JobDetail[] jobDetails = getJobDetails();
        schedulerFactoryBean.setJobDetails(jobDetails);
        schedulerFactoryBean.setTriggers(getTriggers(jobDetails));
        return schedulerFactoryBean;
    }

    private Trigger[] getTriggers(JobDetail[] jobDetails) {
        Trigger[] triggers = new Trigger[cronExpressionArr.length];
        for (int i = 0; i < cronExpressionArr.length; i++) {
            triggers[i] = getTrigger(jobDetails[i], cronExpressionArr[i]);
        }
        return triggers;
    }

    private JobDetail[] getJobDetails() {
        Assert.isTrue(jobNameArr.length == cronExpressionArr.length, "[Assertion failed] - the number of jobNames and cronExpressions in configuration must equal.");
        JobDetail[] jobDetails = new JobDetail[jobNameArr.length];
        for (int i = 0; i < jobNameArr.length; i++) {
            jobDetails[i] = getJobDetailByJobName(jobNameArr[i]);
        }
        return jobDetails;
    }

    public JobDetail getJobDetailByJobName(String jobName){
        JobDetailFactoryBean jobFactory = new JobDetailFactoryBean();
        jobFactory.setApplicationContext(applicationContext);
        jobFactory.setJobClass(SpringBatchLaunchJob.class);
        Map<String, Object> map = new HashMap<>();
        jobFactory.setName("my_job-" + jobName);
        jobFactory.setGroup("my_group");
        map.put("jobName", jobName);
        map.put("jobLauncher", jobLauncher);
        map.put("jobLocator", jobLocator);
        jobFactory.setJobDataAsMap(map);
        jobFactory.setDurability(true);
        jobFactory.afterPropertiesSet();
        return jobFactory.getObject();
    }

    public Trigger getTrigger(JobDetail jobDetail, String cronExpression){
        CronTriggerFactoryBean cTrigger = new CronTriggerFactoryBean();
        cTrigger.setName("trigger_name-" + jobDetail.getKey().getName() + cronExpression);
        cTrigger.setGroup("trigger_group");
        cTrigger.setJobDetail(jobDetail);
        cTrigger.setCronExpression(cronExpression);
        try {
            cTrigger.afterPropertiesSet();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cTrigger.getObject();
    }
}
