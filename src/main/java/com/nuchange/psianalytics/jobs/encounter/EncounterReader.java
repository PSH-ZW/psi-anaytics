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
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${form.baseDir}")
    protected String formDir;

    public EncounterReader(DataSource dataSource) {
        super(dataSource);
    }

    @Transactional
    public EncounterJobDto readEncounter(Integer encounterId) throws Exception {
        logger.debug("Processing Encounter : " + encounterId);
        Encounter encounter = metaDataService.getEncounterByEncounterId(encounterId);
        List<Obs> obsForEncounter = metaDataService.getObsByEncounterIdAndVoided(encounterId);
        Set<String> deleted = new HashSet<>();
        /* To Store All Queries which are generated */
        List<Query> insertQueries = new ArrayList<>();

        /* To Store Query For Insertion Which are generated according to each table */
        Map<String, Query> obsColAndVal = new HashMap<>();

        for (Obs obs : obsForEncounter) {
            FileAttributes file = new FileAttributes(obs.getFormNameSpaceAndPath());
            //TODO: below line needs to be uncommented post necessary meta_data is available
            String formTableName = AnalyticsUtil.generateColumnName(file.getFormName());
            if(metaDataService.shouldNotFlatterForm(formTableName)) {
                continue;
            }
            noMisMatch(file.getFullName());
            Map<String, ObsType> conceptMap = encounterHelper.getConceptObsTypeMapForForm(file.getFileName());
            Forms form = AnalyticsUtil.readForm(formDir + file.getFileName() + ".json");
            formTableName = AnalyticsUtil.generateColumnName(form.getName()); //TODO: this can be removed.
            Concept concept = metaDataService.getConceptByObsId(obs.getObsId());
            String conceptUuid = concept.getUuid();
            Map<UUID, String> columnNames = AnalyticsUtil.getColumnNamesForForm(form);
            String formNameWithInstance = formTableName + "_" + file.getInstance().toString();
            if (!obsColAndVal.containsKey(formNameWithInstance)) {
                initialiseQuery(formTableName, obsColAndVal, formNameWithInstance, encounter, file);
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

    private void initialiseQuery(String formTableName, Map<String, Query> obsColAndVal,
                                 String formNameWithInstance, Encounter encounter, FileAttributes file) {
        Query query = new Query();
        query.setTable(formTableName);
        obsColAndVal.put(formNameWithInstance, query);
        addExtraAttributeToQuery(query, encounter, file);
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
        query.getColAndVal().put("patient_identifier", String.valueOf(encounter.getPatientId())); //TODO:can remove this
        /*query.getColAndVal().put("provider_id", String.valueOf(provider.getProviderId()));*/
        query.getColAndVal().put("location_id", String.valueOf(encounter.getLocationId()));
        Location location = metaDataService.getLocationByEncounterId(encounter.getEncounterId());
        query.getColAndVal().put("location_name", location.getName());
        query.getColAndVal().put("date_created", String.valueOf(encounter.getEncounterDatetime()));
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

    public Boolean noMisMatch(String formNameSpacePath) throws RuntimeException {
        /*String formNameSpacePath = metaDataService.getFormNameSpacePathForEncounter(encounterId);*/
        String[] namePathFragment = formNameSpacePath.split("\\.");
        String obsFormName = namePathFragment[0].substring(namePathFragment[0].indexOf("^") + 1);
        Integer obsVersion = Integer.valueOf(namePathFragment[1].substring(0, namePathFragment[1].indexOf("/")));
        FormDetails formDetails = metaDataService.findFormMetaDataDetailsForName(obsFormName);
        if(formDetails == null) throw new RuntimeException("Inconsistency as Table needs to created for form:" + obsFormName);
        if(formDetails.getVersion() < obsVersion) throw new RuntimeException("Inconsistency in form version as form_meta_data version :"
                + formDetails.getVersion().toString() + "is lower than current obs form version :" + obsVersion);
        return true;
    }

}
