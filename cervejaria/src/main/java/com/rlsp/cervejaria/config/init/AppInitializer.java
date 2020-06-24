package com.rlsp.cervejaria.config.init;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.rlsp.cervejaria.config.AWSS3Config;
import com.rlsp.cervejaria.config.JPAConfig;
import com.rlsp.cervejaria.config.MailConfig;
import com.rlsp.cervejaria.config.SecurityConfig;
import com.rlsp.cervejaria.config.ServiceConfig;
import com.rlsp.cervejaria.config.WebConfig;
/**
 * Configurações iniciais para o SERVLETs que iria inicializar os FRONT CONTROLLER DO SPRING (DISPATCHER SERVLET) ==> que chamará os CONTROLLERs
 * Deve ser implementada 3 metodos
 * 
 * @author Rodrigo
 *
 */
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	/**
	 * Ensina o SPRING a encontrar os BEANS de Service antes da WEB (Servlets)
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		
		return new Class<?>[] { JPAConfig.class , ServiceConfig.class,  SecurityConfig.class, AWSS3Config.class};
		//return null;
	}

	/**
	 * Ensina o SPRING a encontrar os CONTROLLERs na parte WEB
	 * OBS: o MailConfig esta sendo configurado aqui pois sera usado o ThymeLeaf para gerar o EMAIL
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		
		return new Class<?>[] { WebConfig.class , MailConfig.class }; // ==> Dentro dp PACOTE "com.rlsp.brewer.config"
	}

	/**
	 * Padrão de URL que será base para o encaminhamento futuro
	 * 
	 * A partir "/" para frente será entregue pro DISPATCHERSERVLET para procurar o CONTROLLER a ser utilizado
	 * "/" : "qualquer requisição"	 * 
	 * "URLmappings" do XML
	 */
	@Override
	protected String[] getServletMappings() {
		
		return new String[] {"/"};
	}
	
	/**
	 * Resolve o problema com acentuação do Portugues
	 */	
	@Override
	protected Filter[] getServletFilters() {
//		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
//		characterEncodingFilter.setEncoding("UTF-8"); //Seta o encoding para UTF-8
//		characterEncodingFilter.setForceEncoding(true); // Para que o filtro UTF-8 seja aplicado sempre
		
//		return new Filter[] { characterEncodingFilter };
		
		/**
		 * Habilita o uso do PUT @PutMapping na aplicacao
		 */
		FormContentFilter httpPutFormContentFilter = new FormContentFilter();
		
		return new Filter[] { httpPutFormContentFilter };
	}
	
	/**
	 * Usado para recuperar os dados MULTIPART (ex: fotos) no Lado do Servidor
	 */
	@Override
	protected void customizeRegistration(Dynamic registration) {
	
		registration.setMultipartConfig(new MultipartConfigElement("")); // O servidor ira salva o arquivo temporario onde achar melhor
	}
	
	
	/**
	 * Usado para configurar o Storage da Foto (storage-local = LOCAL = computador) ou (storage-aws = na aws)
	 */
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
		servletContext.setInitParameter("spring.profiles.default", "local"); // O padrao vai ser n o Computador
	}

}
