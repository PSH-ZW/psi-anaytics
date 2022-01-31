package com.nuchange.psianalytics.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EncounterJobDto {

    private List<Query> insertQueries;
    private Map<String, List<ResultExtractor>> extractorsWithTarget;

    public EncounterJobDto() {
        insertQueries = new ArrayList<>();
        extractorsWithTarget = new HashMap<>();
    }

    public List<Query> getInsertQueries() {
        return insertQueries;
    }

    public void setInsertQueries(List<Query> insertQueries) {
        this.insertQueries = insertQueries;
    }

    public Map<String, List<ResultExtractor>> getExtractorsWithTarget() {
        return extractorsWithTarget;
    }

    public void setExtractorsWithTarget(Map<String, List<ResultExtractor>> extractorsWithTarget) {
        this.extractorsWithTarget = extractorsWithTarget;
    }
}
