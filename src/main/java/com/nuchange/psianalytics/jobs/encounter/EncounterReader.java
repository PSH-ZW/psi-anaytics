package com.nuchange.psianalytics.jobs.encounter;

import com.nuchange.psianalytics.constants.JobConstants;
import com.nuchange.psianalytics.jobs.querybased.QueryBasedJobReader;
import com.nuchange.psianalytics.model.*;
import com.nuchange.psianalytics.util.MetaDataService;
import com.nuchange.psianalytics.util.QueryBaseJobUtil;
import com.nuchange.psiutil.AnalyticsUtil;
import com.nuchange.psiutil.PsiException;
import com.nuchange.psiutil.model.Forms;
import com.nuchange.psiutil.model.ObsType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.*;


public abstract class EncounterReader<D> extends QueryBasedJobReader<D> {
    private static final Logger logger = LoggerFactory.getLogger(EncounterReader.class);
    @Autowired
    protected MetaDataService metaDataService;
    @Autowired
    protected EncounterHelper encounterHelper;

    protected EncounterReader(DataSource dataSource) {
        super(dataSource);
    }

    @Transactional
    public EncounterJobDto readEncounter(Integer encounterId) throws IOException {
        //TODO: currently we are flattening all encounters. Need to flatten encounters for only the forms we need to sync.
        logger.debug("Processing Encounter : {}", encounterId);
        Encounter encounter = metaDataService.getEncounterByEncounterId(encounterId);
        List<Obs> obsForEncounter = metaDataService.getObsByEncounterIdAndVoided(encounterId);
        Set<String> deleted = new HashSet<>();
        /* To Store All Queries which are generated */
        List<Query> insertQueries = new ArrayList<>();

        /* To Store Query For Insertion Which are generated according to each table */
        Map<String, Query> obsColAndVal = new HashMap<>();

        for (Obs obs : obsForEncounter) {
            FileAttributes fileAttributes = new FileAttributes(obs.getFormNameSpaceAndPath());
            String formTableName = AnalyticsUtil.generateColumnName(fileAttributes.getFormName());
            if(metaDataService.shouldFlattenForm(formTableName)) {
                throwExceptionIfFormVersionsMismatch(fileAttributes);
                String formResourcePath = metaDataService.getFormResourcePath(fileAttributes.getFormName(), fileAttributes.getVersion());
                Map<String, ObsType> conceptMap = encounterHelper.getConceptObsTypeMapForForm(formResourcePath);
                Forms form = AnalyticsUtil.readForm(formResourcePath);
                Concept concept = metaDataService.getConceptByObsId(obs.getObsId());
                String conceptUuid = concept.getUuid();
                Map<String, String> columnNames = AnalyticsUtil.getColumnNamesForForm(form);
                String formNameWithInstance = formTableName + "_" + fileAttributes.getInstance().toString();
                if (!obsColAndVal.containsKey(formNameWithInstance)) {
                    initialiseQuery(formTableName, obsColAndVal, formNameWithInstance, encounter, fileAttributes);
                }
                Query query = obsColAndVal.get(formNameWithInstance);
                if (conceptMap.containsKey(conceptUuid)) {
                    ObsType obsType = conceptMap.get(conceptUuid);
                    String columnName = columnNames.get(conceptUuid);
                    String value = "";
                    if (obsType.getControlType().equals(JobConstants.MULTI_SELECT)) {
                        UUID answerConceptUuid = metaDataService.getConceptUuidById(obs.getValueCoded());
                        columnName = columnNames.get(conceptUuid + answerConceptUuid);
                        value = obs.getVoided() != 1 ? "true" : "false";
                    }
                    if (obsType.getControlType().equals(JobConstants.TABLE)) {
                        value = obs.getVoided() != 1 ? metaDataService.getValueAsString(obs, Locale.ENGLISH) : "";
                    }
                    if(StringUtils.hasLength(columnName)) {
                        //Some old concepts that are removed from the forms might be present in old obs,
                        // in that case the column name will be null. Skipping them to avoid null pointer exception.
                        query.getColAndVal().put(columnName, value);
                    }
                }
            }
        }
        Map<String, List<ResultExtractor>> extractorWithTarget = new HashMap<>();

        /* Read Encounters */
        QueryJob jobDetailsEncounter = QueryBaseJobUtil.getJobDetails(JobConstants.ENCOUNTER);
        List<ResultExtractor> extractorsEncounters = getResultExtractorForCategoryAndId(jobDetailsEncounter,
                JobConstants.ENCOUNTER, Long.valueOf(encounterId));
        assert jobDetailsEncounter != null;
        setOrgUnitForEncounter(encounterId, extractorsEncounters);
        extractorWithTarget.put(jobDetailsEncounter.getTarget(), new ArrayList<>());
        extractorWithTarget.get(jobDetailsEncounter.getTarget()).addAll(extractorsEncounters);

        if (!CollectionUtils.isEmpty(deleted)) {
            deleted.forEach(d -> {
                Query q = new Query();
                q.setTable(d);
                q.setIgnore(true);
                q.getColAndVal().put("encounter_id", String.valueOf(encounterId));
                insertQueries.add(q);
            });
        }

        for (Map.Entry<String, Query> entry : obsColAndVal.entrySet()) {
            insertQueries.add(entry.getValue());
        }
        EncounterJobDto dto = new EncounterJobDto();
        dto.setInsertQueries(insertQueries);
        dto.setExtractorsWithTarget(extractorWithTarget);
        return dto;
    }

    private void setOrgUnitForEncounter(Integer encounterId, List<ResultExtractor> extractorsEncounters) {
        String facility = metaDataService.getFacilityNameForEncounter(encounterId);
        String district = metaDataService.getDistrictNameForEncounter(encounterId);
        String orgUnitCode = getOrgUnitCode(facility, district);
        List<Map<String, Object>> rowValues = extractorsEncounters.get(0).getRowValues();
        if(!CollectionUtils.isEmpty(rowValues)) {
            logger.info("OrgUnit code for encounterId {} : {}", encounterId, orgUnitCode);
            rowValues.get(0).put("org_unit", orgUnitCode);
        }
    }

    private String getOrgUnitCode(String facility, String district) {
        //Check readme for detailed explanation of logic.
        if(StringUtils.hasLength(facility) && StringUtils.hasLength(district)) {
            return "ZWNSC-OU-" + facility.toUpperCase() + "-" + district.toUpperCase();
        } else if (StringUtils.hasLength(facility)){
            return "ZWNSC-"+facility.toUpperCase();
        }
        return "";
    }

    private void initialiseQuery(String formTableName, Map<String, Query> obsColAndVal,
                                 String formNameWithInstance, Encounter encounter, FileAttributes file) {
        Query query = new Query();
        query.setTable(formTableName);
        obsColAndVal.put(formNameWithInstance, query);
        addExtraAttributeToQuery(query, encounter, file);
    }

    public void addExtraAttributeToQuery(Query query, Encounter encounter, FileAttributes file) {
        query.getColAndVal().put("encounter_id", String.valueOf(encounter.getEncounterId()));
        query.getColAndVal().put("visit_id", String.valueOf(encounter.getVisitId()));
        query.getColAndVal().put("patient_id", String.valueOf(encounter.getPatientId()));
        query.getColAndVal().put("patient_identifier", String.valueOf(encounter.getPatientId())); //TODO:can remove this
        query.getColAndVal().put("location_id", String.valueOf(encounter.getLocationId()));
        Location location = metaDataService.getLocationByEncounterId(encounter.getEncounterId());
        query.getColAndVal().put("location_name", location.getName());
        query.getColAndVal().put("date_created", String.valueOf(encounter.getEncounterDatetime()));
        query.getColAndVal().put("instance_id", String.valueOf(file.getInstance()));
    }

    @Override
    public JobParameters getJobParameters() {
        return super.getJobParameters();
    }

    public void throwExceptionIfFormVersionsMismatch(FileAttributes fileAttributes) {
        String formName = fileAttributes.getFormName();
        Integer version = fileAttributes.getVersion();
        Integer formVersion = metaDataService.findFormMetaDataDetailsForName(formName);
        String exceptionString;
        String comment;
        if(formVersion == null) {
            exceptionString = "Inconsistency as table needs to be created for form: " + formName +
                    " Use show_conflicts command from the command line util and create the table.";
            comment = "Create table: " + formName;
            metaDataService.addLogs(formName, comment, exceptionString, JobConstants.ERROR_STATUS.ERROR.toString());
            throw new PsiException(exceptionString);
        }
        if(formVersion < version) {
            exceptionString = "Table for form: "+ formName + " needs to be updated for sync to progress." +
                    " Use show_conflicts command from the command line util and update the table.";
            comment = "Update table: " + formName;
            metaDataService.addLogs(formName, comment, exceptionString, JobConstants.ERROR_STATUS.ERROR.toString());
            throw new PsiException(exceptionString);
        }
    }

}
