package com.nuchange.psianalytics.model;

import java.util.ArrayList;
import java.util.List;

public class FormTable {

    private String name;
    private List<FormConcept> concepts;
    private String parent;
    private FormCotrolProperty properties;

    public FormTable(String name) {
        this.name = name;
        concepts = new ArrayList<FormConcept>();
        parent = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FormConcept> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<FormConcept> concepts) {
        this.concepts = concepts;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public FormCotrolProperty getProperties() {
        return properties;
    }

    public void setProperties(FormCotrolProperty properties) {
        this.properties = properties;
    }
}
