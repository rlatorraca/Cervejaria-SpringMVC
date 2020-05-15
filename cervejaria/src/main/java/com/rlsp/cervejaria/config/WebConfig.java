package com.rlsp.cervejaria.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.rlsp.cervejaria.controller.BeersController;
import com.rlsp.cervejaria.controller.converter.EstiloConverter;

import nz.net.ultraq.thymeleaf.LayoutDialect;

/**
 * Classe que ensina o SPRING MVC a encontrar os CONTROLLERs
 *  
 * @author Rodrigo Latorraca
 * 
 * @Configuration ==> Classe de configuraação
 * @ComponentScan ==> escanea os CONTROLLERs (as classes de controller)
 * @EnableWebMvc ==> configura��o para um projeto WEB MVC
 * 
 * basePackageClasses = BeersController.class ==> diz para que o sistema encontre os CONTROLLers que esta na classe "BeersController" (é um VETOR, usa-se {})
 * 
 * WebMvcConfigurer ==> implementa algumas CLASSES que serão usadas para as configurações do SPRING MVC para WEB (ex: formatador,etc)
 *
 */

@Configuration
@ComponentScan(basePackageClasses = {BeersController.class})
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer , ApplicationContextAware{

	/**
	 * Objeto do SPRING. Quando aplicao subir receber�á o CONTEXT (devido a Implementação da Interface ApplicationContextAware)
	 */	
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * Resolve / Encontra as páginas HTML e processar os dados (objetos) inseridos nas p�ginas HTML
	 * @Bean ==> faz com que fique disponivel dentro do contexto do SPRING / aplicacao	 * 
	 */
	@Bean
	public ViewResolver viewResolver() {
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();		
		resolver.setTemplateEngine(templateEngine());
		resolver.setCharacterEncoding("UTF-8");
		return resolver;
	}
	

	/**
	 * Pega o METODOS "templateResolver()"  e processa os arquivos HTML
	 * @Bean ==> faz com que fique disponivel dentro do contexto do SPRING / aplicacao
	 * 
	 */
	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setEnableSpringELCompiler(true);
		engine.setTemplateResolver(templateResolver());
		
		//Insere o Dialeto de Templates no Thymeleaf
		engine.addDialect(new LayoutDialect());
		return engine;
	}

	/**
	 * Mostra onde estão os arquivos HTML
	 * applicationContext : Objeto do Spring recebido com a Inicialziacao (implementando "ApplicationContextAware")
	 * 
	 */
	private ITemplateResolver templateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver(); // Resolve templates do SPRING
		resolver.setApplicationContext(applicationContext); 
		resolver.setPrefix("classpath:/templates/"); // onde estão os templates das VIEWS (arquivos HTML)
		resolver.setSuffix(".html"); //
		resolver.setTemplateMode(TemplateMode.HTML);
		return resolver;
	}
	
	/**
	 * Todos os recursos STATICs estarao dentro dessa configuracao.
	 * Quando usamos o padrão "/**", significa dizer que estamos criando um padrão de URL que serve para qualquer URL. 
	 * 		então se fazemos "/**" quer dizer "/QUALQUER_PADRAO_DE_STRING". Então quando temos o código:
	 * 		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
	 * 	Significa que estamos capturando qualquer URL e dizendo que o local dos arquivos estáticos para essa URL é o que definimos depois no próximo trecho de código, 
	 * 		que é "classpath:/static/", ou seja, a pasta static no classpath da nossa aplicação.
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
	}
	
	/**
	 * Registro dos CONVERSORES (String --> Objeto)
	 * 
	 */
	@Bean
	public FormattingConversionService mvcConversionService() {
		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
		conversionService.addConverter(new EstiloConverter());
		
		return conversionService;
	}
}
