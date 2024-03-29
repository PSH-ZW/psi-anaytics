package com.nuchange.psianalytics.jobs.querybased;

import com.nuchange.psianalytics.constants.JobConstants;
import com.nuchange.psianalytics.model.ProcessedEvents;
import com.nuchange.psianalytics.model.QueryJob;
import com.nuchange.psianalytics.model.ResultExtractor;
import com.nuchange.psianalytics.util.MetaDataService;
import com.nuchange.psianalytics.util.QueryBaseJobUtil;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class QueryBasedJobWriter<D> implements ItemWriter<D>, StepExecutionListener {

    private JobParameters jobParameters;

    @Autowired
    @Qualifier("analyticsJdbcTemplate")
    public JdbcTemplate template;

    @Autowired
    protected MetaDataService metaDataService;

    public QueryBasedJobWriter(DataSource ds){
        super();
        template = new JdbcTemplate(ds);
    }

    protected void setJobParameter(JobParameters jobParameters) {
        this.jobParameters = jobParameters;
    }

    protected JobParameters getJobParameters() {
        return jobParameters;
    }

    protected void saveResultExtractorList(List<ResultExtractor> resultExtractors, ExecutionContext executionContext) throws SQLException {
        Extractor ex = new Extractor(template);
        for (ResultExtractor extractor : resultExtractors) {
            String target = extractor.getTarget();
            String category = extractor.getCategory();
            QueryJob jobDetails = QueryBaseJobUtil.getJobDetails(category);
            List<String> colHeaders = extractor.getColHeaders();
            Integer id = extractor.getProcessingId();
            for (Map<String,Object> stringObjectMap : extractor.getRowValues()) {
                ex.insertData(target, jobDetails, colHeaders, id, stringObjectMap);
                if(target.equals("encounter")){
                    insertSyncDataForEncounter(stringObjectMap);
                }
            }

            /* Update Processed Events If Event Based */
            if (getJobParameters().getString(JobConstants.TYPE).equals(JobConstants.JOB_TYPE_EVENT)) {
                ProcessedEvents processedEvents = metaDataService.findProcessedEventByCategory(category);
                if (processedEvents == null) {
                    processedEvents = new ProcessedEvents();
                    processedEvents.setCategory(category);
                    processedEvents.setSource(jobParameters.getString("source"));
                }
                processedEvents.setLastProcessedId(executionContext.getInt("eventId"));
                processedEvents.setLastProcessedUuid(executionContext.getString("eventUuid"));
                metaDataService.addOrUpdateProcessedEvent(processedEvents);
            }
        }
    }

    private void insertSyncDataForEncounter(Map<String, Object> stringObjectMap) {
        String encounterId = stringObjectMap.get("encounter_id").toString();
        Set<String> programNames = metaDataService.getProgramsInEncounter(Integer.valueOf(encounterId));
        for(String programName : programNames) {
            if(StringUtils.hasLength(programName)) {
                metaDataService.insertIntoEventsToSync(stringObjectMap.get("patient_id"), programName, encounterId);
            }
        }
    }

}
