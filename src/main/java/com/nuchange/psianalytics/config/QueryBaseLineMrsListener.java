package com.nuchange.psianalytics.config;

import com.nuchange.psianalytics.constants.JobConstants;
import com.nuchange.psianalytics.model.EventRecords;
import com.nuchange.psianalytics.model.ProcessedEvents;
import com.nuchange.psianalytics.util.BatchUtil;
import com.nuchange.psianalytics.util.MetaDataService;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueryBaseLineMrsListener implements JobExecutionListener {

    @Autowired
    protected MetaDataService metaDataService;

    @Autowired
    private BatchUtil batchUtil;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        JobParameters jobParameters = jobExecution.getJobParameters();
        String category = jobParameters.getString("category");
        String eventCategory = JobConstants.CATEGORY_TO_EVENT.get(category);
        String uuid = "";
        Integer id = 0;
        ProcessedEvents processedEvents = finaLastProcessedEventsForCategory(category);
        if (processedEvents != null && processedEvents.getLastProcessedId() != null) {
            id = processedEvents.getLastProcessedId();
        }
        EventRecords mrsRecord = metaDataService.getRecordGreaterThanIdAndCategory(id, eventCategory);
        if (mrsRecord != null) {
            uuid = mrsRecord.getUuid();
            id = mrsRecord.getId();
        }
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        executionContext.put("eventId", id);
        executionContext.put("eventUuid", uuid);
        batchUtil.setLockForCategory(jobParameters.getString("category"), false);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            JobParameters jobParameters = jobExecution.getJobParameters();
            batchUtil.saveLastProcessEventForJob(jobExecution.getJobParameters(), jobExecution.getExecutionContext());
            batchUtil.setLockForCategory(jobParameters.getString("category"), true);
        }
    }

    private ProcessedEvents finaLastProcessedEventsForCategory(String category) {
        ProcessedEvents processedEvents = metaDataService.findProcessedEventByCategory(category);
        return processedEvents;
    }
}
