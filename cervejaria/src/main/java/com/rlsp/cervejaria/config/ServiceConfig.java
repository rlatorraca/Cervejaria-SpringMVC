package com.rlsp.cervejaria.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.rlsp.cervejaria.service.CadastroCervejaService;

@Configuration()
@ComponentScan(basePackageClasses = CadastroCervejaService.class)
public class ServiceConfig {

}
