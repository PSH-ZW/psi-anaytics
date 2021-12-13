package com.nuchange.psianalytics.model;

import java.sql.Timestamp;

public class Patient {
    private Integer patientId;
    private Integer creator;
    private Timestamp dateCreated;
    private Integer changedBy;
    private Timestamp dateChanged;
    private Integer voided;
    private Integer voidedBy;
    private Timestamp dateVoided;
    private String voidReason;
    private String allergyStatus;

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Integer getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(Integer changedBy) {
        this.changedBy = changedBy;
    }

    public Timestamp getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(Timestamp dateChanged) {
        this.dateChanged = dateChanged;
    }

    public Integer getVoided() {
        return voided;
    }

    public void setVoided(Integer voided) {
        this.voided = voided;
    }

    public Integer getVoidedBy() {
        return voidedBy;
    }

    public void setVoidedBy(Integer voidedBy) {
        this.voidedBy = voidedBy;
    }

    public Timestamp getDateVoided() {
        return dateVoided;
    }

    public void setDateVoided(Timestamp dateVoided) {
        this.dateVoided = dateVoided;
    }

    public String getVoidReason() {
        return voidReason;
    }

    public void setVoidReason(String voidReason) {
        this.voidReason = voidReason;
    }

    public String getAllergyStatus() {
        return allergyStatus;
    }

    public void setAllergyStatus(String allergyStatus) {
        this.allergyStatus = allergyStatus;
    }
}
