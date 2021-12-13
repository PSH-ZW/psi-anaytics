package com.nuchange.psianalytics.jobs.encounter.eventbased;

import com.nuchange.psianalytics.jobs.JobConstants;
import com.nuchange.psianalytics.jobs.encounter.EncounterWriter;
import com.nuchange.psianalytics.model.EncounterJobDto;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component(JobConstants.ENCOUNTER_EVENT_BASE_JOB_ITEM_WRITER_STEP_ONE)
public class Writer extends EncounterWriter<EncounterJobDto> {

    public Writer(@Qualifier("analyticsDatasource") DataSource ds) {
        super(ds);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        super.setStepExecution(stepExecution);
        setJobParameter(stepExecution.getJobParameters());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return stepExecution.getExitStatus();
    }

    @Override
    public void write(List<? extends EncounterJobDto> list) throws Exception {
        saveEncountertoDb((List<EncounterJobDto>) list);
    }
}
