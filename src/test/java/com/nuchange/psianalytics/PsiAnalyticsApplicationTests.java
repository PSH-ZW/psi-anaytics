package com.nuchange.psianalytics;

import com.nuchange.psianalytics.util.AnalyticsUtil;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PsiAnalyticsApplication.class)
class PsiAnalyticsApplicationTests {

	@Test
	public void shouldCreateQueriesForCreatingTablesFromForms() throws IOException {
		String formName = "forms/Viac_Form_Template_8681_1.json";
		List<String> query = AnalyticsUtil.generateCreateTableForForm(formName);
		Assert.assertNotNull(query);
	}

	@Test
	public void shouldRemoveSpecialCharactersFromColumnNames() throws IOException {
		String formName = "forms/Viac_Form_Template_8681_1.json";
		List<String> queries = AnalyticsUtil.generateCreateTableForForm(formName);
		Pattern pattern = Pattern.compile("[/-]");
		for(String query : queries) {
			Matcher match = pattern.matcher(query);
			assertFalse(match.find());
			assertFalse(query.contains("?"));
			assertFalse(query.contains("."));
			assertFalse(query.contains("&"));
			assertFalse(query.contains("__"));
		}
	}
}
