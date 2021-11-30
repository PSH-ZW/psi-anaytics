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
    @Bean(name = "mysqlDatasource")
    @ConfigurationProperties(prefix = "mysql.datasource")
    public DataSource mysqlDatasource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "postgresDatasource")
    @ConfigurationProperties(prefix = "postgresql.datasource")
    public DataSource postgresDatasource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mrsJdbcTemplate")
    public JdbcTemplate mrsJdbcTemplate(
            @Qualifier("mysqlDatasource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "analyticsJdbcTemplate")
    public JdbcTemplate analyticsJdbcTemplate(
            @Qualifier("postgresDatasource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
