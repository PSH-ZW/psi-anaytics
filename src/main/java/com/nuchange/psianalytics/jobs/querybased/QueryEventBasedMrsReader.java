package com.nuchange.psianalytics.jobs.querybased;

import com.nuchange.psianalytics.constants.JobConstants;
import com.nuchange.psianalytics.model.EventRecords;
import com.nuchange.psianalytics.model.ProcessedEvents;
import com.nuchange.psianalytics.model.QueryJob;
import com.nuchange.psianalytics.model.ResultExtractor;
import com.nuchange.psianalytics.util.MetaDataService;
import com.nuchange.psianalytics.util.QueryBaseJobUtil;
import com.nuchange.psiutil.AnalyticsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

//@Component(JobConstants.QUERY_EVENT_BASED_MRS_JOB_ITEM_READER_STEP_ONE)
public abstract class QueryEventBasedMrsReader extends QueryBasedJobReader<List<ResultExtractor>> {
    private static Logger logger = LoggerFactory.getLogger(QueryEventBasedMrsReader.class);

    @Autowired
    private ApplicationContext context;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private SimpleJobOperator jobOperator;

    private StepExecution stepExecution;


    public QueryEventBasedMrsReader(DataSource ds) {
        super(ds);
    }

    @Autowired
    private MetaDataService metaDataService;

    public void setStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
        super.setJobParameter(stepExecution.getJobParameters());
    }

    public ExitStatus afterStep(StepExecution stepExecution) {
        return stepExecution.getExitStatus();
    }

    public List<ResultExtractor> readEvent() throws UnexpectedInputException, IOException, ParseException, NonTransientResourceException {
        /* Read Event Records According To Category And Processed That */
        String category = getJobParameters().getString("category");
        String eventCategory = JobConstants.CATEGORY_TO_EVENT.get(category);

        /* Find Last Processed Id From Processed Events */
        ProcessedEvents processedEvents = findLastProcessedEventsForCategory(category);
        int id = 0;
        if (processedEvents != null && processedEvents.getLastProcessedId() != null) {
            id = processedEvents.getLastProcessedId();
        }

        EventRecords eventRecords = metaDataService.getRecordGreaterThanIdAndCategory(id, eventCategory);

        if (eventRecords == null) {
            //There are no more events to process, stop the job.
            stopJob(category);
            return Collections.emptyList();
        }

        String objectRef = eventRecords.getObject();
        String uuid = AnalyticsUtil.getUuidFromParam(objectRef, eventCategory);
        QueryJob queryJob = QueryBaseJobUtil.getJobDetails(category);
        String queryToFindId = queryJob.getFindIdByUuid();
        List<Integer> listOfId = getTemplate().query(queryToFindId, (rs, rowNum) -> rs.getInt(1), uuid);

        if(CollectionUtils.isEmpty(listOfId)) {
            //If there are no resource(patient, enrollment etc.) with the uuid specified in the eventRecord in the DB,
            //it could be because the data has been deleted. We will delete the eventRecord, otherwise the job will be stuck
            //here trying to read the non-existent resource again and again.
            metaDataService.deleteEventRecord(eventRecords.getId());
            return null;
        }
        List<ResultExtractor> resultExtractorList =
                getResultExtractorForCategoryAndId(queryJob, category, Long.valueOf(listOfId.get(0)));
        ExecutionContext executionContext = stepExecution.getExecutionContext();
        executionContext.put("eventId", eventRecords.getId());
        executionContext.put("eventUuid", eventRecords.getUuid());
        logger.info(String.format("Processing EventRecord of category %s with id : %d ", eventCategory, eventRecords.getId()));
        return resultExtractorList;
    }

    private void stopJob(String category) {
        try{
            Job job = (Job) context.getBean(JobConstants.CATEGORY_TO_JOB.get(category));
            Set<JobExecution> executions = jobExplorer.findRunningJobExecutions(job.getName());
            for (JobExecution execution : executions) {
                if (execution.isRunning() && !execution.isStopping()) {
                    jobOperator.stop(execution.getId());
                    logger.info("Stopped Job: {}", job.getName());
                }
            }
        }catch (Exception e) {
            logger.warn("Could not find running instance of Job to stop.");
        }
    }

    private ProcessedEvents findLastProcessedEventsForCategory(String category) {
        ProcessedEvents processedEvents = metaDataService.findProcessedEventByCategory(category);
        return processedEvents;
    }
}
