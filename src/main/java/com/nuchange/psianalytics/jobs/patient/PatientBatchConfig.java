package com.nuchange.psianalytics.jobs.patient;

import com.nuchange.psianalytics.constants.JobConstants;
import com.nuchange.psianalytics.model.ResultExtractor;
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
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class PatientBatchConfig {
    @Autowired
    protected JobBuilderFactory jobBuilderFactory;

    @Autowired
    protected StepBuilderFactory stepBuilderFactory;

    @Resource(name= JobConstants.PATIENT_MRS_JOB_ITEM_READER_ID)
    protected ItemReader<List<ResultExtractor>> itemReader;

    @Resource(name=JobConstants.PATIENT_MRS_JOB_ITEM_PROCESSOR_ID)
    protected ItemProcessor<List<ResultExtractor>, List<ResultExtractor>> itemProcessor;

    @Resource(name=JobConstants.PATIENT_MRS_JOB_ITEM_WRITER_ID)
    protected ItemWriter<List<ResultExtractor>> itemWriter;

    @Primary
    @Bean(JobConstants.PATIENT_MRS_JOB)
    public Job job(@Qualifier(JobConstants.PATIENT_MRS_JOB_STEP_ID) Step step) throws Exception{
        return jobBuilderFactory.get(JobConstants.PATIENT_MRS_JOB)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean(JobConstants.PATIENT_MRS_JOB_STEP_ID)
    public Step step() throws Exception {
         return stepBuilderFactory.get(JobConstants.PATIENT_MRS_JOB_STEP_ID)
                .<List<ResultExtractor>, List<ResultExtractor>>chunk(1)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }
}

