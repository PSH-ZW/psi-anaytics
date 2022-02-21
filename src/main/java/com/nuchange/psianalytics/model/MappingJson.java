package com.nuchange.psianalytics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown=true)
public class MappingJson {
    private Map<String, Map<String, Map<String, String>>> formTableMappings;
    private Map<String, String> dhisProgramStageId;

    public Map<String, Map<String, Map<String, String>>> getFormTableMappings() {
        return formTableMappings;
    }

    public void setFormTableMappings(Map<String, Map<String, Map<String, String>>> formTableMappings) {
        this.formTableMappings = formTableMappings;
    }

    public Map<String, String> getDhisProgramStageId() {
        return dhisProgramStageId;
    }

    public void setDhisProgramStageId(Map<String, String> dhisProgramStageId) {
        this.dhisProgramStageId = dhisProgramStageId;
    }
}
