package com.nuchange.psianalytics.model;

import java.util.HashMap;
import java.util.Map;

public class Query {

    private String table;
    Map<String,String> colAndVal = new HashMap<>();
    String parentConcept;
    String parentTable;

    private boolean ignore = false;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Map<String, String> getColAndVal() {
        return colAndVal;
    }

    public void setColAndVal(Map<String, String> colAndVal) {
        this.colAndVal = colAndVal;
    }

    public String getParentConcept() {
        return parentConcept;
    }

    public void setParentConcept(String parentConcept) {
        this.parentConcept = parentConcept;
    }

    public String getParentTable() {
        return parentTable;
    }

    public void setParentTable(String parentTable) {
        this.parentTable = parentTable;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }
}
