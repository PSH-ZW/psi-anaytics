package com.nuchange.psianalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.nuchange.psianalytics"})
@SpringBootConfiguration
public class PsiAnalyticsApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(PsiAnalyticsApplication.class, args);
	}

	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(30);
		return threadPoolTaskScheduler;
	}

}
