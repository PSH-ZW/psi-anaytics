package com.nuchange.psianalytics.jobs.encounter;

import com.nuchange.psianalytics.model.ObsType;
import com.nuchange.psianalytics.util.AnalyticsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EncounterHelper {
    private static final Logger logger = LoggerFactory.getLogger(EncounterHelper.class);

    private static List<String> snomedConceptList = null;
    // <form,<concept, obsType>>
    private static Map<String, Map<String, ObsType>> formConceptMap = new HashMap<>();

    @Autowired
    private AnalyticsMetadataService metaDataService;

    public List<String> getObsSnomedConceptList() {
        if (snomedConceptList == null) {
            String snomedConceptCsv = metaDataService.getProperty("obs.snomed.concept", "");
            snomedConceptList = Arrays.asList(snomedConceptCsv.split(","));
        }
        return snomedConceptList;
    }

    public Map<String, ObsType> getConceptObsTypeMapForForm(String fileName) throws IOException {
        if (formConceptMap.containsKey(fileName)) {
            return formConceptMap.get(fileName);
        }
        logger.debug("Reading form json file: " + fileName);
        Map<String, ObsType> conceptMap = AnalyticsUtil.extractConceptsFromFile("forms/" + fileName + ".json");
        formConceptMap.put(fileName, conceptMap);
        return conceptMap;
    }
}
