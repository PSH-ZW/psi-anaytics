package com.nuchange.psianalytics.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuchange.psianalytics.constants.JobConstants;
import com.nuchange.psianalytics.model.*;
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
        String columnName = replaceSpecialCharactersInColumnName(name);
        //column and table names for postgres are truncated at 64 chars.
        return columnName.substring(0,Math.min(columnName.length(), 63));
    }

    public static String replaceSpecialCharactersInColumnName(String name) {
        name = name.replaceAll(" ", "_").replaceAll(",", "");
        name = name.replaceAll("-", "_").replaceAll("/", "_");
        name = name.replaceAll("&", "").replaceAll("\\?", "");
        name = name.replaceAll("__", "_").replaceAll("\\.", "_");
        name = name.replaceAll("'", "").replaceAll(":", "");
        name = name.replaceAll("’", "");
        return name.toLowerCase();
    }

    public static String getShortName(String name) {
        if (name.contains(",")) {
            StringBuilder shortName = new StringBuilder();
            String lastName = name.substring(name.lastIndexOf(",")+1).trim();
            name = name.substring(0, name.lastIndexOf(","));
            String[] commaSeparated = name.split(",");
            for (String s : commaSeparated) {
                s = s.trim();
                String[] spaceSeparated = s.split(" ");
                StringBuilder firstLetters = new StringBuilder();
                for (String word : spaceSeparated) {
                    word = word.trim();
                    firstLetters.append(word.charAt(0));
                }
                shortName.append(firstLetters).append("_");
            }
            name = shortName + lastName;
        }
        if(name.contains("(")) {
            name = name.replaceAll("\\(", "_").replaceAll("\\)", "_");
        }
        return name;
    }

    private static String getInitialsForName(String name) {
        StringBuilder shortName = new StringBuilder();
        name = name.replaceAll("\\(", " ").replaceAll("_", " ")
                .replaceAll("\\)", " ");
        String[] words = name.split("\\s+");
        for (String word : words) {
            shortName.append(word.charAt(0));
        }
        return shortName.toString();
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

    public static String generateCreateTableForForm(String formName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Forms forms;
        forms = parseForm(mapper.readTree(AnalyticsUtil.class.getClassLoader().getResource(formName)));
        Map<String, FormTable> obsWithConcepts = new HashMap<>();
        handleObsControls(forms.getControls(), obsWithConcepts, forms.getName(), null);
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE ").append(AnalyticsUtil.generateColumnName(forms.getName())).append("(");
        query.append("id serial PRIMARY KEY, ");

        Set<String> columnNames = new HashSet<>();
        if (obsWithConcepts.containsKey(forms.getName())) {
            for (FormConcept concept : obsWithConcepts.get(forms.getName()).getConcepts()) {
                String name = AnalyticsUtil.generateColumnName(concept.getName());
                if(columnNames.contains(name)) {
                    String errorMessage = String.format("There is a collision for column name %s." +
                            " Please update the concept name for one of the form concepts to make it unique.", name);
                    logger.error(errorMessage);
                    throw new RuntimeException(errorMessage);
                }
                columnNames.add(name);
                query.append(name).append(" varchar, ");
            }
        }
        query.append("encounter_id integer, visit_id integer, patient_id integer, ");
        query.append("username varchar, date_created timestamp, patient_identifier varchar, ");
        query.append("location_id integer, location_name varchar, instance_id integer)");

        return query.toString();
    }

    public static void handleObsControls(List<FormControl> controls, Map<String, FormTable> obsWithConcepts,
                                         String formName, String sectionLabel) {
        for (FormControl control : controls) {
            extractConceptsForObsControl(obsWithConcepts, control, formName, sectionLabel);
        }
    }
    public static void extractConceptsForObsControl(Map<String, FormTable> obsWithConcepts,
                                                    FormControl control, String tableName, String sectionLabel) {
        if (control.getType().equals(JobConstants.LABEL)) {
            return;
        }
        if (control.getType().equals(JobConstants.OBS_CONTROL_GROUP)) {
            handleObsControls(control.getControls(), obsWithConcepts, tableName, null);
        } else if (control.getType().equals(JobConstants.OBS_SECTION_CONTROL)) {
            handleObsControls(control.getControls(), obsWithConcepts, tableName, control.getLabel().getValue());
        } else {
            if (!obsWithConcepts.containsKey(tableName)) {
                obsWithConcepts.put(tableName, new FormTable(tableName));
            }
            FormTable formTable = obsWithConcepts.get(tableName);
            FormConcept formConcept = control.getConcept();

            if (control.getProperties().getMultiSelect() != null && control.getProperties().getMultiSelect()) {
                generateFormConceptsForMultiselectColumns(formTable, formConcept);
            }
            else {
                generateConceptForColumn(sectionLabel, formTable, formConcept);
            }
            formTable.setProperties(control.getProperties());
        }
    }

    private static void generateConceptForColumn(String sectionLabel, FormTable formTable, FormConcept formConcept) {
        String conceptName = formConcept.getName();
        formConcept.setName(getShortName(conceptName));
        if (sectionLabel != null) {
            formConcept.setName(sectionLabel + "_" + formConcept.getName());
        }
        formTable.getConcepts().add(formConcept);
    }

    private static void generateFormConceptsForMultiselectColumns(FormTable formTable, FormConcept formConcept) {
        //Generate a boolean column for each possible value of the multiselect.
        String multiSelectFieldName = getShortName(formConcept.getName());
        multiSelectFieldName = getInitialsForName(multiSelectFieldName);
        for(ConceptAnswer answerConcept : formConcept.getAnswers()) {
            FormConcept answerConceptName = answerConcept.getName();
            String multiSelectOptionName = answerConceptName.getName();
            multiSelectOptionName = multiSelectFieldName + "_" + getShortName(multiSelectOptionName);
            answerConceptName.setName(multiSelectOptionName);
            formTable.getConcepts().add(answerConceptName);
        }
    }

    private static Forms parseForm(JsonNode array){
        JsonNode resources = array.get("formJson").get("resources");
        String version = array.get("formJson").get("version").asText();
        String value = resources.get(0).get("value").toString();
        ObjectMapper mapper = new ObjectMapper();
        Forms c = null;
        try {
            c = mapper.readValue(value.replace("\\", "").replaceAll("^\"|\"$", ""), Forms.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

