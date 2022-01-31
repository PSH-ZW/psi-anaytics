package com.nuchange.psianalytics.jobs.encounter;

import com.nuchange.psianalytics.constants.JobConstants;
import com.nuchange.psianalytics.jobs.querybased.QueryBasedJobWriter;
import com.nuchange.psianalytics.model.EncounterJobDto;
import com.nuchange.psianalytics.model.Query;
import com.nuchange.psianalytics.model.ResultExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class EncounterWriter<D> extends QueryBasedJobWriter<D> {
    private static final Logger logger = LoggerFactory.getLogger(EncounterWriter.class);

    private StepExecution stepExecution;

    private List<String> deleteExecuted = new ArrayList<>();

    public EncounterWriter(DataSource ds) {
        super(ds);
    }

    protected void saveEncounterToDb(List<EncounterJobDto> list) throws Exception {
        for (EncounterJobDto encounterJob : list) {
            List<Query> queryChild = new ArrayList<>();

            List<Query> insertQueries = encounterJob.getInsertQueries();
            List<String> batchDelete = new ArrayList<>();
            Set<String> batchUpdate = new HashSet<>();
            for (Query query : insertQueries) {
                String encounterId = query.getColAndVal().get("encounter_id");
                if (!deleteExecuted.contains(query.getTable())) {
                    batchDelete.add(deleteIfEncounterPresent(query));
                    deleteExecuted.add(query.getTable());
                }
                if (query.getParentTable() != null) {
                    if (!deleteExecuted.contains(query.getParentTable())) {
                        /* delete if encounter and visit_id and instance present */
                        String deleteQuery = "DELETE FROM " + query.getParentTable() + " WHERE encounter_id = '" +
                                encounterId + "'";
                        batchDelete.add(deleteQuery);
                        deleteExecuted.add(query.getParentTable());
                    }
                    if (!query.isIgnore()) {
                        queryChild.add(query);
                    }
                } else if (!query.isIgnore()) {
                    String insertQuery = createQuery(query);
                    batchUpdate.add(insertQuery);
                    logger.info(String.format("Processed Encounter %s for form : %s", encounterId, query.getTable()));
                    addEntryToEventTrackerIfNotExists(batchUpdate, encounterId);
                }
            }

            if (!CollectionUtils.isEmpty(batchDelete)) {
                batchDelete.forEach(d -> template.update(d));
            }

            if (!CollectionUtils.isEmpty(batchUpdate)) {
                batchUpdate.forEach(d -> template.update(d));
            }

            for (Query query : queryChild) {
                if (!deleteExecuted.contains(query.getParentTable())) {
                    /* delete if encounter and visit_id and instance present */
                    String deleteQuery = "DELETE FROM " + query.getParentTable() + " WHERE encounter_id = '" +
                            query.getColAndVal().get("encounter_id") + "'";
                    template.update(deleteQuery);
                    deleteExecuted.add(query.getParentTable());
                }
                List<Integer> count = fetchParentForQuery(query);
                int id = 0;
                if (count.isEmpty()) {
                    /* Insert a data into parent table with encounter_id, visit_id and patient_id and instance_id */
                    String insertParent = insertDummyIntoParent(query);
                    int update = template.update(insertParent);
                    count = fetchParentForQuery(query);
                    id = count.get(0);
                }
                id = count.get(0);
                query.getColAndVal().put("parent_id", String.valueOf(id));
                String insertQuery = createQuery(query);
                int update = template.update(insertQuery);
            }

            /* Write ResultExtractor into DB */
            for (String key : encounterJob.getExtractorsWithTarget().keySet()) {
                if (!key.equals(JobConstants.ENCOUNTER)) {
                    List<ResultExtractor> extractors = encounterJob.getExtractorsWithTarget().get(key);
                    saveResultExtractorList(extractors, stepExecution.getExecutionContext());
                }
            }

            /* Last Save The Encounter */
            if (encounterJob.getExtractorsWithTarget().containsKey(JobConstants.ENCOUNTER)) {
                List<ResultExtractor> extractors = encounterJob.getExtractorsWithTarget().get(JobConstants.ENCOUNTER);
                saveResultExtractorList(extractors, stepExecution.getExecutionContext());
            }
            deleteExecuted = new ArrayList<>();
        }
    }

    private void addEntryToEventTrackerIfNotExists(Set<String> batchUpdate, String encounterId) {
        //TODO: add more details here if needed.
        String sql = "INSERT INTO event_tracker(encounter_id) values("+ encounterId +")";
        boolean shouldInsertToEventTracker = !batchUpdate.contains(sql)
                && !metaDataService.entryExistsInEventTracker(encounterId);
        if(shouldInsertToEventTracker) {
            batchUpdate.add(sql);
        }
    }

    private List<Integer> fetchParentForQuery(Query query) {
        String isExistQuery = getQueryToFetchParent(query);
        List<Integer> count = template.query(isExistQuery, (RowMapper<Integer>) new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getInt(1);
            }
        });
        return count;
    }

    private String createQuery(Query query) {
        StringBuilder columns = new StringBuilder("");
        columns.append("INSERT INTO ").append(query.getTable()).append(" (");
        StringBuilder values = new StringBuilder("VALUES (");
        Map<String, String> colAndVal = query.getColAndVal();
        List<String> listColumns = new ArrayList<>(colAndVal.keySet());
        Collections.sort(listColumns);
        for (String key : listColumns) {
            columns.append(key).append(",");
            String refactorValue = colAndVal.get(key).replaceAll("'", "''");
            values.append("'").append(refactorValue).append("',");
        }
        columns.deleteCharAt(columns.length()-1);
        values.deleteCharAt(values.length() - 1);
        columns.append(") ");
        values.append(" )");
        return columns.toString() + values;
    }

    private String getQueryToFetchParent(Query query) {
        Map<String, String> colAndVal = query.getColAndVal();
        String isExistQuery = "SELECT id FROM " + query.getParentTable() + " ";
        isExistQuery += "WHERE encounter_id = '" + colAndVal.get("encounter_id") + "' and ";
        isExistQuery += "visit_id = '" + colAndVal.get("visit_id") + "' and ";
        isExistQuery += "instance_id = '" + colAndVal.get("instance_id") + "'";
        return isExistQuery;
    }

    private String insertDummyIntoParent(Query query) {
        Map<String, String> colAndVal = query.getColAndVal();

        String insertDummy = "INSERT INTO " + query.getParentTable() + " (";
        insertDummy += "encounter_id, visit_id, instance_id) VALUES (";
        insertDummy += "'" + colAndVal.get("encounter_id") + "', ";
        insertDummy += "'" + colAndVal.get("visit_id") + "', ";
        insertDummy += "'" + colAndVal.get("instance_id") + "')";
        return insertDummy;
    }

    private String deleteIfEncounterPresent(Query query) {
        String deleteQuery = "DELETE FROM " + query.getTable() +
                " WHERE encounter_id = '" + query.getColAndVal().get("encounter_id") + "'";
        return deleteQuery;
    }

    protected void setJobParameters(JobParameters jobParameters) {
        super.setJobParameter(jobParameters);
    }

    protected JobParameters getJobParameter() {
        return super.getJobParameters();
    }

    protected void setStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
}
