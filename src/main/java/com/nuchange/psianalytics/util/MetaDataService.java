package com.nuchange.psianalytics.util;

import com.nuchange.psianalytics.model.AnalyticsCronJob;
import com.nuchange.psianalytics.model.EventRecords;
import com.nuchange.psianalytics.model.ProcessedEvents;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class MetaDataService {

    @Autowired
    @Qualifier("analyticsJdbcTemplate")
    protected JdbcTemplate analyticsJdbcTemplate;

    @Autowired
    @Qualifier("mrsJdbcTemplate")
    protected JdbcTemplate mrsJdbcTemplate;

    public List<AnalyticsCronJob> getActiveAnalyticsCronJobs() {
        String sql = "SELECT * FROM analytics_cron_job WHERE enabled = true";
        return analyticsJdbcTemplate.query(sql, JdbcTemplateMapperFactory.newInstance()
                .newRowMapper(AnalyticsCronJob.class));
    }

    public AnalyticsCronJob getAnalyticsCronJobByName(String name) {
        String sql = "SELECT * FROM analytics_cron_job WHERE name = ?";
        List<AnalyticsCronJob> analyticsCronJobs = analyticsJdbcTemplate
                .query(sql, JdbcTemplateMapperFactory.newInstance().newRowMapper(AnalyticsCronJob.class), name);
        if(!CollectionUtils.isEmpty(analyticsCronJobs)) {
            return analyticsCronJobs.get(0);
        }
        return null;
    }

    public EventRecords getRecordGreaterThanIdAndCategory(int id, String category) {
        String sql = "SELECT * FROM event_records WHERE category = ? and id > ? order by id limit 1";
        List<EventRecords> eventRecords = mrsJdbcTemplate
                .query(sql, JdbcTemplateMapperFactory.newInstance().newRowMapper(EventRecords.class), id, category);
        if(!CollectionUtils.isEmpty(eventRecords)) {
            return eventRecords.get(0);
        }
        return null;
    }

    public ProcessedEvents findProcessedEventByCategory(String category) {
        String sql = "SELECT * FROM processed_events WHERE category = ?";
        List<ProcessedEvents> processedEvents = analyticsJdbcTemplate
                .query(sql, JdbcTemplateMapperFactory.newInstance().newRowMapper(ProcessedEvents.class), category);
        if(!CollectionUtils.isEmpty(processedEvents)) {
            return processedEvents.get(0);
        }
        return null;
    }

    public void addOrUpdateProcessedEvent(ProcessedEvents processedEvents) {
        String insertSql = "insert into processed_events(source, lastProcessedId, lastProcessedUuid, category, name) " +
                "values (?, ?, ?, ?, ?)";
        String updateSql = "update processed_events set last_processed_id = ?, last_processed_uuid = ? where id = ?";
        if(processedEvents.getId() == null) {
            analyticsJdbcTemplate.update(insertSql, processedEvents.getSource(), processedEvents.getLastProcessedId(),
                    processedEvents.getLastProcessedUuid(), processedEvents.getCategory(), processedEvents.getName());
        }
        else {
            analyticsJdbcTemplate.update(updateSql, processedEvents.getLastProcessedId(),
                    processedEvents.getLastProcessedUuid(), processedEvents.getId());
        }
    }
}
