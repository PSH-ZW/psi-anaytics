package com.nuchange.psianalytics.util;

import com.nuchange.psianalytics.model.*;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

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

    public EventRecords getRecordGreaterThanIdAndCategory(Integer id, String category) {
        String sql = "SELECT * FROM event_records WHERE category = ? and id > ? order by id limit 1";
        List<EventRecords> eventRecords = mrsJdbcTemplate
                .query(sql, JdbcTemplateMapperFactory.newInstance().newRowMapper(EventRecords.class), category, id);
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
        String insertSql = "insert into processed_events(source, last_processed_id, last_processed_uuid, category, name) " +
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

    public String getFullNameOfConceptByUuid(UUID conceptUuid){
        final String sql = "SELECT concept_name.name FROM concept INNER JOIN concept_name ON " +
                "concept.concept_id = concept_name.concept_id WHERE concept.uuid = ? AND concept_name_type = 'FULLY_SPECIFIED' and locale = 'en'";
        List<String> conceptNames = mrsJdbcTemplate.query(sql, JdbcTemplateMapperFactory.newInstance().newRowMapper(String.class), conceptUuid.toString());
        if(!CollectionUtils.isEmpty(conceptNames)) {
            return conceptNames.get(0);
        }
        return null;
    }

    public Encounter getEncounterByEncounterId(Integer encounterId){
        final String sql = "SELECT * FROM encounter WHERE encounter_id = ?";
        List<Encounter> encounters = mrsJdbcTemplate.query(sql,
                JdbcTemplateMapperFactory.newInstance().newRowMapper(Encounter.class), encounterId);
        if(!CollectionUtils.isEmpty(encounters)){
            return encounters.get(0);
        }
        return null;
    }

    public List<Obs> getObsByEncounterIdAndVoided(Integer encounterId, Integer voided){
        final String sql = "SELECT * FROM obs WHERE encounter_id = ? AND voided = ? AND form_namespace_and_path IS NOT null ";
        return mrsJdbcTemplate.query(sql, JdbcTemplateMapperFactory.newInstance().newRowMapper(Obs.class), encounterId, voided);
    }

    public Obs getObsById(Integer id){
        final String sql = "SELECT * FROM obs WHERE obs_id = ? ";
        List<Obs> obs = mrsJdbcTemplate.query(sql, JdbcTemplateMapperFactory.newInstance().newRowMapper(Obs.class), id);
        if(!CollectionUtils.isEmpty(obs)) {
            return obs.get(0);
        }
        return null;
    }

    public Encounter getEncounterByUuid(String encounterUUID) {
        final String sql = "SELECT * FROM encounter WHERE uuid = ?";
        List<Encounter> encounters = mrsJdbcTemplate.query(sql,
                JdbcTemplateMapperFactory.newInstance().newRowMapper(Encounter.class), encounterUUID);
        if(!CollectionUtils.isEmpty(encounters)){
            return encounters.get(0);
        }
        return null;
    }

    public Concept getConceptByObsId(Integer obsId){
        final String sql = "select * from concept where concept_id = (select concept_id from obs where obs_id = ?)";
        List<Concept> concepts = mrsJdbcTemplate.query(sql, JdbcTemplateMapperFactory.newInstance().newRowMapper(Concept.class), obsId);
        if(!CollectionUtils.isEmpty(concepts)){
            return concepts.get(0);
        }
        return null;
    }

    public Location getLocationByEncounterId(Integer encounterId){
        final String sql = "select * from location where location_id = (select location_id from encounter where encounter_id = ?)";
        List<Location> locations = mrsJdbcTemplate.query(sql, JdbcTemplateMapperFactory.newInstance().newRowMapper(Location.class), encounterId);
        if(!CollectionUtils.isEmpty(locations)){
            return locations.get(0);
        }
        return null;
    }

}
