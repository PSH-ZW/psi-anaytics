package com.nuchange.psianalytics.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuchange.psianalytics.constants.MRSConstants;
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
import java.text.ParseException;
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

    private final static ObjectMapper mapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(MetaDataService.class);

    private static final Map<String, String> formToProgramMap = new HashMap<>();
    private static final Map<String, String> programToProgramStageIdMap = new HashMap<>();
    private static final String CATEGORY = "analytics";
    public static final String DATE_FORMAT_WITH_24HR_TIME = "yyyy-MM-dd kk:mm:ss";

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

    public UUID getConceptUuidById(Integer conceptId) {
        final String sql = "select uuid from concept where concept_id = ?";
        List<UUID> concepts = mrsJdbcTemplate.query(sql,
                JdbcTemplateMapperFactory.newInstance().newRowMapper(UUID.class), conceptId);
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

    public boolean entryExistsInEventTracker(String encounterId, String programStage) {
        final String sql = "SELECT count(*) from event_tracker where encounter_id = '%s' and program_stage = '%s'";
        List<Integer> value =  analyticsJdbcTemplate.query(String.format(sql, encounterId, programStage),
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

    public void insertIntoEventsToSync(Object patientId, String programId, String encounterId) {
        String checkSql = "select count(*) from events_to_sync where encounter_id = ? and patient_id = ? and program_id = ?";
        List<Integer> count = analyticsJdbcTemplate.query(checkSql, JdbcTemplateMapperFactory.newInstance()
                .newRowMapper(Integer.class), encounterId, patientId.toString(), programId);
        boolean eventDoesNotExists = !CollectionUtils.isEmpty(count) && count.get(0).equals(0);
        String sql;
        if(eventDoesNotExists) {
            sql = "insert into events_to_sync(patient_id, encounter_id, program_id) values (?, ?, ?)";
        } else {
            sql = "update events_to_sync set synced = false where patient_id = ? and encounter_id = ? and program_id = ?";
        }
        analyticsJdbcTemplate.update(sql, patientId.toString(), encounterId, programId);
    }

    public List<String> getFormNameSpacePathsForEncounter(Integer encounterId) {
        String sql = "select form_namespace_and_path from obs where encounter_id = ? and form_namespace_and_path is not null";
        List<String> formNameSpaceNames = mrsJdbcTemplate.query(sql, JdbcTemplateMapperFactory.newInstance().newRowMapper(String.class), encounterId.toString());
        if(!CollectionUtils.isEmpty(formNameSpaceNames)) {
            return formNameSpaceNames;
        }
        return Collections.emptyList();
    }

    public Integer findFormMetaDataDetailsForName(String formName){
        String sql = "select version from form_meta_data where form_name = ?";
        List<Integer> formVersion = analyticsJdbcTemplate
                .query(sql, JdbcTemplateMapperFactory.newInstance().newRowMapper(Integer.class), formName);
        if(!CollectionUtils.isEmpty(formVersion)) {
            return formVersion.get(0);
        }
        return null;
    }

    public String getProgramNameForFormTable(String tableName) {
        initialiseFormProgramMapIfEmpty();
        String program = formToProgramMap.get(tableName);
        //TODO: handle exception when form does not belong any programs.
        return program;
    }

    public String getDhisProgramStageIdForTable(String table) {
        String programName = getProgramNameForFormTable(table);
        if(!programToProgramStageIdMap.containsKey(programName)) {
            try {
                Mapping mapping = getMappingForProgram(programName);
                MappingJson mappingJson = mapper.readValue(mapping.getMappingJson(), MappingJson.class);
                String dhisProgramStageId = mappingJson.getDhisProgramStageId().get("id");
                programToProgramStageIdMap.put(programName, dhisProgramStageId);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return programToProgramStageIdMap.get(programName);
    }

    private Mapping getMappingForProgram(String programName) {
        String sql = "SELECT program_name, mapping_json FROM mapping where program_name = ?";
        try{
            return analyticsJdbcTemplate.query(sql,
                    JdbcTemplateMapperFactory.newInstance().newRowMapper(Mapping.class), programName).get(0);
        } catch (Exception e) {
            logger.error(String.format("Could not get Mapping for program %s", programName), e.getMessage());
            throw e;
        }
    }

    private void initialiseFormProgramMapIfEmpty() {
        if(formToProgramMap.isEmpty()) {
            List<Mapping> mappings = getAllMappings();
            for(Mapping mapping : mappings) {
                Set<String> tables = getTablesFromMapping(mapping);
                for(String formTableName : tables) {
                    formToProgramMap.put(formTableName, mapping.getProgramName());
                }
            }
        }
    }

    private Set<String> getTablesFromMapping(Mapping mapping) {
        String mappingJsonString = mapping.getMappingJson();
        if(mappingJsonString != null) {
            try {
                MappingJson mappingJson = mapper.readValue(mappingJsonString, MappingJson.class);
                Map<String, Map<String, Map<String, String>>> formTableMappings = mappingJson.getFormTableMappings();
                if (!CollectionUtils.isEmpty(formTableMappings)) {
                    return formTableMappings.keySet();
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return new HashSet<>();
    }

    private List<Mapping> getAllMappings() {
        final String sql = "SELECT program_name, mapping_json FROM mapping";
        return analyticsJdbcTemplate.query(sql, JdbcTemplateMapperFactory.newInstance().newRowMapper(Mapping.class));
    }

    public Set<String> getProgramsInEncounter(Integer encounterId) {
        List<String> formNameSpaceAndPaths = getFormNameSpacePathsForEncounter(encounterId);
        Set<String> programs = new HashSet<>();
        for(String formNameSpaceAndPath : formNameSpaceAndPaths) {
            if(StringUtils.isNotEmpty(formNameSpaceAndPath)) {
                String tableName = getFormTableNameFromFormNameSpaceAndPath(formNameSpaceAndPath);
                programs.add(getProgramNameForFormTable(tableName));
            }
        }
        return programs;
    }

    private String getFormTableNameFromFormNameSpaceAndPath(String formName) {
        formName = formName.substring(formName.indexOf("^")+1, formName.indexOf("."));
        formName = formName.replaceAll(" ", "_");
        return AnalyticsUtil.generateColumnName(formName);
    }

    public boolean shouldFlattenForm(String formName) {
        initialiseFormProgramMapIfEmpty();
        return formToProgramMap.containsKey(formName);
    }

    public boolean shouldNotFlattenForm(String formName) {
        return !shouldFlattenForm(formName);
    }

    public String getFormResourcePath(String formName, Integer version) {
        //get the path where the form json is saved for the latest published version.
        String sql = "select value_reference from form_resource " +
                " where form_id = (select form_id from form where name = ? and version = ?)";

        List<String> formResourcePath = mrsJdbcTemplate.query(sql,
                JdbcTemplateMapperFactory.newInstance().newRowMapper(String.class), formName, version);
        if(!CollectionUtils.isEmpty(formResourcePath)) {
            return formResourcePath.get(0);
        }
        return null;
    }

    public static String getStringFromDate(Date date, String format) {
        SimpleDateFormat outputFormat = new SimpleDateFormat(format);
        return outputFormat.format(date);
    }

    public static Date getDateFromString(String date, String format) {
        SimpleDateFormat outputFormat = new SimpleDateFormat(format);
        try {
            return outputFormat.parse(date);
        } catch (ParseException ignored) {

        }
        return new Date(Long.MIN_VALUE);
    }

    public static String getRealStringOrEmptyString(String value) {
        if(value == null) return "";
        return value;
    }

    public void addLogs( String service, String comments, String statusInfo, String status){
        String sql = "INSERT INTO log (program, comments, status_info, date_created," +
                " category, status) VALUES (?, ?, ?, ?, ?, ?)";
        String stringFromDate = getStringFromDate(new Date(), DATE_FORMAT_WITH_24HR_TIME);
        Date dateFromString = getDateFromString(stringFromDate, DATE_FORMAT_WITH_24HR_TIME);
        analyticsJdbcTemplate.update(sql, getRealStringOrEmptyString(service), getRealStringOrEmptyString(comments)
                , getRealStringOrEmptyString(statusInfo), dateFromString, CATEGORY, status);
    }

    public String getFacilityNameForEncounter(Integer encounterId) {
        return getOrgunitNameForEncounterAndType(encounterId, MRSConstants.FACILITY);
    }

    public String getDistrictNameForEncounter(Integer encounterId) {
        return getOrgunitNameForEncounterAndType(encounterId, MRSConstants.DISTRICT);
    }

    private String getOrgunitNameForEncounterAndType(Integer encounterId, String type) {
        String sql = "select cn.name from obs o inner join concept c on o.concept_id = c.concept_id " +
                " inner join concept_class cc on c.class_id = cc.concept_class_id and cc.name = ? " +
                " left join concept_name cn on o.value_coded = cn.concept_id and cn.locale = 'en' and cn.concept_name_type = 'FULLY_SPECIFIED' " +
                " where o.encounter_id = ? and o.voided = 0 order by cn.name";
        List<String> orgUnitNames = mrsJdbcTemplate.query(sql,
                JdbcTemplateMapperFactory.newInstance().newRowMapper(String.class), type, encounterId);
        if(!CollectionUtils.isEmpty(orgUnitNames)) {
            return orgUnitNames.get(0);
        }
        return "";
    }
}

