package com.rlsp.cervejaria.config.init;

import javax.servlet.Filter;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.rlsp.cervejaria.config.JPAConfig;
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
		
		return new Class<?>[] { JPAConfig.class , ServiceConfig.class};
		//return null;
	}

	/**
	 * Ensina o SPRING a encontrar os CONTROLLERs na parte WEB
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		
		return new Class<?>[] { WebConfig.class }; // ==> Dentro dp PACOTE "com.rlsp.brewer.config"
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
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8"); //Seta o encoding para UTF-8
		characterEncodingFilter.setForceEncoding(true); // Para que o filtro UTF-8 seja aplicado sempre
		
		return new Filter[] { characterEncodingFilter };
	}
	
	

}
