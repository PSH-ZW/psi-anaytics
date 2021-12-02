package com.nuchange.psianalytics.jobs.querybased;

import com.nuchange.psianalytics.jobs.JobConstants;
import com.nuchange.psianalytics.model.EventRecords;
import com.nuchange.psianalytics.model.ProcessedEvents;
import com.nuchange.psianalytics.model.QueryJob;
import com.nuchange.psianalytics.model.ResultExtractor;
import com.nuchange.psianalytics.util.MetaDataService;
import com.nuchange.psianalytics.util.QueryBaseJobUtil;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

//@Component(JobConstants.QUERY_EVENT_BASED_MRS_JOB_ITEM_READER_STEP_ONE)
public abstract class QueryEventBasedMrsReader extends QueryBasedJobReader<List<ResultExtractor>> {

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

    public List<ResultExtractor> readEvent() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
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
            return null;
        }

        Object[] params = new Object[] { eventRecords.getObject() };
        QueryJob queryJob = QueryBaseJobUtil.getJobDetails(category);
        String queryToFindId = queryJob.getFindIdByUuid();
        List<Integer> listOfId = getTemplate().query(queryToFindId, new RowMapper<Integer>() {
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt(1);
            }
        }, params);

        List<ResultExtractor> resultExtractorList =
                getResultExtractorForCategoryAndId(queryJob, category, new Long(listOfId.get(0)));
        ExecutionContext executionContext = stepExecution.getExecutionContext();
        executionContext.put("eventId", eventRecords.getId());
        executionContext.put("eventUuid", eventRecords.getUuid());
        return resultExtractorList;
    }

    private ProcessedEvents findLastProcessedEventsForCategory(String category) {
        ProcessedEvents processedEvents = metaDataService.findProcessedEventByCategory(category);
        return processedEvents;
    }
}
