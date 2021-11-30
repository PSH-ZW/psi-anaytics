package com.nuchange.psianalytics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TestService {

    @Autowired
    @Qualifier("mrsJdbcTemplate")
    private JdbcTemplate mrsTemplate;

    @Autowired
    @Qualifier("analyticsJdbcTemplate")
    private JdbcTemplate analyticsTemplate;

    public void testMrsQuery() {
        final String query = "select count(*) from visit_type";
        Map<String, Object> result = mrsTemplate.queryForMap(query, new HashMap<>());
        System.out.println(result);
    }

    public void testAnalyticsQuery() {
        final String query = "select * from test";
        Map<String, Object> result = analyticsTemplate.queryForMap(query, new HashMap<>());
        System.out.println(result);
    }
}
