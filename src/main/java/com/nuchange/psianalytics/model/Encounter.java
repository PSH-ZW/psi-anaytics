package com.nuchange.psianalytics.model;

import java.sql.Timestamp;

public class Encounter {

    private Integer encounterId;

    private Integer encounterType;

    private Integer patientId;

    private Integer locationId;

    private Integer formId;

    private Timestamp encounterDatetime;

    private Integer creator;

    private Timestamp dateCreated;

    private Integer voided ;

    private Integer voidedBy;

    private Timestamp dateVoided;

    private String voidReason;

    private Integer changedBy;

    private Timestamp dateChanged;

    private Integer visitId;

    private String uuid;

    public Integer getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(Integer encounterId) {
        this.encounterId = encounterId;
    }

    public Integer getEncounterType() {
        return encounterType;
    }

    public void setEncounterType(Integer encounterType) {
        this.encounterType = encounterType;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getFormId() {
        return formId;
    }

    public void setFormId(Integer formId) {
        this.formId = formId;
    }

    public Timestamp getEncounterDatetime() {
        return encounterDatetime;
    }

    public void setEncounterDatetime(Timestamp encounterDatetime) {
        this.encounterDatetime = encounterDatetime;
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

    public Integer getVisitId() {
        return visitId;
    }

    public void setVisitId(Integer visitId) {
        this.visitId = visitId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
//encounterDatetime, voided, voidedby, void reason, date voided, date changed, changed by, form id, location, encounter type
