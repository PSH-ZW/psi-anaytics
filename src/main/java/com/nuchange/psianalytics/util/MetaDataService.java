package com.nuchange.psianalytics.util;

import com.nuchange.psianalytics.constants.MRSConstants.ConceptDatatype;
import com.nuchange.psianalytics.model.*;
import io.micrometer.core.instrument.util.StringUtils;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
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

    public String getConceptNameByConceptNameId(Integer conceptNameId){
        final String sql = "SELECT name FROM concept_name WHERE concept_name_id = ?";
        List<String> conceptNames = mrsJdbcTemplate.query(sql,
                JdbcTemplateMapperFactory.newInstance().newRowMapper(String.class), conceptNameId);
        if(!CollectionUtils.isEmpty(conceptNames)) {
            return conceptNames.get(0);
        }
        return null;
    }

    public String getFullNameOfConceptByIdAndLocale(Integer id, Locale locale){
        final String sql = "SELECT name FROM concept_name WHERE concept_id = ? AND concept_name_type = 'FULLY_SPECIFIED' and locale = ?";
        List<String> conceptNames = mrsJdbcTemplate.query(sql,
                JdbcTemplateMapperFactory.newInstance().newRowMapper(String.class), id, locale.toLanguageTag());
        if(!CollectionUtils.isEmpty(conceptNames)) {
            return conceptNames.get(0);
        }
        return null;
    }

    public ConceptDatatype getDataTypeOfConceptWithConceptId(Integer conceptId) {
        final String sql = "SELECT cd.name FROM concept_datatype cd " +
                "INNER JOIN concept c ON cd.concept_datatype_id = c.datatype_id WHERE concept_id = ?";
        List<String> dataType = mrsJdbcTemplate.query(sql,
                JdbcTemplateMapperFactory.newInstance().newRowMapper(String.class), conceptId);
        if(!CollectionUtils.isEmpty(dataType)) {
            return ConceptDatatype.create(dataType.get(0));
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

    public Obs getObsGroup(Obs obs) {
        Integer obsGroupId = obs.getObsGroupId();
        return getObsById(obsGroupId);
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
        List<Concept> concepts = mrsJdbcTemplate.query(sql,
                JdbcTemplateMapperFactory.newInstance().newRowMapper(Concept.class), obsId);
        if(!CollectionUtils.isEmpty(concepts)){
            return concepts.get(0);
        }
        return null;
    }

    public Location getLocationByEncounterId(Integer encounterId){
        final String sql = "select location_id, name, uuid from location where location_id = (select location_id from encounter where encounter_id = ?)";
        List<Location> locations = mrsJdbcTemplate.query(sql, JdbcTemplateMapperFactory.newInstance().newRowMapper(Location.class), encounterId);
        if(!CollectionUtils.isEmpty(locations)){
            return locations.get(0);
        }
        return null;
    }

    private Boolean isPreciseConceptNumeric(Integer conceptId) {
        final String sql = "select precise from concept_numeric where concept_id = ? ";
        List<Integer> isPrecise = mrsJdbcTemplate.query(sql,
                JdbcTemplateMapperFactory.newInstance().newRowMapper(Integer.class), conceptId);
        if(!CollectionUtils.isEmpty(isPrecise)){
            return isPrecise.get(0) == 1 ? Boolean.TRUE : Boolean.FALSE;
        }
        return Boolean.FALSE;
    }

    private List<Obs> getGroupMembersOfObs(Obs obs) {
        Integer obsId = obs.getObsId();
        final String sql = "SELECT * FROM obs WHERE obs_group_id = ? ";
        return mrsJdbcTemplate.query(sql, JdbcTemplateMapperFactory.newInstance().newRowMapper(Obs.class), obsId);
    }

    public String getGlobalPropertyValue(String property) {
        final String sql = "SELECT property_value FROM global_property WHERE property = ? ";
        List<String> value =  mrsJdbcTemplate.query(sql,
                JdbcTemplateMapperFactory.newInstance().newRowMapper(String.class), property);
        if(!CollectionUtils.isEmpty(value)) {
            return value.get(0);
        }
        return null;
    }

    public String getValueAsString(Obs obs, Locale locale) {
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("#0.0#####");
        if (obs != null) {
            ConceptDatatype dataType = getDataTypeOfConceptWithConceptId(obs.getConceptId());
            if (dataType.equals(ConceptDatatype.BOOLEAN)) {
                return getValueAsBoolean(obs) == null ? "" : getValueAsBoolean(obs).toString();
            }

            if (dataType.equals(ConceptDatatype.CODED)) {
                if (obs.getValueCoded() == null) {
                    return "";
                }

                if (obs.getValueDrug() != null) {
                    return getFullNameOfConceptByIdAndLocale(obs.getValueDrug(), locale);
                }

                return getFullNameOfConceptByIdAndLocale(obs.getValueCoded(), locale);
            }

            if (!dataType.equals(ConceptDatatype.NUMERIC) && !dataType.equals(ConceptDatatype.STRUCTURED_NUMERIC)) {
                if (dataType.equals(ConceptDatatype.DATE)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    return obs.getValueDatetime() == null ? "" : dateFormat.format(obs.getValueDatetime());
                }

                if (dataType.equals(ConceptDatatype.TIME)) {
                    return obs.getValueDatetime() == null ? "" : Formatter.format(obs.getValueDatetime(),
                            locale, Formatter.FORMAT_TYPE.TIME);
                }

                if (dataType.equals(ConceptDatatype.DATETIME)) {
                    return obs.getValueDatetime() == null ? "" : Formatter.format(obs.getValueDatetime(),
                            locale, Formatter.FORMAT_TYPE.TIMESTAMP);
                }

                if (dataType.equals(ConceptDatatype.TEXT)) {
                    return obs.getValueText();
                }

                if (dataType.equals(ConceptDatatype.COMPLEX) && obs.getValueComplex() != null) {
                    String[] complexValues = obs.getValueComplex().split("\\|");

                    for (String complexValue : complexValues) {
                        if (StringUtils.isNotEmpty(complexValue)) {
                            return complexValue.trim();
                        }
                    }
                }
            } else {
                if (obs.getValueNumeric() == null) {
                    return "";
                }

                if (dataType.equals(ConceptDatatype.NUMERIC)) {
                    if (isPreciseConceptNumeric(obs.getConceptId())) {
                        double groupMember = obs.getValueNumeric();
                        int numericValue = (int) groupMember;
                        return Integer.toString(numericValue);
                    }

                    df.format(obs.getValueNumeric());
                }
            }
        }
        if (obs != null) {
            List<Obs> groupMembers = getGroupMembersOfObs(obs);
            if (obs.getValueNumeric() != null) {
                return df.format(obs.getValueNumeric());
            } else if (obs.getValueCoded() != null) {
                if (obs.getValueDrug() != null) {
                    return getFullNameOfConceptByIdAndLocale(obs.getValueDrug(), locale);
                } else {
                    return getConceptNameByConceptNameId(obs.getValueCodedNameId());
                }
            } else if (obs.getValueDatetime() != null) {
                return Formatter.format(obs.getValueDatetime(), locale, Formatter.FORMAT_TYPE.DATE);
            } else if (obs.getValueText() != null) {
                return obs.getValueText();
            } else if (!CollectionUtils.isEmpty(groupMembers)) {
                 {
                    StringBuilder memberValues = new StringBuilder();
                    Obs memberObs;
                    for (Iterator<Obs> obsIterator = groupMembers.iterator(); obsIterator.hasNext(); memberValues.append(getValueAsString(memberObs, locale))) {
                        memberObs = obsIterator.next();
                        if (memberValues.length() > 0) {
                            memberValues.append(", ");
                        }
                    }

                    return memberValues.toString();
                }
            } else {
                if (obs.getValueComplex() != null) {
                    String[] complexValues = obs.getValueComplex().split("\\|");

                    for (String complexValue : complexValues) {
                        if (StringUtils.isNotEmpty(complexValue)) {
                            return complexValue.trim();
                        }
                    }
                }

                return "";
            }
        }

        return null;
    }

    public Boolean getValueAsBoolean(Obs obs) {
        if (obs.getValueCoded() != null) {
            final int YES_CONCEPT_ID = Integer.parseInt(getGlobalPropertyValue("concept.true"));
            if (obs.getValueCoded().equals(YES_CONCEPT_ID)) {
                return Boolean.TRUE;
            }

            final int NO_CONCEPT_ID = Integer.parseInt(getGlobalPropertyValue("concept.false"));
            if (obs.getValueCoded().equals(NO_CONCEPT_ID)) {
                return Boolean.FALSE;
            }
        } else if (obs.getValueNumeric() != null) {
            if (obs.getValueNumeric() == 1.0D) {
                return Boolean.TRUE;
            }

            if (obs.getValueNumeric() == 0.0D) {
                return Boolean.FALSE;
            }
        }

        return Boolean.FALSE;
    }

    public void updateEventYetToBeSynced(String type, Object primaryIdentifier) {
        String insertSql = "insert into event_record_left_to_sync(type_name,type_identifier) " +
                "values (?, ?)";
        analyticsJdbcTemplate.update(type, primaryIdentifier.toString());

    }
}
