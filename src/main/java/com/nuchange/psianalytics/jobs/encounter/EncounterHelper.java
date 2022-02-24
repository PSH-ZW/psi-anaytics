package com.nuchange.psianalytics.jobs.encounter;

import com.nuchange.psiutil.AnalyticsUtil;
import com.nuchange.psiutil.model.ObsType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class EncounterHelper {
    @Value("${form.baseDir}")
    protected String formDir;

    private static final Logger logger = LoggerFactory.getLogger(EncounterHelper.class);

    // <form,<concept, obsType>>
    private static Map<String, Map<String, ObsType>> formConceptMap = new HashMap<>();

    public Map<String, ObsType> getConceptObsTypeMapForForm(String fileName) throws IOException {
        if (formConceptMap.containsKey(fileName)) {
            return formConceptMap.get(fileName);
        }
        logger.debug("Reading form json file: {}", fileName);
        Map<String, ObsType> conceptMap = AnalyticsUtil.extractConceptsFromFile(formDir + fileName + ".json");
        formConceptMap.put(fileName, conceptMap);
        return conceptMap;
    }
}
