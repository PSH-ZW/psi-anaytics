package com.nuchange.psianalytics.config;

import com.nuchange.psianalytics.jobs.JobConstants;
import com.nuchange.psianalytics.model.EncounterJobDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@EnableBatchProcessing
public class EncounterEventBasedBatch {

    @Autowired
    protected JobBuilderFactory jobBuilderFactory;

    @Autowired
    protected StepBuilderFactory stepBuilderFactory;

    @Resource(name = JobConstants.ENCOUNTER_EVENT_BASE_JOB_ITEM_READER_STEP_ONE)
    protected ItemReader<EncounterJobDto> itemReader;

    @Resource(name = JobConstants.ENCOUNTER_EVENT_BASE_JOB_ITEM_PROCESSOR_STEP_ONE)
    protected ItemProcessor<EncounterJobDto, EncounterJobDto> itemProcessor;

    @Resource(name = JobConstants.ENCOUNTER_EVENT_BASE_JOB_ITEM_WRITER_STEP_ONE)
    protected ItemWriter<EncounterJobDto> itemWriter;

    @Bean(JobConstants.ENCOUNTER_EVENT_BASE_JOB)
    public Job job(@Qualifier(JobConstants.ENCOUNTER_EVENT_BASE_JOB_STEP_ONE) Step step) throws Exception{
        return jobBuilderFactory.get(JobConstants.ENCOUNTER_EVENT_BASE_JOB)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean(JobConstants.ENCOUNTER_EVENT_BASE_JOB_STEP_ONE)
    public Step step() throws Exception {
        return stepBuilderFactory.get(JobConstants.ENCOUNTER_EVENT_BASE_JOB_STEP_ONE)
                .<EncounterJobDto, EncounterJobDto>chunk(1)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }
}