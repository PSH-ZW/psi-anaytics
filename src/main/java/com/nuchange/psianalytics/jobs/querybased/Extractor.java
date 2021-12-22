package com.nuchange.psianalytics.jobs.querybased;

import com.nuchange.psianalytics.model.QueryJob;
import com.nuchange.psianalytics.model.ResultExtractor;
import com.nuchange.psianalytics.util.AnalyticsUtil;
import com.nuchange.psianalytics.util.QueryBaseJobUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Extractor {

    //TODO: need to get from application.props
    private String orgUnitId = "TwUzWzgDAST";

    private final JdbcTemplate template;

    public Extractor(JdbcTemplate template) {
        this.template = template;
    }

    public List<ResultExtractor> getResultExtractors(QueryJob queryJob, String category, Long id) throws IOException {
        String inputFile = queryJob.getInput();

        /* Extract Data From File */
        URL resource = this.getClass().getClassLoader().getResource(inputFile);
        String query = new String(Files.readAllBytes(Paths.get(resource.getPath())));

        /* Run Query Against JDBC */
        ResultExtractor resultExtractor = new ResultExtractor();
        List<String> colHeaders = new ArrayList<>();
        List<Map<String, Object>> rowValues = new ArrayList<>();

        /* If Id is null than return null */
        if (id == null) {
            return null;
        }
        Object[] params = new Object[] { id };
        List<ResultExtractor> extractors = new ArrayList<>();
        AnalyticsUtil.getRowAndColumnValuesForQuery(template, query, colHeaders, rowValues, params);
        if(!CollectionUtils.isEmpty(rowValues)) {
            colHeaders.add("org_unit");
            rowValues.get(0).put("org_unit", orgUnitId);
        }
        for (Map<String, Object> stringObjectMap : rowValues) {
            readChildren(category, stringObjectMap, extractors);
        }

        resultExtractor.setColHeaders(colHeaders);
        resultExtractor.setRowValues(rowValues);
        resultExtractor.setProcessingId(Math.toIntExact(id));
        resultExtractor.setCategory(category);
        resultExtractor.setTarget(queryJob.getTarget());
        extractors.add(resultExtractor);
        return extractors;
    }

    public void readChildren(String parentCategory, Map<String,Object> stringObjectMap, List<ResultExtractor> childExtractors) throws IOException {
        QueryJob jobDetails = QueryBaseJobUtil.getJobDetails(parentCategory);
        if(jobDetails.getChildren() != null) {
            String[] childs = jobDetails.getChildren();
            for (String child : childs) {
                QueryJob childJob = QueryBaseJobUtil.getJobDetails(child);
                if (childJob != null) {
                    String inputFile = childJob.getFetchAll();
                    URL resource = this.getClass().getClassLoader().getResource(inputFile);
                    String query = new String(Files.readAllBytes(Paths.get(resource.getPath())));
                    List<String> colHeaders = new ArrayList<>();
                    List<Map<String, Object>> colValues = new ArrayList<>();
                    Object id = stringObjectMap.get(childJob.getParentKey());
                    AnalyticsUtil.getRowAndColumnValuesForQuery(template, query, colHeaders, colValues, new Object[] {id});
                    for (Map<String, Object> colValue : colValues) {
                        List<Map<String, Object>> newList = new ArrayList<>();
                        readChildren(child, colValue, childExtractors);
                        newList.add(colValue);
                        ResultExtractor resultExtractor = new ResultExtractor();
                        resultExtractor.setCategory(child);
                        resultExtractor.setTarget(childJob.getTarget());
                        resultExtractor.setRowValues(newList);
                        resultExtractor.setColHeaders(colHeaders);
                        resultExtractor.setProcessingId((Integer) colValue.get(childJob.getPrimaryKey()));
                        childExtractors.add(resultExtractor);
                    }
                }
            }
        }
    }

    public void insertData(String target, QueryJob jobDetails, List<String> colHeaders, Integer id, Map<String, Object> stringObjectMap) throws SQLException {
        String existQuery = jobDetails.getExistQuery();
        Object[] paramsExist;
        String[] existQueryParams = jobDetails.getExistQueryParams();
        if (existQueryParams != null && existQueryParams.length != 0) {
            paramsExist = new Object[existQueryParams.length];
            for(int i = 0; i < existQueryParams.length; i++) {
                paramsExist[i] = stringObjectMap.get(existQueryParams[i]);
            }
        } else {
            paramsExist = new Object[] {id};
        }
        boolean exist = false;
//                Integer count = template.queryForObject(existQuery, Integer.class);
        if (existQuery != null && existQuery.length() != 0) {
            List<Integer> count = template.query(existQuery, (RowMapper<Integer>) new RowMapper<Integer>() {
                @Override
                public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getInt(1);
                }
            }, paramsExist);
            if (count.size() != 0 && count.get(0) != 0) {
                exist = true;
            }
        }

        String query = "";
        Object[] params;
        if (exist) {
            /* Run Update Query */
            if (existQueryParams != null && existQueryParams.length != 0) {
                query = AnalyticsUtil.getUpdateQueryWithExistingParams(colHeaders, target, existQueryParams);
                params = new Object[colHeaders.size() + existQueryParams.length];
            }
            else {
                query = AnalyticsUtil.getUpdateQuery(colHeaders, target, jobDetails.getPrimaryKey());
                params = new Object[colHeaders.size() + 1]; // one extra for id
            }
        } else {
            query = AnalyticsUtil.getInsertQuery(colHeaders, target);
            params = new Object[colHeaders.size()];
        }
        int i = 0;
        for (String colHeader : colHeaders) {
            params[i++] = stringObjectMap.get(colHeader);
        }
        if (exist) {
            if (existQueryParams != null && existQueryParams.length != 0) {
                for(String existParam: existQueryParams) {
                    params[i] = stringObjectMap.get(existParam);
                    i++;
                }
            } else {
                params[i] = id;
            }
        }
        template.update(query, params);
        //TODO: uncomment if not needed.
//        List<PostJobWriterListener> listners = AppContext.getInstance().
//                getRegisteredComponents(PostJobWriterListener.class);
//        if (!CollectionUtils.isEmpty(listners)) {
//            for (PostJobWriterListener listner : listners) {
//                if(listner.canProcess(target)){
//                    listner.execute(template, stringObjectMap);
//                }
//            }
//        }
    }

}

