package com.nuchange.psianalytics;

import com.nuchange.psianalytics.constants.JobConstants;
import com.nuchange.psianalytics.jobs.FlatteningTask;
import com.nuchange.psianalytics.model.AnalyticsCronJob;
import com.nuchange.psianalytics.util.MetaDataService;
import com.nuchange.psiutil.PsiException;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
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

    private static final Logger logger = LoggerFactory.getLogger(JobScheduler.class);

    @Autowired
    private ApplicationContext applicationContext;

    private final Map<String, FlatteningTask> jobs = new HashMap<>();

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Autowired
    private MetaDataService metaDataService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        final List<AnalyticsCronJob> cronJobs = metaDataService.getActiveAnalyticsCronJobs();

        for (AnalyticsCronJob cronJob : cronJobs) {
            FlatteningTask bean = null;
            try {
                 bean = (FlatteningTask) applicationContext.getBean(cronJob.getName());
                 logger.info("Found bean for job : {}", cronJob.getName());
            } catch (NoSuchBeanDefinitionException e) {
                logger.info("Could not find bean for processing Job: {}", cronJob.getName());
                String exceptionString = "Could not find bean for processing Job: " + cronJob.getName() ;
                String comment = "Cron Job name : "+ cronJob.getName() + " and id : " + cronJob.getId();
                metaDataService.addLogs("", comment, exceptionString, JobConstants.ERROR_STATUS.ERROR.toString());
                throw new PsiException(exceptionString);
            }
            jobs.put(cronJob.getName(), bean);
            try {
                scheduledTaskRegistrar.setScheduler(threadPoolTaskScheduler);
                scheduledTaskRegistrar.addTriggerTask(getTask(cronJob), getTrigger(cronJob));
            } catch (ParseException e) {
                logger.error("Could not parse the cron expression: {}  for: {}",
                        cronJob.getName(), cronJob.getExpression());
                String exceptionString = "Could not parse the cron expression: " + cronJob.getName() + "for: " + cronJob.getExpression();
                String comment = "Cron Job name : "+ cronJob.getName() + " and id : " + cronJob.getId();
                metaDataService.addLogs("", comment, exceptionString, JobConstants.ERROR_STATUS.ERROR.toString());
                e.printStackTrace();
            }
        }
    }

    private Trigger getTrigger(AnalyticsCronJob quartzCronScheduler) throws ParseException {
        PeriodicTrigger periodicTrigger;
        Date now = new Date();
        long nextExecutionTimeByStatement = new CronExpression(quartzCronScheduler.getExpression()).
                getNextValidTimeAfter(now).getTime();
        periodicTrigger = new PeriodicTrigger((int) (nextExecutionTimeByStatement - now.getTime()), TimeUnit.MILLISECONDS);
        periodicTrigger.setInitialDelay(quartzCronScheduler.getStartDelay());
        return periodicTrigger;
    }

    private Runnable getTask(final AnalyticsCronJob quartzCronScheduler) {
        return () -> {
            String jobName = quartzCronScheduler.getName();
            FlatteningTask job = jobs.get(jobName);
            try {
                if(job != null) {
                    logger.info("Starting job for {}", jobName);
                    String exceptionString = "Starting "+ jobName;
                    String comment = "Cron Job name : "+ jobName;
                    metaDataService.addLogs("", comment, exceptionString, JobConstants.ERROR_STATUS.INFO.toString());
                    job.process();
                }
            } catch (InterruptedException e) {
                logger.warn("Thread Interrupted for the job: {}", jobName);
                String exceptionString = "Thread Interrupted for the job: "+ jobName;
                String comment = "Cron Job name : "+ jobName ;
                metaDataService.addLogs("", comment, exceptionString, JobConstants.ERROR_STATUS.ERROR.toString());
            } catch (IOException e) {
                logger.warn("I/O Exception Occur for the job: {}", jobName);
                String exceptionString = "I/O Exception Occur for the job: "+ jobName;
                String comment = "Cron Job name : "+ jobName ;
                metaDataService.addLogs("", comment, exceptionString, JobConstants.ERROR_STATUS.ERROR.toString());
            } catch (JobParametersInvalidException e) {
                logger.warn("Job Parameter Invalid for the job: {}", jobName);
                String exceptionString = "Job Parameter Invalid for the job: "+ jobName;
                String comment = "Cron Job name : "+ jobName ;
                metaDataService.addLogs("", comment, exceptionString, JobConstants.ERROR_STATUS.ERROR.toString());
            } catch (JobExecutionAlreadyRunningException e) {
                logger.warn("Job Execution is already running {}", jobName);
                String exceptionString = "Job Execution is already running: "+ jobName;
                String comment = "Cron Job name : "+ jobName ;
                metaDataService.addLogs("", comment, exceptionString, JobConstants.ERROR_STATUS.ERROR.toString());
            } catch (JobRestartException e) {
                logger.warn("Job Restart Exception for the job: {}", jobName);
                String exceptionString = "Job Restart Exception for the job: "+ jobName;
                String comment = "Cron Job name : "+ jobName ;
                metaDataService.addLogs("", comment, exceptionString, JobConstants.ERROR_STATUS.ERROR.toString());
            } catch (JobInstanceAlreadyCompleteException e) {
                e.printStackTrace();
            }
        };
    }
}
