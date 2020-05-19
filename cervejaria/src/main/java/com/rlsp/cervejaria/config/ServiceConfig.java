package com.rlsp.cervejaria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.rlsp.cervejaria.storage.FotoStorage;
import com.rlsp.cervejaria.storage.local.FotoStorageLocal;

@Configuration()
@ComponentScan(basePackages = "com.rlsp.cervejaria.service")
public class ServiceConfig {

	
	@Bean
	public FotoStorage fotoStorage() {
		return new FotoStorageLocal();
	}
}
