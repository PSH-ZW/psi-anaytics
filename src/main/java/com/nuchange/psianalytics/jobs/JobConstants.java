package com.nuchange.psianalytics.jobs;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JobConstants {

    public static final String MRS_PATIENT = "patient";
    public static final String MRS_PATIENT_JOB = "mrsPatientJob";
    public static final String PATIENT_MRS_JOB = "PATIENT_MRS_JOB";
    public static final String PATIENT_MRS_JOB_STEP_ID = "PATIENT_MRS_JOB_STEP_ID";
    public static final String PATIENT_MRS_JOB_ITEM_READER_ID = "PATIENT_MRS_JOB_ITEM_READER_ID";
    public static final String PATIENT_MRS_JOB_ITEM_PROCESSOR_ID = "PATIENT_MRS_JOB_ITEM_PROCESSOR_ID";
    public static final String PATIENT_MRS_JOB_ITEM_WRITER_ID = "PATIENT_MRS_JOB_ITEM_WRITER_ID";

    public static final String ENCOUNTER_EVENT_BASE_JOB_STEP_ONE = "ENCOUNTER_EVENT_BASE_JOB_STEP_ONE";
    public static final String ENCOUNTER_EVENT_BASE_JOB_ITEM_READER_STEP_ONE = "ENCOUNTER_EVENT_BASE_JOB_ITEM_READER_STEP_ONE";
    public static final String ENCOUNTER_EVENT_BASE_JOB_ITEM_PROCESSOR_STEP_ONE = "ENCOUNTER_EVENT_BASE_JOB_ITEM_PROCESSOR_STEP_ONE";
    public static final String ENCOUNTER_EVENT_BASE_JOB_ITEM_WRITER_STEP_ONE = "ENCOUNTER_EVENT_BASE_JOB_ITEM_WRITER_STEP_ONE";

    public static final String TYPE = "type";
    public static final String JOB_TYPE_EVENT = "event_based";
    public static final String OBS_FLOWSHEET = "flowsheet";
    public static final String LABEL = "label";
    public static final String OBS_CONTROL_GROUP = "obsGroupControl";
    public static final String OBS_SECTION_CONTROL = "section";

    public static final String ENCOUNTER = "encounter";
    public static final String MRS_ENCOUNTER = "encounter";
    public static final String MRS_ENCOUNTER_JOB = "mrsEncounterJob";
    public static final String ENCOUNTER_EVENT_BASE_JOB = "ENCOUNTER_EVENT_BASE_JOB";
    public static final String VISIT_DIAGNOSIS_CONCEPT_UUID = "159947AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String CODED_DIAGNOSIS_CONCEPT_UUID = "1284AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String DIAGNOSIS_CERTAINTY_CONCEPT_UUID = "159394AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String DIAGNOSIS_ORDER_CONCEPT_UUID = "159946AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String NON_CODED_DIAGNOSIS_CONCEPT_UUID = "161602AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String BAHMNI_DIAGNOSIS_STATUS_CONCEPT_UUID = "ed247048-101d-4bcb-9761-2a276b558230";
    public static final String DIAGNOSIS_SCT_DESCRIPTION_CONCEPT_UUID = "0096c5fe-92ca-4080-bf99-2d0ee1b9215b";
    public static final String MULTI_SELECT = "multiSelect";
    public static final String TABLE = "table";
    public static final String ORDERS = "orders";
    public static final String DIAGNOSIS = "diagnosis";
    public static final String CODED_DIAGNOSIS_SCT_DESCRIPTION_COLUMN = "coded_diagnosis_sct_description";
    public static final String CODED_DIAGNOSIS_SCT_CONCEPT_ID_COLUMN = "sct2_concept_id";
    public static final String OBS_CONTROL = "obsControl";

    public static final Map<String, String> CATEGORY_TO_ANALYTICS_JOB = Collections.unmodifiableMap(new HashMap<String, String>() {
        {
            put(MRS_PATIENT, MRS_PATIENT_JOB);
            put(MRS_ENCOUNTER, MRS_ENCOUNTER_JOB);
        }
    });

    public static final Map<String, String> CATEGORY_TO_JOB = Collections.unmodifiableMap(new HashMap<String, String>() {
        {
            put(MRS_PATIENT, PATIENT_MRS_JOB);
            put(MRS_ENCOUNTER, ENCOUNTER_EVENT_BASE_JOB);
        }
    });

    public static final Map<String, String> CATEGORY_TO_EVENT = Collections.unmodifiableMap(new HashMap<String, String>() {
        {
            put(MRS_PATIENT, "patient");
            put(MRS_ENCOUNTER, "Encounter");
        }
    });


    public static final Integer PATIENT_UUID_POSITION = 6;
    public static final Integer ENCOUNTER_UUID_POSITION = 7;
    public static final Map<String, Integer> UUID_POSITION = Collections.unmodifiableMap(new HashMap<String, Integer>() {
        {
            put(MRS_PATIENT, PATIENT_UUID_POSITION);
            put(MRS_ENCOUNTER, ENCOUNTER_UUID_POSITION);
        }
    });
}
