package com.nuchange.psianalytics;

import com.nuchange.psianalytics.util.AnalyticsUtil;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PsiAnalyticsApplication.class)
class PsiAnalyticsApplicationTests {

	@Test
	void contextLoads() {
	}

//	@org.junit.Test
	@Test
	public void shouldCreateQueryFromJsonThree() throws IOException {
		String formName = "forms/Vitals_5.json";
		List<String> query = AnalyticsUtil.generateCreateTableForForm(formName);
		Assert.assertNotNull(query);
	}
}
