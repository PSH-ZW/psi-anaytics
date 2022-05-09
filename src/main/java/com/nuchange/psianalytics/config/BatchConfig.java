package com.nuchange.psianalytics.config;

import com.nuchange.psianalytics.jobs.patient.PatientProcessor;
import com.nuchange.psianalytics.jobs.patient.PatientReader;
import com.nuchange.psianalytics.jobs.patient.PatientWriter;
import com.nuchange.psianalytics.listener.JobCompletionNotificationListener;
import com.nuchange.psianalytics.model.ResultExtractor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
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

    @Autowired
    JobRegistry jobRegistry;

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);
        return postProcessor;
    }

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

    @Bean
    public BatchConfigurer batchConfigurer(@Qualifier("analyticsDatasource") DataSource dataSource) {
        return new DefaultBatchConfigurer(dataSource) {
            @Override
            protected JobRepository createJobRepository() throws Exception {
                JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
                factory.setDataSource(dataSource);
                factory.setTransactionManager(getTransactionManager());
                factory.setIsolationLevelForCreate("ISOLATION_REPEATABLE_READ");
                return factory.getObject();
            }
        };
    }
}
