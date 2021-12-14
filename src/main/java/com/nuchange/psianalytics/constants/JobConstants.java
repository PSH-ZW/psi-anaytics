package com.nuchange.psianalytics.constants;

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
    public static final String MULTI_SELECT = "multiSelect";
    public static final String TABLE = "table";
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