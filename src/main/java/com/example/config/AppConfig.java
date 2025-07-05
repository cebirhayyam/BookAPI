package com.example.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;


@Configuration
public class AppConfig {

	private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);
	
	@PostConstruct
	private void onStartup() {
		logger.info("Uygulama başlatıldı.");
	}
	
	@PreDestroy
	private void onShutdown() {
		logger.info("Uygulama sonlandı.");
	}
}
