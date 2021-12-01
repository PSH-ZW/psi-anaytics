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

    public static final String TYPE = "type";
    public static final String JOB_TYPE_BASE = "base";
    public static final String JOB_TYPE_EVENT = "event_based";

    public static final Map<String, String> CATEGORY_TO_ANALYTICS_JOB = Collections.unmodifiableMap(new HashMap<String, String>() {
        {
            put(MRS_PATIENT, MRS_PATIENT_JOB);
        }
    });

    public static final Map<String, String> CATEGORY_TO_JOB = Collections.unmodifiableMap(new HashMap<String, String>() {
        {
            put(MRS_PATIENT, PATIENT_MRS_JOB);
        }
    });

    public static final Map<String, String> CATEGORY_TO_EVENT = Collections.unmodifiableMap(new HashMap<String, String>() {
        {

            put(MRS_PATIENT, "FhirPatientExt");
        }
    });
}
