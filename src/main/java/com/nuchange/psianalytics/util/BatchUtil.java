package com.nuchange.psianalytics.util;

import com.nuchange.psianalytics.constants.JobConstants;
import com.nuchange.psianalytics.model.AnalyticsCronJob;
import com.nuchange.psianalytics.model.ProcessedEvents;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class BatchUtil {

    @Autowired
    protected MetaDataService metaDataService;

    public ProcessedEvents saveLastProcessEventForJob(JobParameters jobParameters, ExecutionContext executionContext) {
        String category = jobParameters.getString("category");
        String source = jobParameters.getString("source");
        ProcessedEvents processedEvents = metaDataService.findProcessedEventByCategory(category);
        if (processedEvents == null) {
            processedEvents = new ProcessedEvents();
            processedEvents.setCategory(category);
            processedEvents.setSource(source);
        }
        processedEvents.setLastProcessedId(executionContext.getInt("eventId"));
        processedEvents.setLastProcessedUuid(executionContext.getString("eventUuid"));
        metaDataService.addOrUpdateProcessedEvent(processedEvents);
        return processedEvents;
    }

    public AnalyticsCronJob setLockForCategory(String category, Boolean unlocked) {
        /*AnalyticsCronJob analyticCronJob = analyticCronJobRepository.findByName(JobConstants.CATEGORY_TO_ANALYTICS_JOB.get(category));*/
        AnalyticsCronJob analyticCronJob = metaDataService.getAnalyticsCronJobByName(JobConstants.CATEGORY_TO_ANALYTICS_JOB.get(category));
        if (analyticCronJob != null) {
            analyticCronJob.setEnabled(unlocked);
        }
        return analyticCronJob;
    }

    public Integer runCountQuery(JdbcTemplate template, String query) {
        List<Integer> queryResult  = template.query(query, new RowMapper() {

            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return rs.getInt(1);
            }

        });
        if (queryResult.isEmpty()) {
            return null;
        } else {
            return queryResult.get(0);
        }
    }

}
