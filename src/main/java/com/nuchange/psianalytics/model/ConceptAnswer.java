package com.nuchange.psianalytics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ConceptAnswer {
    private FormConcept name;

    public FormConcept getName() {
        return name;
    }

    public void setName(FormConcept name) {
        this.name = name;
    }
}
