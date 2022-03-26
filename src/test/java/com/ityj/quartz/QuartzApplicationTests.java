package com.ityj.quartz;

import com.ityj.quartz.job.MyJob;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QuartzApplicationTests {

	@Test
	public void testScheduler() throws SchedulerException {

		// scheduler
		// jobDetail
		// trigger
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
				.withIdentity("job01-myjob", "group01-job")
				.build();
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity("Trigger01", "group01-trigger")
				.withSchedule(CronScheduleBuilder.cronSchedule("*/3 * * * * ?"))
				.build();
		scheduler.scheduleJob(jobDetail, trigger);
		scheduler.start();
	}

}
