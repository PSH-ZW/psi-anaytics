package com.nuchange.psianalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan(basePackages = {"com.nuchange.psianalytics"})
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(com.nuchange.psianalytics.PsiAnalyticsApplication.class, args);
    }
}
