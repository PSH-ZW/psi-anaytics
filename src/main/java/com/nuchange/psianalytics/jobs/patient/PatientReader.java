package com.nuchange.psianalytics.jobs.patient;

import com.nuchange.psianalytics.constants.JobConstants;
import com.nuchange.psianalytics.jobs.querybased.QueryEventBasedMrsReader;
import com.nuchange.psianalytics.model.ResultExtractor;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;


@Component(JobConstants.PATIENT_MRS_JOB_ITEM_READER_ID)
public class PatientReader extends QueryEventBasedMrsReader {

    public PatientReader(@Qualifier("mrsDatasource") DataSource ds) {
        super(ds);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        super.setStepExecution(stepExecution);
    }

    @Override
    public List<ResultExtractor> read() throws Exception {
        return readEvent();
    }
}
