package com.nuchange.psianalytics.jobs.programEnrolment;

import com.nuchange.psianalytics.constants.JobConstants;
import com.nuchange.psianalytics.jobs.querybased.QueryEventBasedMrsReader;
import com.nuchange.psianalytics.model.ResultExtractor;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component(JobConstants.PROGRAM_ENROLMENT_MRS_JOB_ITEM_READER_ID)
public class ProgramEnrolmentReader extends QueryEventBasedMrsReader {

    public ProgramEnrolmentReader(DataSource ds) {
        super(ds);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        super.setStepExecution(stepExecution);
    }

    @Override
    public List<ResultExtractor> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return readEvent();
    }
}
