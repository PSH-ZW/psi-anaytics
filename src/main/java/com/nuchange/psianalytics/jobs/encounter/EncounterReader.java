package com.nuchange.psianalytics.jobs.encounter;

import com.nuchange.psianalytics.constants.JobConstants;
import com.nuchange.psianalytics.jobs.querybased.QueryBasedJobReader;
import com.nuchange.psianalytics.model.*;
//import com.nuchange.psianalytics.util.AnalyticsUtil;
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
import java.util.*;


public abstract class EncounterReader<D> extends QueryBasedJobReader<D> {
    private static final Logger logger = LoggerFactory.getLogger(EncounterReader.class);
    @Autowired
    protected MetaDataService metaDataService;
    @Autowired
    protected EncounterHelper encounterHelper;

    public EncounterReader(DataSource dataSource) {
        super(dataSource);
    }

    @Transactional
    public EncounterJobDto readEncounter(Integer encounterId) throws Exception {
        logger.debug("Processing Encounter : " + encounterId);
        Encounter encounter = metaDataService.getEncounterByEncounterId(encounterId);
        List<Obs> obsForEncounter = metaDataService.getObsByEncounterIdAndVoided(encounterId, 0);
        List<Obs> voidedObs = metaDataService.getObsByEncounterIdAndVoided(encounterId, 1);
        // adding voided obs to delete any obs got synced before and no non-voided obs are present
        obsForEncounter.addAll(voidedObs);
        Set<String> deleted = new HashSet<>();
        /* To Store All Queries which are generated */
        List<Query> insertQueries = new ArrayList<>();

        /* To Store Query For Insertion Which are generated according to each table */
        Map<String, Query> obsColAndVal = new HashMap<>();

        for (Obs obs : obsForEncounter) {
            //TODO: we are doing this for each obs, check whether we can group obs belonging to one form.
            FileAttributes file = new FileAttributes(obs.getFormNameSpaceAndPath());
            //TODO: below line needs to be uncommented post necessary meta_data is available
            /*noMisMatch(file.getFullName());*/
            Map<String, ObsType> conceptMap = encounterHelper.getConceptObsTypeMapForForm(file.getFileName());
            Forms form = AnalyticsUtil.readForm("forms/" + file.getFileName() + ".json");
            Concept concept = metaDataService.getConceptByObsId(obs.getObsId());
            String conceptUuid = concept.getUuid();
            String formTableName = AnalyticsUtil.generateColumnName(form.getName());
            Map<UUID, String> columnNames = AnalyticsUtil.getColumnNamesForForm(form);
            String formNameWithInstance = formTableName + "_" + file.getInstance().toString();
            if (conceptMap.containsKey(conceptUuid)) {
                ObsType obsType = conceptMap.get(conceptUuid);
                String columnName = columnNames.get(UUID.fromString(conceptUuid));
                if (obsType.getControlType().equals(JobConstants.MULTI_SELECT)) {
                    //TODO: handle voiding
                    //TODO: initialise query if this is the first one.

                    List<UUID> conceptNameUuids = metaDataService.getConceptNameUuidsForConcept(obs.getValueCoded());
                    for (UUID conceptNameUuid : conceptNameUuids) {
                        //The forms use the uuid of the concept_name (instead of uuid of the concept) for multiselect answers.
                        // Get all concept names for the concept and check if the uuid of that concept name is in the columnName map
                        if(columnNames.containsKey(conceptNameUuid)) {
                            columnName = columnNames.get(conceptNameUuid);
                            break;
                        }
                    }
                    Query query = obsColAndVal.get(formNameWithInstance);
                    query.getColAndVal().put(columnName, "true");
                }

                if (obsType.getControlType().equals(JobConstants.TABLE)) {

                    if (obs.getObsGroupId() != null) {
                        Obs obsGroup = metaDataService.getObsGroup(obs);
                        generateQueryForObsGroup(obs, obsGroup, obsColAndVal, file, form, encounter);
                    } else {
                        if (!obsColAndVal.containsKey(formNameWithInstance)) {
                            Query query = new Query();
                            query.setTable(formTableName);
                            obsColAndVal.put(formNameWithInstance, query);
                            addExtraAttributeToQuery(query, encounter, file);
                        }
                        if (!Objects.equals(obsType.getParentType(), JobConstants.OBS_CONTROL_GROUP)) {
                            Query query = obsColAndVal.get(formNameWithInstance);
                            query.getColAndVal().put(columnName, metaDataService.getValueAsString(obs, Locale.ENGLISH));
                            query.setIgnore(obs.getVoided() == 1);
                        }
                    }
                }
            }
        }
        Map<String, List<ResultExtractor>> extractorWithTarget = new HashMap<>();

        /* Read Encounters */
        QueryJob jobDetailsEncounter = QueryBaseJobUtil.getJobDetails(JobConstants.ENCOUNTER);
        List<ResultExtractor> extractorsEncounters = getResultExtractorForCategoryAndId(jobDetailsEncounter,
                JobConstants.ENCOUNTER, Long.valueOf(encounterId));
        if (!extractorWithTarget.containsKey(jobDetailsEncounter.getTarget())) {
            extractorWithTarget.put(jobDetailsEncounter.getTarget(), new ArrayList<>());
        }
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

        for (String key : obsColAndVal.keySet()) {
            insertQueries.add(obsColAndVal.get(key));
        }
        EncounterJobDto dto = new EncounterJobDto();
        dto.setInsertQueries(insertQueries);
        dto.setExtractorsWithTarget(extractorWithTarget);
        return dto;
    }

    private Query createIgnoredQueryFrom(Integer encounterId, String table) {
        Query diagnosisQuery = new Query();
        diagnosisQuery.setTable(table);
        diagnosisQuery.getColAndVal().put("encounter_id", String.valueOf(encounterId));
        diagnosisQuery.setIgnore(true);
        return diagnosisQuery;
    }

    public void addExtraAttributeToQuery(Query query, Encounter encounter, FileAttributes file) {
        /*EncounterProvider encounterProvider = MRSContext.getInstance().getEncounterProviderService().getEncounterProviderByEncounter(encounter);
        Provider provider = encounterProvider.getProviderId();*/

        query.getColAndVal().put("encounter_id", String.valueOf(encounter.getEncounterId()));
        query.getColAndVal().put("visit_id", String.valueOf(encounter.getVisitId()));
        query.getColAndVal().put("patient_id", String.valueOf(encounter.getPatientId()));
        query.getColAndVal().put("patient_identifier", String.valueOf(encounter.getPatientId()));
        /*query.getColAndVal().put("provider_id", String.valueOf(provider.getProviderId()));*/
        query.getColAndVal().put("location_id", String.valueOf(encounter.getLocationId()));
        Location location = metaDataService.getLocationByEncounterId(encounter.getEncounterId());
        query.getColAndVal().put("location_name", location.getName());
        query.getColAndVal().put("date_created", String.valueOf(encounter.getEncounterDatetime()));
        query.getColAndVal().put("instance_id", String.valueOf(file.getInstance()));
    }

    private void generateQueryForObsGroup(Obs currentObs, Obs parentObs, Map<String, Query> obsColAndVal,
                                          FileAttributes file, Forms forms, Encounter encounter) {
        if (parentObs != null) {
            Obs obsGroup = metaDataService.getObsGroup(parentObs);
            if (obsGroup != null) {
                generateQueryForObsGroup(parentObs, obsGroup, obsColAndVal, file, forms, encounter);
            }
        }
        Concept parentConcept = metaDataService.getConceptByObsId(parentObs.getObsId());
        String formTableName = AnalyticsUtil.generateColumnName(forms.getName());
        String parentConceptName = metaDataService.getFullNameOfConceptByUuid(UUID.fromString(parentConcept.getUuid()));
        String tableName = forms.getName() + "_" + AnalyticsUtil.generateColumnName(parentConceptName);
        tableName = AnalyticsUtil.generateColumnName(tableName);
        String tableNameWithInstance = tableName + "_" + file.getInstance().toString();
        if (!obsColAndVal.containsKey(tableNameWithInstance)) {
            Query query = new Query();
            query.setTable(tableName);
            obsColAndVal.put(tableNameWithInstance, query);
            addExtraAttributeToQuery(query, encounter, file);
        }
        Concept currentConcept = metaDataService.getConceptByObsId(parentObs.getObsId());
        String conceptName = metaDataService.getFullNameOfConceptByUuid(UUID.fromString(currentConcept.getUuid()));
        conceptName = AnalyticsUtil.generateColumnName(conceptName);
        Query query = obsColAndVal.get(tableNameWithInstance);
        query.setIgnore(currentObs.getVoided() == 1 ? true : false);
        query.getColAndVal().put(conceptName, metaDataService.getValueAsString(currentObs, Locale.ENGLISH));
        query.getColAndVal().put("parent_id", null);
        query.setParentTable(formTableName);
        query.setParentConcept(AnalyticsUtil.generateColumnName(parentConceptName));
        query.getColAndVal().put("encounter_id", String.valueOf(encounter.getEncounterId()));
        query.getColAndVal().put("visit_id", String.valueOf(encounter.getVisitId()));
        query.getColAndVal().put("patient_id", String.valueOf(encounter.getPatientId()));
        query.getColAndVal().put("instance_id", String.valueOf(file.getInstance()));
    }

    private Obs findParentObs(Obs obs) {
        Integer obsGroupId = obs.getObsGroupId();
        while (obsGroupId != null) {
            obs = metaDataService.getObsById(obsGroupId);
            obsGroupId = obs.getObsGroupId();
        }
        return obs;
    }

    public void saveJobParameters(JobParameters jobParameters) {
        setJobParameter(jobParameters);
    }

    public JobParameters getJobParameters() {
        return super.getJobParameters();
    }

    public Boolean noMisMatch(String formNameSpacePath) throws Exception {
        /*String formNameSpacePath = metaDataService.getFormNameSpacePathForEncounter(encounterId);*/
        String[] namePathFragment = formNameSpacePath.split("\\.");
        String obsFormName = namePathFragment[0].substring(namePathFragment[0].indexOf("^") + 1);
        Integer obsVersion = Integer.valueOf(namePathFragment[1].substring(0, namePathFragment[1].indexOf("/")));
        FormDetails formDetails = metaDataService.findFormMetaDataDetailsForName(obsFormName);
        if(formDetails.getVersion() < obsVersion) throw new Exception("Inconsistency in form version as form_meta_data version :"
                + formDetails.getVersion().toString() + "is lower than current obs form version :" + obsVersion.toString());
        return true;
    }

}
