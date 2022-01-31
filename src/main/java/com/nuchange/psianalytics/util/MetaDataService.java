package com.nuchange.psianalytics.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuchange.psianalytics.constants.MRSConstants.ConceptDatatype;
import com.nuchange.psianalytics.model.*;
import com.nuchange.psiutil.AnalyticsUtil;
import com.nuchange.psiutil.model.FormDetails;
import io.micrometer.core.instrument.util.StringUtils;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MetaDataService {

    @Autowired
    @Qualifier("analyticsJdbcTemplate")
    protected JdbcTemplate analyticsJdbcTemplate;

    @Autowired
    @Qualifier("mrsJdbcTemplate")
    protected JdbcTemplate mrsJdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(MetaDataService.class);

    private static Map<String, String> formToProgramMap = new HashMap<>();

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

    public void deleteEventRecord(Integer id) {
        String deleteSql = "delete from event_records where id = ?";
        mrsJdbcTemplate.update(deleteSql, id);
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

    public String getFullNameOfConceptByConceptId(Integer id){
        final String sql = "SELECT get_concept_name(?)";
        List<String> conceptNames = mrsJdbcTemplate.query(sql,
                JdbcTemplateMapperFactory.newInstance().newRowMapper(String.class), id);
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

    public List<Obs> getObsByEncounterIdAndVoided(Integer encounterId){
        final String sql = "SELECT * FROM obs WHERE encounter_id = ? AND voided = 0 AND form_namespace_and_path IS NOT null ";
        return mrsJdbcTemplate.query(sql, JdbcTemplateMapperFactory.newInstance().newRowMapper(Obs.class), encounterId);
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

    public Concept getConceptById(Integer conceptId){
        final String sql = "select * from concept where concept_id = ?";
        List<Concept> concepts = mrsJdbcTemplate.query(sql,
                JdbcTemplateMapperFactory.newInstance().newRowMapper(Concept.class), conceptId);
        if(!CollectionUtils.isEmpty(concepts)){
            return concepts.get(0);
        }
        return null;
    }

    public List<UUID> getConceptNameUuidsForConcept(Integer conceptId) {
        final String sql = "select uuid from concept_name where concept_id = ?";
        return mrsJdbcTemplate.query(sql,
                JdbcTemplateMapperFactory.newInstance().newRowMapper(UUID.class), conceptId);
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

    public boolean entryExistsInEventTracker(String encounterId) {
        final String sql = "SELECT count(*) from event_tracker where encounter_id = '%s'";
        List<Integer> value =  analyticsJdbcTemplate.query(String.format(sql, encounterId),
                JdbcTemplateMapperFactory.newInstance().newRowMapper(Integer.class));
        if(!CollectionUtils.isEmpty(value)) {
            return !value.get(0).equals(0);
        }
        return false;
    }

    public String getValueAsString(Obs obs, Locale locale) {
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("#0.0#####");
        if (obs != null) {
            ConceptDatatype dataType = getDataTypeOfConceptWithConceptId(obs.getConceptId());
            if (dataType.equals(ConceptDatatype.BOOLEAN)) {
                return getValueAsBoolean(obs);
            }

            if (dataType.equals(ConceptDatatype.CODED)) {
                if (obs.getValueCoded() == null) {
                    return "";
                }

                if (obs.getValueDrug() != null) {
                    return getFullNameOfConceptByConceptId(obs.getValueDrug());
                }

                return getFullNameOfConceptByConceptId(obs.getValueCoded());
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
                    return getFullNameOfConceptByConceptId(obs.getValueDrug());
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

    public String getValueAsBoolean(Obs obs) {
        final String TRUE = "true";
        final String FALSE = "false";
        if (obs.getValueCoded() != null) {
            final int YES_CONCEPT_ID = Integer.parseInt(getGlobalPropertyValue("concept.true"));
            if (obs.getValueCoded().equals(YES_CONCEPT_ID)) {
                return TRUE;
            }

            final int NO_CONCEPT_ID = Integer.parseInt(getGlobalPropertyValue("concept.false"));
            if (obs.getValueCoded().equals(NO_CONCEPT_ID)) {
                return FALSE;
            }
        } else if (obs.getValueNumeric() != null) {
            if (obs.getValueNumeric() == 1.0D) {
                return TRUE;
            }

            if (obs.getValueNumeric() == 0.0D) {
                return FALSE;
            }
        }

        return FALSE;
    }

    //TODO: make separate methods for enrollment and encounter
    public void updateEventsToSync(String type, Object primaryIdentifier, Object patientId, Object programId
            , Object encounterId, Boolean isEncounterType) {
        if (isEncounterType) {
            String checkSql = "select type_identifier from events_to_sync where encounter_id = ? and patient_id = ?";
            List<String> stringList = analyticsJdbcTemplate.query(checkSql, JdbcTemplateMapperFactory.newInstance().newRowMapper(String.class), encounterId.toString(), patientId.toString());
            if(!CollectionUtils.isEmpty(stringList)) return;

            String insertSql = "insert into events_to_sync(type_name,type_identifier,patient_id,encounter_id, program_id) " +
                    "values (?, ?, ?, ?, ?)";
            analyticsJdbcTemplate.update(insertSql, type, primaryIdentifier.toString(), patientId.toString(),
                    encounterId.toString(), programId);
        }else {
            //TODO: needs to be checked if below line is needed
            String checkSql = "select type_identifier from events_to_sync where program_id = ? and patient_id = ?";
            List<String> stringList = analyticsJdbcTemplate.query(checkSql, JdbcTemplateMapperFactory.newInstance().newRowMapper(String.class), programId.toString(), patientId.toString());
            if(!CollectionUtils.isEmpty(stringList)) return;

            String insertSql = "insert into events_to_sync(type_name,type_identifier,patient_id,program_id) " +
                    "values (?, ?, ?, ?)";
            analyticsJdbcTemplate.update(insertSql, type, primaryIdentifier.toString(), patientId.toString(),
                    programId.toString());
        }
    }

    public String getFormNameSpacePathForEncounter(Integer encounterId) {
        String sql = "select form_namespace_and_path from obs where encounter_id = ? and form_namespace_and_path is not null limit 1";
        List<String> formNameSpaceNames = mrsJdbcTemplate.query(sql, JdbcTemplateMapperFactory.newInstance().newRowMapper(String.class), encounterId.toString());
        if(!CollectionUtils.isEmpty(formNameSpaceNames)) {
            return formNameSpaceNames.get(0);
        }
        return null;
    }

    public void initializeFormMetaDataTable() {
        String sql = "select distinct name , version from form where published = 1";
        List<FormDetails> formDetails = mrsJdbcTemplate.query(sql, JdbcTemplateMapperFactory.newInstance()
                .newRowMapper(FormDetails.class));
        if(!CollectionUtils.isEmpty(formDetails)){
            String insertSql = "insert into form_meta_data(form_name, version) values(?, ?)";
            for(FormDetails formDetail: formDetails){
                analyticsJdbcTemplate.update(insertSql, formDetail.getFormName(), formDetail.getVersion().toString());
            }
        }
    }
    public FormDetails findFormMetaDataDetailsForName(String formName){
        String sql = "select form_name, version from form_meta_data where form_name = ?";
        List<FormDetails> formDetails = analyticsJdbcTemplate
                .query(sql, JdbcTemplateMapperFactory.newInstance().newRowMapper(FormDetails.class), formName);
        if(!CollectionUtils.isEmpty(formDetails)) {
            return formDetails.get(0);
        }
        return null;
    }

    public String getProgramNameForFormTable(String formName) {
        if(formToProgramMap.isEmpty()) {
            initialiseFormProgramMap();
        }
        String program = formToProgramMap.get(formName);
        //TODO: handle exception when form does not belong any programs.
        return program;
    }

    private void initialiseFormProgramMap() {
        List<Mapping> mappings = getAllMappings();
        for(Mapping mapping : mappings) {
            try{
                Set<String> tables = getTablesFromMappingJson(mapping.getMappingJson());
                for(String formTableName : tables) {
                    formToProgramMap.put(formTableName, mapping.getProgramName());
                }
            }
            catch (JsonProcessingException e) {
                logger.error(String.format("Could not process mapping json for program : %s", mapping.getProgramName()));
            }
        }
    }

    private Set<String> getTablesFromMappingJson(String mappingJson) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Map<String, Object>> mapping = mapper
                .readValue(mappingJson, new TypeReference<Map<String, Map<String, Object>>>(){});
        if(mapping != null && !CollectionUtils.isEmpty(mapping.get("formTableMappings"))) {
            return mapping.get("formTableMappings").keySet();
        }
        return new HashSet<>();
    }

    private List<Mapping> getAllMappings() {
        final String sql = "SELECT program_name, mapping_json, dhis_program_stage_id FROM mapping";
        return analyticsJdbcTemplate.query(sql, JdbcTemplateMapperFactory.newInstance().newRowMapper(Mapping.class));
    }

    public String getProgramForEncounter(Integer encounter_id) {
        String formNameSpaceAndPath = getFormNameSpacePathForEncounter(encounter_id);
        if(formNameSpaceAndPath != null) {
            String tableName = getFormTableNameFromFormNameSpaceAndPath(formNameSpaceAndPath);
            return getProgramNameForFormTable(tableName);
        }

        return null;
    }

    private String getFormTableNameFromFormNameSpaceAndPath(String formName) {
        formName = formName.substring(formName.indexOf("^")+1, formName.indexOf("."));
        formName = formName.replaceAll(" ", "_");
        return AnalyticsUtil.generateColumnName(formName);
    }

    public boolean isValidProgramName(String programName) {
        if(formToProgramMap.isEmpty()) {
            initialiseFormProgramMap();
        }
        return formToProgramMap.containsValue(programName);
    }

    public boolean shouldFlattenForm(String formName) {
        if(formToProgramMap.isEmpty()) {
            initialiseFormProgramMap();
        }
        return formToProgramMap.containsKey(formName);
    }
}

