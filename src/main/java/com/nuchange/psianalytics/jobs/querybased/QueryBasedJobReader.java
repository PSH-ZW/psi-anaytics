package com.nuchange.psianalytics.jobs.querybased;

import com.nuchange.psianalytics.model.QueryJob;
import com.nuchange.psianalytics.model.ResultExtractor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public abstract class QueryBasedJobReader<D> implements ItemReader<D>, StepExecutionListener {

    private boolean stop = false;

    private JobParameters jobParameters;

    @Autowired
    @Qualifier("mrsJdbcTemplate")
    private JdbcTemplate template;

    public QueryBasedJobReader(DataSource ds) {
        super();
        template = new JdbcTemplate(ds);
    }

    protected void setJobParameter(JobParameters jobParameters) {
        this.jobParameters = jobParameters;
    }

    protected JobParameters getJobParameters() {
        return jobParameters;
    }

    public JdbcTemplate getTemplate() {
        return template;
    }

    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    protected List<ResultExtractor> getResultExtractorForCategoryAndId(QueryJob queryJob,
                                                                       String category, Long id) throws IOException, SQLException {
        Extractor ex = new Extractor(template);
        return ex.getResultExtractors(queryJob, category, id);
    }
}
