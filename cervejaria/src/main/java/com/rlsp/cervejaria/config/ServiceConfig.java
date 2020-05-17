package com.rlsp.cervejaria.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration()
@ComponentScan(basePackages = "com.rlsp.cervejaria.service")
public class ServiceConfig {

}
