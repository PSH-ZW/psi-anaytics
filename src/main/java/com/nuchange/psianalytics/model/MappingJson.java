package com.nuchange.psianalytics.model;

import java.util.Map;

public class MappingJson {
    private Map<String, Map<String, String>> formTableMappings;
    private String dhisProgramStageId;

    public Map<String, Map<String, String>> getFormTableMappings() {
        return formTableMappings;
    }

    public void setFormTableMappings(Map<String, Map<String, String>> formTableMappings) {
        this.formTableMappings = formTableMappings;
    }

    public String getDhisProgramStageId() {
        return dhisProgramStageId;
    }

    public void setDhisProgramStageId(String dhisProgramStageId) {
        this.dhisProgramStageId = dhisProgramStageId;
    }
}
