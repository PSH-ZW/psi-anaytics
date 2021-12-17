package com.nuchange.psianalytics.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuchange.psianalytics.constants.JobConstants;
import com.nuchange.psianalytics.model.*;
import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class AnalyticsUtil {
    private static Logger logger = LoggerFactory.getLogger(AnalyticsUtil.class);
    private static Map<String, Forms> formCache = new HashMap<>();

    public static String getInsertQuery(List<String> colHeaders, String target) {
        StringBuilder query = new StringBuilder("INSERT INTO " + target + " (");
        for (String colHeader : colHeaders) {
            query.append(colHeader).append(",");
        }
        query.deleteCharAt(query.length() - 1);
        query.append(") values (");
        for (String colHeader : colHeaders) {
            query.append("?,");
        }
        query.deleteCharAt(query.length() - 1);
        query.append(")");
        return query.toString();
    }

    public static String getExistQuery(Map<String, Object> rowValue, String target, String[] params) {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM ");
        query.append(target).append(" WHERE ");
        for(int i = 0; i < params.length; i++) {
            if (rowValue.get(params[i]) != null) {
                query.append(params[i]).append(" = '").append(rowValue.get(params[i])).append("'");
            } else {
                query.append(params[i]).append(" is null");
            }
            if (i != params.length - 1) {
                query.append(" and ");
            }

        }
        return query.toString();
    }

    public static String getUpdateQuery(List<String> colHeaders, String target, String primaryKey) {
        StringBuilder query = new StringBuilder("UPDATE " + target + " SET ");
        for(String colHeader: colHeaders) {
            query.append(colHeader).append(" = ?,");
        }
        query.deleteCharAt(query.length() - 1);
        query.append(" WHERE ").append(primaryKey).append(" = ? ");
        return query.toString();
    }

    public static String getUpdateQueryWithExistingParams(List<String> colHeaders, String target, String[] existParams) {
        StringBuilder query = new StringBuilder("UPDATE " + target + " SET ");
        for(String colHeader: colHeaders) {
            query.append(colHeader).append(" = ?,");
        }
        query.deleteCharAt(query.length() - 1);
        query.append(" WHERE ");
        for (int i = 0; i < existParams.length; i++) {
            query.append(existParams[i]).append(" = ? ");
            if (i != (existParams.length - 1)) {
                query.append(" and ");
            }
        }
        return query.toString();
    }

    public static String getLastRecordForCategoryInEventRecords(String category) {
        return "SELECT * FROM event_records " + "WHERE id = (SELECT MAX(id) FROM event_records WHERE category = '" + category + "')";
    }

    public static String generateColumnName(String name) {
        if (name.contains("(")) {
           name = name.substring(0, name.indexOf("(")).trim();
        }
        name = name.replaceAll(" ", "_").replaceAll(",", "");
        name = name.replaceAll("-", "_").replaceAll("/", "_");
        name = name.replaceAll("&", "").replaceAll("\\?", "");
        name = name.replaceAll("__", "_").replaceAll("\\.", "_");
        return name.toLowerCase();
    }

    public static String getShortName(String name) {
        if (name.contains(",")) {
            StringBuilder shortName = new StringBuilder();
            String lastName = name.substring(name.lastIndexOf(",")+1).trim();
            name = name.substring(0, name.lastIndexOf(","));
            String[] commaSeprated = name.split(",");
            for (String s : commaSeprated) {
                s = s.trim();
                String[] spaceSeprated = s.split(" ");
                StringBuilder firstLetters = new StringBuilder();
                for (String word : spaceSeprated) {
                    word = word.trim();
                    firstLetters.append(word.charAt(0));
                }
                shortName.append(firstLetters).append("_");
            }
            return shortName + lastName;
        }
        return name;
    }
    public static void getRowAndColumnValuesForQuery(JdbcTemplate template, String query, List<String> colHeaders,
                                                     List<Map<String, Object>> rowValues, Object[] params) {
        template.query(query,new RowMapper<ResultSet>() {
            public ResultSet mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String, Object> rowValue = new HashMap<>();
                if (CollectionUtils.isEmpty(colHeaders)){
                    extractColNames(rs, colHeaders);
                }
                int i = 0;
                for (String colHeader : colHeaders) {
                    rowValue.put(colHeader, rs.getObject(colHeader));
                }
                rowValues.add(rowValue);
                return rs;
            }

            private List<String> extractColNames(ResultSet rs, List<String> colHeaders) throws SQLException {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                while(columnCount>0){
                    String catalogName = metaData.getColumnLabel(columnCount--);
                    colHeaders.add(catalogName.toLowerCase());
                }
                return colHeaders;
            }
        }, params);
    }

    public static List<String> generateCreateTableForForm(String formName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Forms forms;
        forms = parseForm(mapper.readTree(AnalyticsUtil.class.getClassLoader().getResource(formName)));
        List<String> queries = new ArrayList<>();
        Map<String, FormTable> obsWithConcepts = new HashMap<>();
        handleObsControls(forms.getControls(), obsWithConcepts, forms.getName(), null, null);
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE ").append(AnalyticsUtil.generateColumnName(forms.getName())).append("(");
        query.append("id serial PRIMARY KEY, ");
        if (obsWithConcepts.containsKey(forms.getName())) {
            for (FormConcept concept : obsWithConcepts.get(forms.getName()).getConcepts()) {
                String name = AnalyticsUtil.generateColumnName(concept.getName());
                query.append(name).append(" varchar, ");
            }
        }
        query.append("encounter_id integer, visit_id integer, patient_id integer, ");
        query.append("username varchar, date_created timestamp, patient_identifier varchar, ");
        query.append("location_id integer, location_name varchar, instance_id integer)");
        queries.add(query.toString());
        if (obsWithConcepts.containsKey(forms.getName())){
            obsWithConcepts.remove(forms.getName());
        }
        for (String s : obsWithConcepts.keySet()) {
            queries.add(createQueryForFormTable(obsWithConcepts.get(s), forms.getName()));
        }
        return queries;
    }

    public static void handleObsControls(List<FormControl> controls, Map<String, FormTable> obsWithConcepts,
                                         String parent, String formName, String sectionLabel) {
        for (FormControl control : controls) {
            extractConceptsForObsControl(obsWithConcepts, control, parent, formName, sectionLabel);
        }
    }
    public static void extractConceptsForObsControl(Map<String, FormTable> obsWithConcepts,
                                                    FormControl control, String parent, String tableName, String sectionLabel) {
        //TODO: remove parent argument
        if (control.getType().equals(JobConstants.OBS_FLOWSHEET) || control.getType().equals(JobConstants.LABEL)) {
            return;
        }
        if (control.getType().equals(JobConstants.OBS_CONTROL_GROUP)) {
            handleObsControls(control.getControls(), obsWithConcepts, parent, tableName, null);
        } else if (control.getType().equals(JobConstants.OBS_SECTION_CONTROL)) {
            handleObsControls(control.getControls(), obsWithConcepts, parent, tableName, control.getLabel().getValue());
        } else {
            if (tableName == null) {
                tableName = parent;
                parent = null;
            }
            if (!obsWithConcepts.containsKey(tableName)) {
                obsWithConcepts.put(tableName, new FormTable(tableName));
            }
            FormTable formTable = obsWithConcepts.get(tableName);
            FormConcept formConcept = control.getConcept();

            if (control.getProperties().getMultiSelect() != null && control.getProperties().getMultiSelect()) {
                String fieldName = getShortName(formConcept.getName());
                for(ConceptAnswer answerConcept : formConcept.getAnswers()) {
                    FormConcept answerConceptName = answerConcept.getName();
                    String multiSelectOptionName = answerConceptName.getName();
                    multiSelectOptionName = fieldName + "_" + getShortName(multiSelectOptionName);
                    answerConceptName.setName(multiSelectOptionName);
                    formTable.getConcepts().add(answerConceptName);
                }
            }
            else {
                String conceptName = PSIContext.getInstance().getMetaDataService()
                        .getFullNameOfConceptByUuid(UUID.fromString(formConcept.getUuid()));
                formConcept.setName(getShortName(conceptName));
                if (sectionLabel != null) {
                    formConcept.setName(sectionLabel + "_" + formConcept.getName());
                }
                formTable.getConcepts().add(formConcept);
            }
            formTable.setProperties(control.getProperties());
            formTable.setParent(parent);
        }
    }

    private static String createQueryForFormTable(FormTable formTable, String formName) {
        StringBuilder query = new StringBuilder("");
        String tableName = "";
        if (!formTable.getName().contains("multiselect")) {
            tableName = AnalyticsUtil.generateColumnName(formName + "_" + formTable.getName());
        } else {
            tableName = AnalyticsUtil.generateColumnName(formTable.getName());
        }
        query.append("CREATE TABLE ").append(AnalyticsUtil.generateColumnName(tableName)).append(" (");
        query.append("id serial PRIMARY KEY, ");
        for (FormConcept concept : formTable.getConcepts()) {
            String name = AnalyticsUtil.generateColumnName(concept.getName());
            query.append(name).append(" varchar, ");
        }
        query.append("parent_id integer, encounter_id integer, visit_id integer, patient_id integer, instance_id integer, provider_id integer, ");
        query.append("username varchar, date_created timestamp, patient_identifier varchar,");
        query.append("location_id integer, location_name varchar)");
        return query.toString();
    }

    private static Forms parseForm(JsonNode array){
        JsonNode resources = array.get("formJson").get("resources");
        String version = array.get("formJson").get("version").asText();
        String versionString = "";
        if(!StringUtils.isEmpty(version)){
            versionString = "." + version + "/";
        }
        String value = resources.get(0).get("value").toString();
        ObjectMapper mapper = new ObjectMapper();
        Forms c = null;
        try {
            c = mapper.readValue(value.replace("\\", "").replaceAll("^\"|\"$", ""), Forms.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*for(FormControl control : c.getControls()){
            parseObj(control, c.getName());
        }*/
        return c;
    }

    public static String getUuidFromParam(String params, String eventCategory) {
        if (params == null) {
            return null;
        }
        String[] tokens = params.split("/");
        int position = JobConstants.UUID_POSITION.get(eventCategory);
        String uuidString = tokens[position].substring(0, 36);
        //Doing this to verify the string is a valid UUID, if not, this will throw an exception
        //TODO: can be removed if not needed.
        return UUID.fromString(uuidString).toString();
    }

    public static Forms readForm(String fileName) throws IOException {
        if (formCache.containsKey(fileName)) {
            return formCache.get(fileName);
        }
        String resource = replaceSpecialCharWithUnderScore(fileName);
        logger.debug("Finding file  : " + resource);
        ObjectMapper mapper = new ObjectMapper();

        Forms forms = parseForm(mapper.readTree(AnalyticsUtil.class.getClassLoader().getResource(resource)));
        formCache.put(fileName, forms);
        return forms;
    }

    public static String replaceSpecialCharWithUnderScore(String fileName) {
        return fileName.replaceAll("&", "_");
    }

    public static Map<String, ObsType> extractConceptsFromFile(String fileName) throws IOException {
        Forms forms = readForm(fileName);
        Map<String, ObsType> conceptTypes = new HashMap<>();
        getConceptTypeFromControls(forms.getControls(), conceptTypes, null, null);
        return conceptTypes;
    }

    private static void getConceptTypeFromControls(List<FormControl> formControlList, Map<String, ObsType> concepts, String parentType,
                                                   FormLabel formLabel) {
        for (FormControl formControl : formControlList) {
            if (formControl.getType().equals(JobConstants.OBS_CONTROL)) {
                String uuid = formControl.getConcept().getUuid();
                ObsType obsType = new ObsType();
                obsType.setUuid(uuid);
                if (parentType != null) {
                    if (parentType.equals(JobConstants.OBS_SECTION_CONTROL)) {
                        obsType.setParentType(JobConstants.OBS_SECTION_CONTROL);
                        obsType.setLabel(formLabel);
                    }
                    else if (parentType.equals(JobConstants.OBS_CONTROL_GROUP)) {
                        obsType.setParentType(JobConstants.OBS_CONTROL_GROUP);
                        obsType.setLabel(formLabel);
                    }
                }
                if (formControl.getProperties().getMultiSelect() != null && formControl.getProperties().getMultiSelect()) {
                    obsType.setControlType(JobConstants.MULTI_SELECT);
                } else {
                    obsType.setControlType(JobConstants.TABLE);
                }
                concepts.put(uuid, obsType);

            } else if (formControl.getType().equals(JobConstants.OBS_FLOWSHEET) || formControl.getType().equals(JobConstants.LABEL)) {
                continue;
            } else {
                getConceptTypeFromControls(formControl.getControls(), concepts, formControl.getType(), formControl.getLabel());
            }
        }

    }
}

