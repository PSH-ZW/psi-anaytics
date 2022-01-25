package com.nuchange.psianalytics;

import com.nuchange.psianalytics.util.AnalyticsUtil;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
class PsiAnalyticsApplicationTests {

	@Test
	public void shouldCreateQueriesForCreatingTablesFromForms() throws IOException {
		String formName = "forms/Viac_Form_Template_8681_1.json";
		String query = AnalyticsUtil.generateCreateTableForForm(formName);
		Assert.assertNotNull(query);
	}

	@Test
	public void shouldRemoveSpecialCharactersFromColumnNames() throws IOException {
		String formName = "forms/Viac_Form_Template_8681_1.json";
		String query = AnalyticsUtil.generateCreateTableForForm(formName);
		Pattern pattern = Pattern.compile("[/-]");
		Matcher match = pattern.matcher(query);
		assertFalse(match.find());
		assertFalse(query.contains("?"));
		assertFalse(query.contains("."));
		assertFalse(query.contains("&"));
		assertFalse(query.contains("__"));
		assertFalse(query.contains("'"));
		assertFalse(query.contains(":"));
	}

	@Test
	public void shouldGenerateCreateTableQueriesForListOfForms() throws IOException {
		//used to create queries for all the tables.
		List<String> formNames = new ArrayList<>();
		formNames.add("forms/Viac_Form_Template_8681_1.json");
		formNames.add("forms/Art_initial_Visit_compulsory_Question_1_of_2.json");
		formNames.add("forms/Art_initial_Visit_compulsory_Question_2_of_2.json");
		formNames.add("forms/FP_Continuation.json");
		formNames.add("forms/FP_Counselling_Only.json");
		formNames.add("forms/HIV_self_testing.json");
		formNames.add("forms/Post_HIV_test_counselling.json");
		formNames.add("forms/Prep_Cont_Form.json");
		formNames.add("forms/Prep_Init_Form.json");
		formNames.add("forms/Provider_HIV_test_counselling.json");

		List<String> queries = new ArrayList<>();
		for (String formName : formNames) {
			String query = AnalyticsUtil.generateCreateTableForForm(formName);
			queries.add(query);
		}
		assertEquals(10, queries.size());

		Pattern pattern = Pattern.compile("[/-]");
		for (String query : queries) {
			Matcher match = pattern.matcher(query);
			assertFalse(match.find());
			assertFalse(query.contains("?"));
			assertFalse(query.contains("."));
			assertFalse(query.contains("&"));
			assertFalse(query.contains("__"));
			assertFalse(query.contains("'"));
			assertFalse(query.contains(":"));
			assertFalse(query.contains("’"));
		}
	}

	/*@Test
	public void shouldNotThrowExceptionAsVersionIsConsistentInMetaDataTable() throws Exception{
		String formName = "Bahmni^test2.2/39-0";
		Boolean noInconsistency = AnalyticsUtil.noMisMatch(formName);
		assertTrue(noInconsistency);
	}*/
}
