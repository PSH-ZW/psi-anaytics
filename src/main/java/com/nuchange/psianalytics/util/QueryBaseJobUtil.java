package com.nuchange.psianalytics.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuchange.psianalytics.model.QueryJob;
import com.nuchange.psianalytics.model.QueryJobs;

import java.io.IOException;

public class QueryBaseJobUtil {

    public static QueryJob getJobDetails(String category) {
        ObjectMapper mapper = new ObjectMapper();
        QueryJobs analyticsReader;
        try {
            analyticsReader = mapper.readValue(QueryBaseJobUtil.class.getClassLoader().
                    getResource("QueryJobs.json"), QueryJobs.class);
            return analyticsReader.getJobDetailsFor(category);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
