package com.rlsp.cervejaria.config;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.number.NumberStyleFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.github.mxab.thymeleaf.extras.dataattribute.dialect.DataAttributeDialect;
import com.rlsp.cervejaria.controller.BeerController;
import com.rlsp.cervejaria.controller.converter.CidadeConverter;
import com.rlsp.cervejaria.controller.converter.EstadoConverter;
import com.rlsp.cervejaria.controller.converter.EstiloConverter;
import com.rlsp.cervejaria.controller.converter.GrupoConverter;
import com.rlsp.cervejaria.session.TabelaItensVenda;
import com.rlsp.cervejaria.session.TabelasItensSession;
import com.rlsp.cervejaria.thymeleaf.CervejariaDialect;

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
 * basePackageClasses = BeerController.class ==> diz para que o sistema encontre os CONTROLLers que esta na classe "BeersController" (é um VETOR, usa-se {})
 * 
 * @WebMvcConfigurer ==> implementa algumas CLASSES que serão usadas para as configurações do SPRING MVC para WEB (ex: formatador,etc)
 * 
 * @EnableSpringDataWebSupport ==> adiciona o SUporte a PAGINACAO do Spring Data
 * 
 * @EnableCaching ==> habilantando a possibilidade de CACHE (gravar em Memoria RAM)
 *
 */

@Configuration
@ComponentScan(basePackageClasses = {BeerController.class, TabelasItensSession.class})
@EnableWebMvc
@EnableSpringDataWebSupport
@EnableCaching
public class WebConfig implements WebMvcConfigurer {
//public class WebConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware{


	/**
	 * Objeto do SPRING. Quando aplicao subir receberá o CONTEXT (devido a Implementação da Interface ApplicationContextAware)
	 */	
	@Autowired
	private ApplicationContext applicationContext;

//	@Override
//	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//		this.applicationContext = applicationContext;
//	}

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

		//Insere o Dialeto de Templates Personalizado
		engine.addDialect(new CervejariaDialect());
		
		//Insere o Dialeto de Thymeleaf data attributes no HTML (data:"nome_tag")
		engine.addDialect(new DataAttributeDialect());
		
		//Insere o Dialeto de Spring Security EXTRAS - Nome do Usuario Logado
				engine.addDialect(new SpringSecurityDialect());
		
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
		conversionService.addConverter(new CidadeConverter());
		conversionService.addConverter(new EstadoConverter());
		conversionService.addConverter(new GrupoConverter());
		
		/**
		 * Converte valores BigDecimais que entrarem no form respeitando o padrao da mascara
		 */
		NumberStyleFormatter bigDecimalFormatter = new NumberStyleFormatter("#,###,##0.00");
		conversionService.addFormatterForFieldType(BigDecimal.class, bigDecimalFormatter);
		
		NumberStyleFormatter integerFormatter = new NumberStyleFormatter("#,##0");
		conversionService.addFormatterForFieldType(Integer.class, integerFormatter);
		
		// API de Datas a partir JAVA 8
		DateTimeFormatterRegistrar dateTimeFormatter = new DateTimeFormatterRegistrar();
		dateTimeFormatter.setDateFormatter(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		dateTimeFormatter.registerFormatters(conversionService);
		
		return conversionService;
	}

	/**
	 * Implementacao do CACHE
	 *  - Esse padrao eh do SPRING (
	 *  - NAO RECOMENDADO para producao
	 */
	@Bean
	public CacheManager chacheManager() {
		 return new ConcurrentMapCacheManager();  //guarda o CACHE em MAP (SPRING)
		
		 /* >> Usado para GUAVA
		 CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder()
				.maximumSize(3) // Maximo de 3 objetos
				.expireAfterAccess(20, TimeUnit.SECONDS); //expira depois de X tempo
		
		
		GuavaCacheManager cacheManager = new GuavaCacheManager();
		cacheManager.setCacheBuilder(cacheBuilder);
		return cacheManager;
		*/
	}
	
	/*
	 *  Usado para TRADUCAO e INTERNACIONALIZACAO das mensagens
	 */
	@Bean
	public MessageSource messageSourcer() {
		ReloadableResourceBundleMessageSource bundle = new ReloadableResourceBundleMessageSource();
		bundle.setBasename("classpath:/messages");
		bundle.setDefaultEncoding("UTF-8"); // http://www.utf8-chartable.de
		return bundle;
	}
	
	
	/*
	 * - Usando para fazer a INTEGRACAO entre o SPRING MVC + SPRING JPA, permitindo a busca de um Elemento de uma ENTIDADE
	 *  sem, precisar usar a funcao/metodo findById()
	 * - Dessa forma fara a busca e conversao usando a URL (Ex.: "/item/{codigoCerveja}") do mapeamento para fazer a busca de elemento  
	 */
	@Bean
	public DomainClassConverter<FormattingConversionService> domainClassConverter() {
		return new DomainClassConverter<FormattingConversionService>(mvcConversionService());
	}

}
