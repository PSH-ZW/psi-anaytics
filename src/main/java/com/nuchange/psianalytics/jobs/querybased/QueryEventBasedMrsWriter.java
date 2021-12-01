package com.nuchange.psianalytics.jobs.querybased;

import com.nuchange.psianalytics.model.ResultExtractor;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;

import javax.sql.DataSource;
import java.util.List;

//@Component(JobConstants.QUERY_EVENT_BASED_MRS_JOB_ITEM_WRITER_STEP_ONE)
public abstract class QueryEventBasedMrsWriter extends QueryBasedJobWriter<List<ResultExtractor>> {

    private StepExecution stepExecution;

    public QueryEventBasedMrsWriter(DataSource ds) {
        super(ds);
    }

    public void setStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
        setJobParameter(stepExecution.getJobParameters());
    }

    public void writeEvent(List<? extends List<ResultExtractor>> list) throws Exception {
        ExecutionContext executionContext = stepExecution.getExecutionContext();
        for (List<ResultExtractor> extractors : list) {
            saveResultExtractorList(extractors, executionContext);
        }
    }
}
