package com.nuchange.psianalytics.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class PSIContext {

    @Value("${country.org.unit.for.orgunit.sync}")
    private String orgUnitId;

    @Autowired
    private MetaDataService metaDataService;

    private static PSIContext instance;

    public static PSIContext getInstance() {
        return instance;
    }

    public static void setInstance(PSIContext instance) {
        PSIContext.instance = instance;
    }

    public MetaDataService getMetaDataService() {
        return metaDataService;
    }

    public void setMetaDataService(MetaDataService metaDataService) {
        this.metaDataService = metaDataService;
    }

    @PostConstruct
    private void assignMRSContext() {
        instance = this;
    }

    public String getOrgUnitId() {
        return this.orgUnitId;
    }
}
