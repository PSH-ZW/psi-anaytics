package com.nuchange.psianalytics.jobs;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import java.io.IOException;

public interface FlatJob {
    //TODO: rename this to flatteningJob once first stage of refactoring is complete.
    void process() throws InterruptedException, IOException, JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException;
}
