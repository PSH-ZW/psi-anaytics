package com.nuchange.psianalytics.model;

public class Mapping {
    private String programName;
    private String mappingJson; //TODO: change to Map<String, Map<String, String>> only.
    private String dhisProgramStageId;

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getMappingJson() {
        return mappingJson;
    }

    public void setMappingJson(String mappingJson) {
        this.mappingJson = mappingJson;
    }

    public String getDhisProgramStageId() {
        return dhisProgramStageId;
    }

    public void setDhisProgramStageId(String dhisProgramStageId) {
        this.dhisProgramStageId = dhisProgramStageId;
    }
}
