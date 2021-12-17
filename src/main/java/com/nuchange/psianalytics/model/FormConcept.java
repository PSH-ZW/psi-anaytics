package com.nuchange.psianalytics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FormConcept {
    private String name;
    private String uuid;
    private List<ConceptAnswer> answers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<ConceptAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<ConceptAnswer> answers) {
        this.answers = answers;
    }
}
