package com.nuchange.psianalytics.jobs.patient;

import com.nuchange.psianalytics.jobs.FlatteningTask;
import com.nuchange.psianalytics.constants.JobConstants;
import com.nuchange.psianalytics.jobs.MrsCommonTask;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Component;

@Component(JobConstants.MRS_PATIENT_JOB)
@DisallowConcurrentExecution
public class PatientTask extends MrsCommonTask implements FlatteningTask {

    @Override
    public void process() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        runMrsJob(JobConstants.MRS_PATIENT);
    }
}
