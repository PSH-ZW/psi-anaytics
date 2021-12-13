package com.nuchange.psianalytics.jobs.encounter;
import com.nuchange.psianalytics.jobs.JobConstants;
import com.nuchange.psianalytics.jobs.querybased.QueryBasedJobReader;
import com.nuchange.psianalytics.model.*;
import com.nuchange.psianalytics.util.AnalyticsUtil;
import com.nuchange.psianalytics.util.MetaDataService;
import com.nuchange.psianalytics.util.PSIContext;
import com.nuchange.psianalytics.util.QueryBaseJobUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.*;


public abstract class EncounterReader<D> extends QueryBasedJobReader<D> {
    private static final Logger logger = LoggerFactory.getLogger(EncounterReader.class);

    @Autowired
    private EncounterHelper encounterHelper;

    @Autowired
    protected MetaDataService metaDataService;

    public EncounterReader(DataSource dataSource) {
        super(dataSource);
    }

    @Transactional
    public EncounterJobDto readEncounter(Integer encounterId) throws Exception {
        logger.debug("Processing Encounter : " + encounterId);
        Encounter encounter = PSIContext.getInstance().getMetaDataService().getEncounterByEncounterId(encounterId);
        List<Obs> obsForEncounter = PSIContext.getInstance().getMetaDataService().getObsByEncounterIdAndVoided(encounterId, 0);
        List<Obs> voidedObs = PSIContext.getInstance().getMetaDataService().getObsByEncounterIdAndVoided(encounterId, 1);
        // adding voided obs to delete any obs got synced before and no non-voided obs are present
        obsForEncounter.addAll(voidedObs);
        Set<String> deleted = new HashSet<>();
        /* To Store Concepts Which in Form With Type as multi-select, table */
        Map<String, Map<String, ObsType>> conceptTypes = new HashMap<>();

        /* To Store All Queries which are generated */
        List<Query> insertQueries = new ArrayList<>();

        /* To Store Query For Insertion Which are generated according to each table */
        Map<String, Query> obsColAndVal = new HashMap<>();
        Map<String, List<Query>> obsColAndVal_multiselect = new HashMap<>();

        List<String> snomedConceptList = encounterHelper.getObsSnomedConceptList();

        for (Obs obs : obsForEncounter) {
            FileAttributes file = new FileAttributes(obs.getFormNameSpaceAndPath());
            Map<String, ObsType> conceptMap = encounterHelper.getConceptObsTypeMapForForm(file.getFileName());
            Forms form = AnalyticsUtil.readForm("forms/" + file.getFileName() + ".json");
            Concept concept = PSIContext.getInstance().getMetaDataService().getConceptByObsId(obs.getObsId());
            String conceptName = PSIContext.getInstance().getMetaDataService().getFullNameOfConceptByUuid(UUID.fromString(concept.getUuid()));
            String conceptUuid = concept.getUuid();
            String formTableName = AnalyticsUtil.generateColumnName(form.getName());
            String formNameWithInstance = formTableName + "_" + file.getInstance().toString();
            if (conceptMap.containsKey(conceptUuid)) {
                ObsType obsType = conceptMap.get(conceptUuid);
                if (obsType.getParentType() != null && obsType.getParentType().equals(JobConstants.OBS_SECTION_CONTROL)) {
                    conceptName = obsType.getLabel().getValue() + "_" + conceptName;
                }
                conceptName = AnalyticsUtil.getShortName(conceptName);
                conceptName = AnalyticsUtil.generateColumnName(conceptName);
                if (obsType.getControlType().equals(JobConstants.MULTI_SELECT)) {
                    if (obs.getObsGroupId() == null) {
                        if (obs.getVoided() == 1) {
                            deleted.add(formTableName + "_multiselect");
                            continue;
                        }
                        if (!obsColAndVal_multiselect.containsKey(formNameWithInstance + "_multiselect")) {
                            List<Query> list = new ArrayList<>();
                            Query query = new Query();
                            query.setTable(formTableName + "_multiselect");
                            list.add(query);
                            obsColAndVal_multiselect.put((formNameWithInstance + "_multiselect"), list);
                        }

                        List<Query> queryList = obsColAndVal_multiselect.get(formNameWithInstance + "_multiselect");
                        boolean inserted = false;
                        for (Query query : queryList) {
                            if (!query.getColAndVal().containsKey(conceptName)) {
                                query.getColAndVal().put(conceptName, MRSServiceUtil.getValueAsString(obs, Locale.ENGLISH));
                                /*if (snomedConceptList.contains(String.valueOf(obs.getConcept().getConceptId()))) {
//                                    logger.debug("Obs "+obs.getObsId());
                                    if (obs.getValueSct() != null) {
                                        BigInteger id = obs.getValueSct().getConceptId().getId();
                                        query.getColAndVal().put(conceptName + "_sct", id.toString());
                                    }
                                }*/
                                addExtraAttributeToQuery(query, encounter, file);
                                query.setIgnore(obs.getVoided() == 1? true: false);
                                inserted = true;
                                break;
                            }
                        }
                        if (!inserted) {
                            Query query = new Query();
                            query.setTable(formTableName + "_multiselect");
                            queryList.add(query);
                            query.getColAndVal().put(conceptName, MRSServiceUtil.getValueAsString(obs, Locale.ENGLISH));
                            /*if (snomedConceptList.contains(String.valueOf(obs.getConcept().getConceptId()))) {
                                if (obs.getValueSct() != null) {
                                    BigInteger id = obs.getValueSct().getConceptId().getId();
                                    query.getColAndVal().put(conceptName + "_sct", id.toString());
                                }
                            }*/
                            addExtraAttributeToQuery(query, encounter, file);
                            query.setIgnore(obs.getVoided() == 1? true: false);
                        }
                    } else {
                        Obs parentObs = findParentObs(obs);
                        Concept parentConcept = PSIContext.getInstance().getMetaDataService().getConceptByObsId(parentObs.getObsId());
                        String parentConceptName = PSIContext.getInstance().getMetaDataService().getFullNameOfConceptByUuid(UUID.fromString(parentConcept.getUuid()));
                        if (!obsColAndVal_multiselect.containsKey(formNameWithInstance + "_multiselect")) {
                            List<Query> list = new ArrayList<>();
                            Query query = new Query();
                            query.setTable(AnalyticsUtil.generateColumnName(parentConceptName) + "_multiselect");
                            list.add(query);
                            obsColAndVal_multiselect.put((formNameWithInstance + "_multiselect"), list);
                        }
                        List<Query> queryList = obsColAndVal_multiselect.get(formNameWithInstance + "_multiselect");
                        boolean inserted = false;
                        for (Query query : queryList) {
                            if (!query.getColAndVal().containsKey(conceptName)) {
                                query.getColAndVal().put(conceptName, MRSServiceUtil.getValueAsString(obs, Locale.ENGLISH));
                                query.getColAndVal().put("parent", null);
                                query.setParentTable(formTableName);
                                query.setParentConcept(AnalyticsUtil.generateColumnName(parentConceptName));
                                addExtraAttributeToQuery(query, encounter, file);
                                query.setIgnore(obs.getVoided() == 1? true: false);
                                inserted = true;
                                break;
                            }
                        }
                        if (!inserted) {
                            Query query = new Query();
                            query.setTable(AnalyticsUtil.generateColumnName(parentConceptName) + "_multiselect");
                            queryList.add(query);
                            query.getColAndVal().put(conceptName, MRSServiceUtil.getValueAsString(obs, Locale.ENGLISH));
                            query.getColAndVal().put("parent", null);
                            query.setParentTable(formTableName);
                            query.setParentConcept(AnalyticsUtil.generateColumnName(parentConceptName));
                            addExtraAttributeToQuery(query, encounter, file);
                            query.setIgnore(obs.getVoided() == 1? true: false);
                        }
                    }
                }

                if (obsType.getControlType().equals(JobConstants.TABLE)) {

                    if (obs.getObsGroupId() != null) {
                        generateQueryForObsGroup(obs, obs.getObsGroup(), obsColAndVal, file, form, encounter);
                    } else {
                        if (!obsColAndVal.containsKey(formNameWithInstance)) {
                            Query query = new Query();
                            query.setTable(formTableName);
                            obsColAndVal.put(formNameWithInstance, query);
                        }
                        Query query = obsColAndVal.get(formNameWithInstance);
                        query.getColAndVal().put(conceptName, MRSServiceUtil.getValueAsString(obs, Locale.ENGLISH));
                        addExtraAttributeToQuery(query, encounter, file);
                        query.setIgnore(obs.getVoided() == 1? true: false);
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

        /* Get Order For Particular Encounter */
/*        List<Orders> orders = MRSContext.getInstance().
                getOrdersService().getAllOrdersByEncounter(encounter);
        if (CollectionUtils.isEmpty(orders)) {
            insertQueries.add(createIgnoredQueryFrom(encounterId, JobConstants.LAB_ORDER));
            insertQueries.add(createIgnoredQueryFrom(encounterId, JobConstants.DRUG_ORDER));
        } else {
            boolean hasLabOrder = false;
            boolean hasDrugOrder = false;
            for (Orders order : orders) {
                QueryJob jobDetails = QueryBaseJobUtil.getJobDetails(JobConstants.ORDERS);
                List<ResultExtractor> extractors = getResultExtractorForCategoryAndId(jobDetails,
                        JobConstants.ORDERS, Long.valueOf(order.getOrderId()));
                if (!extractorWithTarget.containsKey(jobDetails.getTarget())) {
                    extractorWithTarget.put(jobDetails.getTarget(), new ArrayList<>());
                }
                extractorWithTarget.get(jobDetails.getTarget()).addAll(extractors);
                String orderType = order.getOrderType().getName();
                if (orderType != null && orderType.equals(JobConstants.DRUG_ORDERS_TYPE)) {
                    hasDrugOrder = true;
                    QueryJob drugOrderJob = QueryBaseJobUtil.getJobDetails(JobConstants.DRUG_ORDER);
                    extractors = getResultExtractorForCategoryAndId(drugOrderJob,
                            JobConstants.DRUG_ORDER, Long.valueOf(order.getOrderId()));
                    if (!extractorWithTarget.containsKey(drugOrderJob.getTarget())) {
                        extractorWithTarget.put(drugOrderJob.getTarget(), new ArrayList<>());
                    }
                    extractorWithTarget.get(drugOrderJob.getTarget()).addAll(extractors);
                } else if (orderType != null && orderType.equals(JobConstants.LAB_ORDER_TYPE)) {
                    hasLabOrder = true;
                    QueryJob labOrderJob = QueryBaseJobUtil.getJobDetails(JobConstants.LAB_ORDER);
                    extractors = getResultExtractorForCategoryAndId(labOrderJob,
                            JobConstants.LAB_ORDER, Long.valueOf(order.getOrderId()));
                    if (!extractorWithTarget.containsKey(labOrderJob.getTarget())) {
                        extractorWithTarget.put(labOrderJob.getTarget(), new ArrayList<>());
                    }
                    extractorWithTarget.get(labOrderJob.getTarget()).addAll(extractors);
                }
            }
            if (!hasDrugOrder) {
                insertQueries.add(createIgnoredQueryFrom(encounterId, JobConstants.DRUG_ORDER));
            }
            if (!hasLabOrder) {
                insertQueries.add(createIgnoredQueryFrom(encounterId, JobConstants.LAB_ORDER));
            }
        }*/
        /*Patient patient = encounter.getPatient();*/

        /* Get All Diagnosis For An Encounter */
/*        List<ResultExtractor> diagnosisExtractors = new ArrayList<>();
        List<Obs> visitDiagnosisList = MRSContext.getInstance().getObsService().getObsAtTopLevelByEncounterId(encounterId, JobConstants.VISIT_DIAGNOSIS_CONCEPT_UUID);
        if (!CollectionUtils.isEmpty(visitDiagnosisList)) {
            logger.debug("Visit Diagnosis found " + visitDiagnosisList.size());
            EncounterProvider encounterProvider = MRSContext.getInstance().getEncounterProviderService().getEncounterProviderByEncounter(encounter);
            Provider provider = encounterProvider.getProviderId();
            for (Obs diagnosisObsGrp : visitDiagnosisList) {
                Set<Obs> memberObs = diagnosisObsGrp.getGroupMembers();
                if (!CollectionUtils.isEmpty(memberObs)) {
                    String conceptName;
                    Object obsValue = null;
                    boolean hasDiagnosis = false;
                    Set<String> colHeaders = new HashSet<>();
                    Map<String, Object> values = new HashMap<>();
                    for (Obs obs : memberObs) {
                        if (obs.getVoided() == 1) {
                            continue;
                        }
                        Concept concept = obs.getConcept();
                        conceptName = AnalyticsUtil.generateColumnName(AnalyticsUtil.getShortName(concept.getFullySpecifiedName(Locale.ENGLISH).getName()));
//                        logger.debug("Processing obs " + conceptName);
                        // coded diagnosis
                        if (concept.getUuid().equals(JobConstants.CODED_DIAGNOSIS_CONCEPT_UUID)) {
                            Concept valueCoded = obs.getValueCoded();
                            obsValue = valueCoded.getFullySpecifiedName(Locale.ENGLISH). getName();
                            hasDiagnosis = true;
                        } else if (concept.getUuid().equals(JobConstants.DIAGNOSIS_SCT_DESCRIPTION_CONCEPT_UUID)) {
                            // snomed coded diagnosis
                            SnomedDescription valueSct = obs.getValueSct();
                            colHeaders.add(JobConstants.CODED_DIAGNOSIS_SCT_DESCRIPTION_COLUMN);
                            values.put(JobConstants.CODED_DIAGNOSIS_SCT_DESCRIPTION_COLUMN, valueSct.getTerm());
                            conceptName = JobConstants.CODED_DIAGNOSIS_SCT_CONCEPT_ID_COLUMN;
                            obsValue = valueSct.getConceptId().getId().longValue();
                            hasDiagnosis = true;
                        } else if (concept.getUuid().equals(JobConstants.NON_CODED_DIAGNOSIS_CONCEPT_UUID)) {
                            // non coded diagnosis
                            obsValue = obs.getValueText();
                            hasDiagnosis = true;
                        } else if (concept.getUuid().equals(JobConstants.DIAGNOSIS_CERTAINTY_CONCEPT_UUID)) {
                            // diagnosis certainity
                            Concept valueCoded = obs.getValueCoded();
                            obsValue = valueCoded.getFullySpecifiedName(Locale.ENGLISH). getName();
                        } else if (concept.getUuid().equals(JobConstants.DIAGNOSIS_ORDER_CONCEPT_UUID)) {
                            // diagnosis order
                            Concept valueCoded = obs.getValueCoded();
                            obsValue = valueCoded.getFullySpecifiedName(Locale.ENGLISH). getName();
                        } else if (concept.getUuid().equals(JobConstants.BAHMNI_DIAGNOSIS_STATUS_CONCEPT_UUID)) {
                            // bahmni diagnosis status
                            Concept valueCoded = obs.getValueCoded();
                            obsValue = valueCoded.getFullySpecifiedName(Locale.ENGLISH). getName();
                        } else {
                            // unknown concept obs
                            continue;
                        }
                        values.put(conceptName, obsValue);
                        colHeaders.add(conceptName);
                    }

                    if (hasDiagnosis) {
                        colHeaders.add("parent_id");
                        colHeaders.add("encounter_id");
                        colHeaders.add("patient_identifier");
                        colHeaders.add("provider");
                        colHeaders.add("location_id");
                        colHeaders.add("location_name");
                        colHeaders.add("date_created");
                        values.put("encounter_id", encounterId);
                        values.put("parent_id", diagnosisObsGrp.getObsId());
                        values.put("patient_identifier", String.valueOf(encounter.getPatientId()));
                        *//*values.put("provider", provider.getName());*//*
                        values.put("location_id", location.getLocationId());
                        values.put("location_name", location.getName());
                        values.put("date_created", encounter.getEncounterDatetime());

                        ResultExtractor resultExtractor = new ResultExtractor();
                        resultExtractor.setTarget(JobConstants.DIAGNOSIS);
                        resultExtractor.setCategory(JobConstants.DIAGNOSIS);
                        resultExtractor.setProcessingId(Math.toIntExact(diagnosisObsGrp.getObsId()));

                        resultExtractor.setColHeaders(new ArrayList<>(colHeaders));
                        resultExtractor.setRowValues(Collections.singletonList(values));
                        diagnosisExtractors.add(resultExtractor);
                    }
                }
            }
        } else {
            insertQueries.add(createIgnoredQueryFrom(encounterId, JobConstants.DIAGNOSIS));
        }*/

//        extractorWithTarget.put(JobConstants.DIAGNOSIS, diagnosisExtractors);

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
        for (String key : obsColAndVal_multiselect.keySet()) {
            insertQueries.addAll(obsColAndVal_multiselect.get(key));
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
        Location location = PSIContext.getInstance().getMetaDataService().getLocationByEncounterId(encounter.getEncounterId());
        query.getColAndVal().put("location_name", location.getName());
        query.getColAndVal().put("date_created", String.valueOf(encounter.getEncounterDatetime()));
        query.getColAndVal().put("instance_id", String.valueOf(file.getInstance()));
    }

    private void generateQueryForObsGroup(Obs currentObs, Obs parentObs, Map<String, Query> obsColAndVal,
                                           FileAttributes file, Forms forms, Encounter encounter) {
        if(parentObs != null && parentObs.getObsGroup() != null) {
            generateQueryForObsGroup(parentObs, parentObs.getObsGroup(), obsColAndVal, file, forms, encounter);
        }
        Concept parentConcept = PSIContext.getInstance().getMetaDataService().getConceptByObsId(parentObs.getObsId());
        String formTableName = AnalyticsUtil.generateColumnName(forms.getName());
        String parentConceptName = PSIContext.getInstance().getMetaDataService().getFullNameOfConceptByUuid(UUID.fromString(parentConcept.getUuid()));
        String tableName = forms.getName() + "_" + AnalyticsUtil.generateColumnName(parentConceptName);
        tableName = AnalyticsUtil.generateColumnName(tableName);
        String tableNameWithInstance = tableName + "_" + file.getInstance().toString();
        if (!obsColAndVal.containsKey(tableNameWithInstance)) {
            Query query = new Query();
            query.setTable(tableName);
            obsColAndVal.put(tableNameWithInstance, query);
        }
        Concept currentConcept = PSIContext.getInstance().getMetaDataService().getConceptByObsId(parentObs.getObsId());
        String conceptName = PSIContext.getInstance().getMetaDataService().getFullNameOfConceptByUuid(UUID.fromString(currentConcept.getUuid()));
        conceptName = AnalyticsUtil.generateColumnName(conceptName);
        Query query = obsColAndVal.get(tableNameWithInstance);
        query.setIgnore(currentObs.getVoided() == 1? true: false);
        query.getColAndVal().put(conceptName, MRSServiceUtil.getValueAsString(currentObs, Locale.ENGLISH));
        query.getColAndVal().put("parent_id",  null);
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

}
