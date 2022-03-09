package com.nuchange.psianalytics.jobs.encounter;

import com.nuchange.psianalytics.constants.JobConstants;
import com.nuchange.psianalytics.jobs.querybased.QueryBasedJobReader;
import com.nuchange.psianalytics.model.*;
import com.nuchange.psianalytics.util.MetaDataService;
import com.nuchange.psianalytics.util.QueryBaseJobUtil;
import com.nuchange.psiutil.AnalyticsUtil;
import com.nuchange.psiutil.model.FormDetails;
import com.nuchange.psiutil.model.Forms;
import com.nuchange.psiutil.model.ObsType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
            if(metaDataService.shouldNotFlattenForm(formTableName)) {
                continue;
            }
            throwExceptionIfFormVersionsMismatch(fileAttributes);
            String formResourcePath = metaDataService.getFormResourcePath(fileAttributes.getFormName(), fileAttributes.getVersion());
            Map<String, ObsType> conceptMap = encounterHelper.getConceptObsTypeMapForForm(formResourcePath);
            Forms form = AnalyticsUtil.readForm(formResourcePath);
            Concept concept = metaDataService.getConceptByObsId(obs.getObsId());
            String conceptUuid = concept.getUuid();
            Map<UUID, String> columnNames = AnalyticsUtil.getColumnNamesForForm(form);
            String formNameWithInstance = formTableName + "_" + fileAttributes.getInstance().toString();
            if (!obsColAndVal.containsKey(formNameWithInstance)) {
                initialiseQuery(formTableName, obsColAndVal, formNameWithInstance, encounter, fileAttributes);
            }
            Query query = obsColAndVal.get(formNameWithInstance);
            if (conceptMap.containsKey(conceptUuid)) {
                ObsType obsType = conceptMap.get(conceptUuid);
                String columnName = columnNames.get(UUID.fromString(conceptUuid));
                if (obsType.getControlType().equals(JobConstants.MULTI_SELECT)) {
                    //TODO: handle voiding
                    List<UUID> conceptNameUuids = metaDataService.getConceptNameUuidsForConcept(obs.getValueCoded());
                    for (UUID conceptNameUuid : conceptNameUuids) {
                        //The forms use the uuid of the concept_name (instead of uuid of the concept) for multiselect answers.
                        // Get all concept names for the concept and check if the uuid of that concept name is in the columnName map
                        if(columnNames.containsKey(conceptNameUuid)) {
                            columnName = columnNames.get(conceptNameUuid);
                            break;
                        }
                    }
                    String value = obs.getVoided() != 1 ? "true" : "";
                    query.getColAndVal().put(columnName, value);
                }
                if (obsType.getControlType().equals(JobConstants.TABLE)) {
                    String value = obs.getVoided() != 1 ? metaDataService.getValueAsString(obs, Locale.ENGLISH) : "";
                    query.getColAndVal().put(columnName, value);
                }
            }
        }
        Map<String, List<ResultExtractor>> extractorWithTarget = new HashMap<>();

        /* Read Encounters */
        QueryJob jobDetailsEncounter = QueryBaseJobUtil.getJobDetails(JobConstants.ENCOUNTER);
        List<ResultExtractor> extractorsEncounters = getResultExtractorForCategoryAndId(jobDetailsEncounter,
                JobConstants.ENCOUNTER, Long.valueOf(encounterId));
        assert jobDetailsEncounter != null;
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
        FormDetails formDetails = metaDataService.findFormMetaDataDetailsForName(formName);
        if(formDetails == null) {
            //TODO: should we add this error to the logs table?
            throw new RuntimeException("Inconsistency as Table needs to be created for form:" + formName);
        }
        if(formDetails.getVersion() < version) {
            throw new RuntimeException("Inconsistency in form version as form_meta_data version :"
                    + formDetails.getVersion().toString() + "is lower than current obs form version :" + version);
        }
    }

}
