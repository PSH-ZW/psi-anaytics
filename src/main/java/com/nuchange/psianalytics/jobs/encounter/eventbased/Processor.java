package com.nuchange.psianalytics.jobs.encounter.eventbased;

import com.nuchange.psianalytics.constants.JobConstants;
import com.nuchange.psianalytics.jobs.encounter.EncounterProcessor;
import com.nuchange.psianalytics.model.EncounterJobDto;
import org.springframework.stereotype.Component;

@Component(JobConstants.ENCOUNTER_EVENT_BASE_JOB_ITEM_PROCESSOR_STEP_ONE)
public class Processor extends EncounterProcessor<EncounterJobDto, EncounterJobDto> {

    @Override
    public EncounterJobDto process(EncounterJobDto encounterJobDto) throws Exception {
        return encounterJobDto;
    }
}
