package com.nuchange.psianalytics.jobs.programEnrolment;

import com.nuchange.psianalytics.constants.JobConstants;
import com.nuchange.psianalytics.jobs.querybased.QueryEventBasedMrsWriter;
import com.nuchange.psianalytics.model.ResultExtractor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component(JobConstants.PROGRAM_ENROLMENT_MRS_JOB_ITEM_WRITER_ID)
public class ProgramEnrolmentWriter extends QueryEventBasedMrsWriter {

    public ProgramEnrolmentWriter(@Qualifier("analyticsDatasource") DataSource ds) {
        super(ds);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        super.setStepExecution(stepExecution);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return stepExecution.getExitStatus();
    }

    @Override
    public void write(List<? extends List<ResultExtractor>> list) throws Exception {
        writeEvent(list);
    }
}
