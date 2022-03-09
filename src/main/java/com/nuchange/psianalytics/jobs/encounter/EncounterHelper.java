package com.nuchange.psianalytics.jobs.encounter;

import com.nuchange.psiutil.AnalyticsUtil;
import com.nuchange.psiutil.model.ObsType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class EncounterHelper {

    private static final Logger logger = LoggerFactory.getLogger(EncounterHelper.class);

    // <form,<concept, obsType>>
    private static Map<String, Map<String, ObsType>> formConceptMap = new HashMap<>();

    public Map<String, ObsType> getConceptObsTypeMapForForm(String formResourcePath) throws IOException {
        if (formConceptMap.containsKey(formResourcePath)) {
            return formConceptMap.get(formResourcePath);
        }
        logger.debug("Reading form json file: {}", formResourcePath);
        Map<String, ObsType> conceptMap = AnalyticsUtil.extractConceptsFromFile(formResourcePath);
        formConceptMap.put(formResourcePath, conceptMap);
        return conceptMap;
    }
}
