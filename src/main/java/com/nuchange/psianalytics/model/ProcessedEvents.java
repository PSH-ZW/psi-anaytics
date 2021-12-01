package com.nuchange.psianalytics.model;

public class ProcessedEvents {

    private Integer id;
    private String source;
    private Integer lastProcessedId;
    private String lastProcessedUuid;
    private String category;
    private String name;
    private Boolean locked = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getLastProcessedId() {
        return lastProcessedId;
    }

    public void setLastProcessedId(Integer lastProcessedId) {
        this.lastProcessedId = lastProcessedId;
    }

    public String getLastProcessedUuid() {
        return lastProcessedUuid;
    }

    public void setLastProcessedUuid(String lastProcessedUuid) {
        this.lastProcessedUuid = lastProcessedUuid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }
}
