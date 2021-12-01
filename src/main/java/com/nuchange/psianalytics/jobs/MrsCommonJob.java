package com.nuchange.psianalytics.jobs;

import com.nuchange.psianalytics.model.AnalyticCronJob;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class MrsCommonJob {

    @Autowired
    ApplicationContext context;

    @Autowired
    JobLauncher jobLauncher;

    public void runMrsJob(String category) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        String jobName = JobConstants.CATEGORY_TO_ANALYTICS_JOB.get(category);

        /* Check if base query is running or not */
        AnalyticCronJob analyticCronJob = analyticCronJobRepository.findByName(jobName);
        if (!analyticCronJob.getEnabled()) {
            /* Means Base Job Is Running */
            return;
        }

        int id = 0;
        Map<String, JobParameter> maps = new HashMap<>();
        maps.put("time", new JobParameter(System.currentTimeMillis()));
        maps.put("id", new JobParameter(new Long(id)));
        maps.put("category", new JobParameter(category));
        maps.put("source", new JobParameter("mrs"));
        maps.put(JobConstants.TYPE, new JobParameter(JobConstants.JOB_TYPE_EVENT));
        Job fhirJob = (Job) context.getBean(JobConstants.CATEGORY_TO_JOB.get(category));
        JobParameters parameters = new JobParameters(maps);

        JobExecution jobExecution = jobLauncher.run(fhirJob, parameters);

    }
}

