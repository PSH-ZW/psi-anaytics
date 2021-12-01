package com.nuchange.psianalytics.model;

import java.util.Map;

public class QueryJobs {
    private Map<String, QueryJob> jobs;

    public Map<String, QueryJob> getJobs() {
        return jobs;
    }

    public void setJobs(Map<String, QueryJob> jobs) {
        this.jobs = jobs;
    }

    public QueryJob getJobDetailsFor(String cateogry){
        return jobs.get(cateogry);
    }
}
