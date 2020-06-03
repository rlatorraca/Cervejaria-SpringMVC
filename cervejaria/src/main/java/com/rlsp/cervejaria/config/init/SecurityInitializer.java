package com.rlsp.cervejaria.config.init;

import java.util.EnumSet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.SessionTrackingMode;

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
		
		/**
		 * Tempos da SESSAO em SEGUNGOS
		 *  - Voltando a pagina da Login
		 * 
		 *  	int tempoDaVidaSessao = 120;
		 * 		servletContext.getSessionCookieConfig().setMaxAge(tempoDaVidaSessao);
		 * 
		 *  PS ==> Pode ser feito no /src/main/webapp/WEB-INF/web.xml		
		*/
		
		/**
		 *  "jSessionID"
		 *   - Configura o JSessionID para ser GERADO por meio de COOKIE e nao por 'url'
		 */
		servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
		
		
		/**
		 * Configuracao dos ACENTOS
		 */
		FilterRegistration.Dynamic characterEncodingFilter = servletContext.addFilter("encodingFilter",
				new CharacterEncodingFilter());
		characterEncodingFilter.setInitParameter("encoding", "UTF-8");
		characterEncodingFilter.setInitParameter("forceEncoding", "true");
		characterEncodingFilter.addMappingForUrlPatterns(null, false, "/*");
	}
}
