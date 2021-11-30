package com.nuchange.psianalytics.jobs;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class Reader extends JdbcCursorItemReader<Object[]> implements ItemReader<Object[]> {

    public Reader(@Autowired DataSource mysqlDatasource) {
        setDataSource(mysqlDatasource);
        setSql("SELECT id, name FROM test");
        setFetchSize(100);
        setRowMapper(new ObjectRowMapper());
    }

    public static class ObjectRowMapper implements RowMapper<Object[]> {

        @Override
        public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
            Object[] data = new Object[2];
            data[0] = rs.getInt("id");
            data[1] = rs.getString("name");

            return data;
        }
    }
}
