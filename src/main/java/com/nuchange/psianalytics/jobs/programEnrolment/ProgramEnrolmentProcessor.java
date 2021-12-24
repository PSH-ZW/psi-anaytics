package com.nuchange.psianalytics.jobs.programEnrolment;

import com.nuchange.psianalytics.constants.JobConstants;
import com.nuchange.psianalytics.jobs.querybased.QueryEventBasedMrsProcessor;
import com.nuchange.psianalytics.model.ResultExtractor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(JobConstants.PROGRAM_ENROLMENT_MRS_JOB_ITEM_PROCESSOR_ID)
public class ProgramEnrolmentProcessor extends QueryEventBasedMrsProcessor {

    @Override
    public List<ResultExtractor> process(List<ResultExtractor> resultExtractors) throws Exception {
        return resultExtractors;
    }
}
