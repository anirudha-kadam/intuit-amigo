package com.intuit.craftdemoapps.api.intuitamigo.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

@Configuration
public class AppConfig {

	@Bean
	public ExecutorService executorService() {
		return Executors.newFixedThreadPool(100,
				new ThreadFactoryBuilder().setNameFormat("intuit-amigo-repo-thread-").build());
	}
}
