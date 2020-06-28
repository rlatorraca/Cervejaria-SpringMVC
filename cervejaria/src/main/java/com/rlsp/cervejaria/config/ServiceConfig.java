package com.rlsp.cervejaria.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.rlsp.cervejaria.service.CadastroCervejaService;
import com.rlsp.cervejaria.storage.FotoStorage;

@Configuration()
@ComponentScan(basePackageClasses =  { CadastroCervejaService.class, FotoStorage.class})
public class ServiceConfig {

	
	/*
	 * @Bean public FotoStorage fotoStorage() { return new FotoStorageLocal(); }
	 */
}
