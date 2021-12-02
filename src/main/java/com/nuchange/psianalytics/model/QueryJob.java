package com.nuchange.psianalytics.model;

public class QueryJob {
    private String input;
    private String target;
    private String lastDayRecords;
    private String lastNRecords;
    private String firstRecord;
    private String existQuery;
    private String[] existQueryParams;
    private String primaryKey;
    private String fetchAll;
    private String[] children;
    private String dateColumn;
    private String parentKey;
    private String findIdByUuid;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getLastNRecords() {
        return lastNRecords;
    }

    public void setLastNRecords(String lastNRecords) {
        this.lastNRecords = lastNRecords;
    }

    public String getFirstRecord() {
        return firstRecord;
    }

    public void setFirstRecord(String firstRecord) {
        this.firstRecord = firstRecord;
    }

    public String getExistQuery() {
        return existQuery;
    }

    public void setExistQuery(String existQuery) {
        this.existQuery = existQuery;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getFetchAll() {
        return fetchAll;
    }

    public void setFetchAll(String fetchAll) {
        this.fetchAll = fetchAll;
    }

    public String[] getChildren() {
        return children;
    }

    public void setChildren(String[] children) {
        this.children = children;
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public String getFindIdByUuid() {
        return findIdByUuid;
    }

    public void setFindIdByUuid(String findIdByUuid) {
        this.findIdByUuid = findIdByUuid;
    }

    public String getLastDayRecords() {
        return lastDayRecords;
    }

    public void setLastDayRecords(String lastDayRecords) {
        this.lastDayRecords = lastDayRecords;
    }

    public String getDateColumn() {
        return dateColumn;
    }

    public void setDateColumn(String dateColumn) {
        this.dateColumn = dateColumn;
    }

    public String[] getExistQueryParams() {
        return existQueryParams;
    }

    public void setExistQueryParams(String[] existQueryParams) {
        this.existQueryParams = existQueryParams;
    }
}
