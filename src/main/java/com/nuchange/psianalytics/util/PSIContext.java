package com.nuchange.psianalytics.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class PSIContext {

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
    private void assingMRSContext() {
        instance = this;
    }
}
