package com.nuchange.psianalytics.jobs.encounter;

import com.nuchange.psianalytics.jobs.FlatteningTask;
import com.nuchange.psianalytics.jobs.JobConstants;
import com.nuchange.psianalytics.model.AnalyticsCronJob;
import com.nuchange.psianalytics.util.MetaDataService;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(JobConstants.MRS_ENCOUNTER_JOB)
@DisallowConcurrentExecution
public class EncounterEventBaseJob implements FlatteningTask {

    @Autowired
    ApplicationContext context;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    protected MetaDataService metaDataService;

    @Override
    public void process() throws InterruptedException, IOException, JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        String jobName = JobConstants.CATEGORY_TO_ANALYTICS_JOB.get(JobConstants.ENCOUNTER);

        /* Check if base query is running or not */
        AnalyticsCronJob analyticCronJob = metaDataService.getAnalyticsCronJobByName(jobName);
        if (!analyticCronJob.getEnabled()) {
            /* Means Base Job Is Running */
            return;
        }

        int id = 0;
        Map<String, JobParameter> maps = new HashMap<>();
        maps.put("time", new JobParameter(System.currentTimeMillis()));
        maps.put("source", new JobParameter("mrs"));
        maps.put(JobConstants.TYPE, new JobParameter(JobConstants.JOB_TYPE_EVENT));
        Job job = (Job) context.getBean(JobConstants.CATEGORY_TO_JOB.get(JobConstants.MRS_ENCOUNTER));
        JobParameters parameters = new JobParameters(maps);

        JobExecution jobExecution = jobLauncher.run(job, parameters);
    }
}
