package com.nuchange.psianalytics.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ResultExtractor {
    private List<String> colHeaders;
    private List<Map<String, Object>> rowValues;
    private Integer processingId;
    private String category;
    private String target;
    private Date currentDate;

    public List<String> getColHeaders() {
        return colHeaders;
    }

    public void setColHeaders(List<String> colHeaders) {
        this.colHeaders = colHeaders;
    }

    public List<Map<String, Object>> getRowValues() {
        return rowValues;
    }

    public void setRowValues(List<Map<String, Object>> rowValues) {
        this.rowValues = rowValues;
    }

    public Integer getProcessingId() {
        return processingId;
    }

    public void setProcessingId(Integer processingId) {
        this.processingId = processingId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }
}
