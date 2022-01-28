package com.nuchange.psianalytics.jobs.encounter.eventbased;

import com.nuchange.psianalytics.constants.JobConstants;
import com.nuchange.psianalytics.jobs.encounter.EncounterReader;
import com.nuchange.psianalytics.model.Encounter;
import com.nuchange.psianalytics.model.EncounterJobDto;
import com.nuchange.psianalytics.model.EventRecords;
import com.nuchange.psianalytics.model.ProcessedEvents;
import com.nuchange.psianalytics.util.MetaDataService;
import com.nuchange.psiutil.AnalyticsUtil;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Component(JobConstants.ENCOUNTER_EVENT_BASE_JOB_ITEM_READER_STEP_ONE)
public class Reader extends EncounterReader<EncounterJobDto> {

    @Autowired
    protected MetaDataService metaDataService;

    private StepExecution stepExecution;

    public Reader(@Qualifier("mrsDatasource") DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
        setJobParameter(stepExecution.getJobParameters());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return stepExecution.getExitStatus();
    }

    @Override
    @Transactional
    public EncounterJobDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        String category = "encounter";
        String eventCategory = JobConstants.CATEGORY_TO_EVENT.get(category);
        ProcessedEvents processedEvents = finaLastProcessedEventsForCategory(category);

        int id = 0;
        if (processedEvents != null && processedEvents.getLastProcessedId() != null) {
            id = processedEvents.getLastProcessedId();
        }

        EventRecords eventRecords = metaDataService.getRecordGreaterThanIdAndCategory(id, eventCategory);

        if (eventRecords == null) {
            return null;
        }

        String params = eventRecords.getObject();
        String encounterUUID = AnalyticsUtil.getUuidFromParam(params, eventCategory);
        Encounter encounter = metaDataService.getEncounterByUuid(encounterUUID);
        ExecutionContext executionContext = stepExecution.getExecutionContext();
        executionContext.put("eventId", eventRecords.getId());
        executionContext.put("eventUuid", eventRecords.getUuid());
        return readEncounter(encounter.getEncounterId());
    }

    private ProcessedEvents finaLastProcessedEventsForCategory(String category) {
        ProcessedEvents processedEvents = metaDataService.findProcessedEventByCategory(category);
        return processedEvents;
    }
}
