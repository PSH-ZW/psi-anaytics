package com.nuchange.psianalytics.model;

import java.sql.Timestamp;

public class Concept {

    private Integer conceptId;
    private Integer retired;
    private String shortName;
    private String description;
    private String formText;
    private Integer isSet;
    private String version;
    private Timestamp dateCreated;
    private Timestamp dateChanged;
    private Timestamp dateRetired;
    private String retireReason;
    private String uuid;

    public Integer getConceptId() {
        return conceptId;
    }

    public void setConceptId(Integer conceptId) {
        this.conceptId = conceptId;
    }

    public Integer getRetired() {
        return retired;
    }

    public void setRetired(Integer retired) {
        this.retired = retired;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormText() {
        return formText;
    }

    public void setFormText(String formText) {
        this.formText = formText;
    }

    public Integer getIsSet() {
        return isSet;
    }

    public void setIsSet(Integer isSet) {
        this.isSet = isSet;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(Timestamp dateChanged) {
        this.dateChanged = dateChanged;
    }

    public Timestamp getDateRetired() {
        return dateRetired;
    }

    public void setDateRetired(Timestamp dateRetired) {
        this.dateRetired = dateRetired;
    }

    public String getRetireReason() {
        return retireReason;
    }

    public void setRetireReason(String retireReason) {
        this.retireReason = retireReason;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
