package com.nuchange.psianalytics.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DatasourceConfig {

    @Primary
    @Bean(name = "mrsDatasource")
    @ConfigurationProperties(prefix = "mrs.datasource")
    public DataSource mrsDatasource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "analyticsDatasource")
    @ConfigurationProperties(prefix = "analytics.datasource")
    public DataSource analyticsDatasource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mrsJdbcTemplate")
    public JdbcTemplate mrsJdbcTemplate(
            @Qualifier("mrsDatasource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "analyticsJdbcTemplate")
    public JdbcTemplate analyticsJdbcTemplate(
            @Qualifier("analyticsDatasource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
