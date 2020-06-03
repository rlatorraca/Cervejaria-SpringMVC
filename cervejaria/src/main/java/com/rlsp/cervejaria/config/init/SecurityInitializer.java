package com.rlsp.cervejaria.config.init;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * Serve para configurar o Spring Security (Autenticacao e Autorizacao de acesso as paginas)
 *  - Inicializando os FILTROS
 * @author rlatorraca
 *
 */
public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {

	
	/**
	 * CODIGO PARA ACENTUACAO, que antes era no "AppInitilizaer.getServletFilters()", aplos adicioanr Spring Security
	 */
	@Override
	protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
		FilterRegistration.Dynamic characterEncodingFilter = servletContext.addFilter("encodingFilter",
				new CharacterEncodingFilter());
		characterEncodingFilter.setInitParameter("encoding", "UTF-8");
		characterEncodingFilter.setInitParameter("forceEncoding", "true");
		characterEncodingFilter.addMappingForUrlPatterns(null, false, "/*");
	}
}
