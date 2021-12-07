package com.nuchange.psianalytics.config;

import com.nuchange.psianalytics.jobs.patient.PatientProcessor;
import com.nuchange.psianalytics.jobs.patient.PatientReader;
import com.nuchange.psianalytics.jobs.patient.PatientWriter;
import com.nuchange.psianalytics.listener.JobCompletionNotificationListener;
import com.nuchange.psianalytics.model.ResultExtractor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    PatientReader myCustomReader;

    @Autowired
    PatientWriter myCustomWriter;

    @Autowired
    PatientProcessor myCustomProcessor;

    @Autowired
    JobCompletionNotificationListener completionListener;

    @Bean
    public Job createJob() {
        return jobBuilderFactory.get("MyJob")
                .incrementer(new RunIdIncrementer())
                .listener(completionListener)
                .flow(createStep()).end().build();
    }

    @Bean
    public Step createStep() {
        return stepBuilderFactory.get("MyStep")
                .<List<ResultExtractor>, List<ResultExtractor>> chunk(1)
                .reader(myCustomReader)
                .processor(myCustomProcessor)
                .writer(myCustomWriter)
                .build();
    }
}
