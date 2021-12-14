package com.nuchange.psianalytics.model;

import java.sql.Timestamp;

public class Obs {

    private Integer obsId;

    private Integer personId;

    private Integer encounterId;

    private Integer conceptId;

    private Integer orderId;

    private Timestamp obsDatetime;

    private Integer locationId;

    private Integer obsGroupId;

    private String accessionNumber; 

    private Integer valueGroupId;

    private Integer valueCoded;

    private Integer valueCodedNameId;

    private Integer valueDrug;

    private Timestamp valueDatetime; 

    private Double valueNumeric;

    private String valueModifier; //Used for storing concrpt values of boolean type

    private String valueText;

    private String valueComplex; 

    private String comments;

    private Integer creator;

    private Timestamp dateCreated;

    private Integer voided;

    private Integer voidedBy;

    private Timestamp dateVoided; 

    private String voidReason;

    private String uuid;

    private Integer previousVersion;

    private String formNameSpaceAndPath;

    private String status;

    private String interpretation;

    public Integer getObsId() {
        return obsId;
    }

    public void setObsId(Integer obsId) {
        this.obsId = obsId;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public Integer getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(Integer encounterId) {
        this.encounterId = encounterId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Timestamp getObsDatetime() {
        return obsDatetime;
    }

    public void setObsDatetime(Timestamp obsDatetime) {
        this.obsDatetime = obsDatetime;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getObsGroupId() {
        return obsGroupId;
    }

    public void setObsGroupId(Integer obsGroupId) {
        this.obsGroupId = obsGroupId;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public Integer getValueGroupId() {
        return valueGroupId;
    }

    public void setValueGroupId(Integer valueGroupId) {
        this.valueGroupId = valueGroupId;
    }

    public Integer getValueCoded() {
        return valueCoded;
    }

    public void setValueCoded(Integer valueCoded) {
        this.valueCoded = valueCoded;
    }

    public Integer getValueCodedNameId() {
        return valueCodedNameId;
    }

    public void setValueCodedNameId(Integer valueCodedNameId) {
        this.valueCodedNameId = valueCodedNameId;
    }

    public Integer getValueDrug() {
        return valueDrug;
    }

    public void setValueDrug(Integer valueDrug) {
        this.valueDrug = valueDrug;
    }

    public Timestamp getValueDatetime() {
        return valueDatetime;
    }

    public void setValueDatetime(Timestamp valueDatetime) {
        this.valueDatetime = valueDatetime;
    }

    public Double getValueNumeric() {
        return valueNumeric;
    }

    public void setValueNumeric(Double valueNumeric) {
        this.valueNumeric = valueNumeric;
    }

    public String getValueModifier() {
        return valueModifier;
    }

    public void setValueModifier(String valueModifier) {
        this.valueModifier = valueModifier;
    }

    public String getValueText() {
        return valueText;
    }

    public void setValueText(String valueText) {
        this.valueText = valueText;
    }

    public String getValueComplex() {
        return valueComplex;
    }

    public void setValueComplex(String valueComplex) {
        this.valueComplex = valueComplex;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getPreviousVersion() {
        return previousVersion;
    }

    public void setPreviousVersion(Integer previousVersion) {
        this.previousVersion = previousVersion;
    }

    public String getFormNameSpaceAndPath() {
        return formNameSpaceAndPath;
    }

    public void setFormNameSpaceAndPath(String formNameSpaceAndPath) {
        this.formNameSpaceAndPath = formNameSpaceAndPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInterpretation() {
        return interpretation;
    }

    public void setInterpretation(String interpretation) {
        this.interpretation = interpretation;
    }

    public Integer getConceptId() {
        return conceptId;
    }

    public void setConceptId(Integer conceptId) {
        this.conceptId = conceptId;
    }
}
