package com.nuchange.psianalytics.jobs;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Writer implements ItemWriter<Object[]> {

    @Autowired
    @Qualifier("analyticsJdbcTemplate")
    private JdbcTemplate analyticsTemplate;

    @Override
    public void write(List<? extends Object[]> list) throws Exception {
        for(Object[] data : list) {
            System.out.println("MyCustomWriter    : Writing data    : " + data[0] + " " + data[1]);
            final String sql = "insert into test values(?, ?)";
            analyticsTemplate.update(sql, data[0], data[1]);
        }
    }
}