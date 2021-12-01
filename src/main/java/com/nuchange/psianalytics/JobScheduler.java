package com.nuchange.psianalytics;

import com.nuchange.psianalytics.jobs.FlatJob;
import com.nuchange.psianalytics.model.AnalyticCronJob;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
public class JobScheduler implements SchedulingConfigurer {

    private static Logger logger = LoggerFactory.getLogger(JobScheduler.class);

    @Autowired
    private ApplicationContext applicationContext;

    private Map<String, FlatJob> jobs = new HashMap<>();

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        final List<AnalyticCronJob> cronJobs = analyticCronJobRepository.findAllByEnabled(true);

        for (AnalyticCronJob cronJob : cronJobs) {
            FlatJob bean = (FlatJob) applicationContext.getBean(cronJob.getName());
            if (bean == null) {
                logger.error("Could not find bean for processing Job: " + cronJob.getName());
            } else{
                jobs.put(cronJob.getName(), bean);
                try {
                    scheduledTaskRegistrar.setScheduler(threadPoolTaskScheduler);
                    scheduledTaskRegistrar.addTriggerTask(getTask(cronJob), getTrigger(cronJob));
                } catch (ParseException e) {
                    logger.error("Could not parse the cron expression: " + cronJob.getExpression() + " for: " +
                            cronJob.getName());
                    e.printStackTrace();
                }
            }
        }
    }

    private Trigger getTrigger(AnalyticCronJob quartzCronScheduler) throws ParseException {
        PeriodicTrigger periodicTrigger;
        Date now = new Date();
        long nextExecutionTimeByStatement = new CronExpression(quartzCronScheduler.getExpression()).
                getNextValidTimeAfter(now).getTime();
        periodicTrigger = new PeriodicTrigger((int) (nextExecutionTimeByStatement - now.getTime()), TimeUnit.MILLISECONDS);
        periodicTrigger.setInitialDelay(quartzCronScheduler.getStartDelay());
        return periodicTrigger;
    }

    private Runnable getTask(final AnalyticCronJob quartzCronScheduler) {
        return new Runnable() {
            @Override
            public void run() {
                FlatJob job = jobs.get(quartzCronScheduler.getName());
                try {
                    job.process();
                } catch (InterruptedException e) {
                    logger.warn("Thread Interrupted for the job: " + quartzCronScheduler.getName());
                } catch (IOException e) {
                    logger.warn("I/O Exception Occur for the job: " + quartzCronScheduler.getName());
                } catch (JobParametersInvalidException e) {
                    logger.warn("Job Parameter Invalid for the job: " + quartzCronScheduler.getName());
                } catch (JobExecutionAlreadyRunningException e) {
                    logger.warn("Job Execution is already running " + quartzCronScheduler.getName());
                } catch (JobRestartException e) {
                    logger.warn("Job Restart Exception for the job: " + quartzCronScheduler.getName());
                } catch (JobInstanceAlreadyCompleteException e) {
                    e.printStackTrace();
                }
                try {
                    logger.debug("Triggering job: " + quartzCronScheduler.getName());

                } catch (Exception e) {
                    logger.warn("Thread Failed for the job: " + quartzCronScheduler.getName());
                }
            }
        };
    }
}