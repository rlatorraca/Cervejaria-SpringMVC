package com.rlsp.cervejaria.thymeleaf.processor;

import javax.servlet.http.HttpServletRequest;

import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

public class MenuAttributeTagProcessor extends AbstractAttributeTagProcessor {

	private static final String NOME_ATRIBUTO = "menu";
	private static final int PRECEDENCIA = 1000;
	
	public MenuAttributeTagProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, null, false, NOME_ATRIBUTO, true, PRECEDENCIA, true);
	}
	
	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
			String attributeValue, IElementTagStructureHandler structureHandler) {
		
		/**
		 * Pega a URI da pagina que esta sendo ACESSADA
		 */		
		IEngineConfiguration configuration = context.getConfiguration();  //Pega o Contexto
		IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
		IStandardExpression expression = parser.parseExpression(context, attributeValue);  // Pega o valor do 'attributeValue' do Menu ex: "/estilos"
		String menu = (String) expression.execute(context);  // Menu = "/cervejaria/cerveja"   ==> Contexto Correto
		
		/**
		 * Pega a URI '/cerveajaria/**'
		 */
		HttpServletRequest request = ((IWebContext) context).getRequest(); 
		String uri = request.getRequestURI();
		
		/**
		 * Adicionara isActive(css classe), PARA MOSTRAR o MENU EXPANDIDO, se a URI comecar com  (startsWith)
		 *  - Ex: /cervejaria/cerveja/* ==> adiciona isActive para todas a paginas a partir dessa URI
		 */
		

		if (uri.matches(menu)) {
			String classesExistentes = tag.getAttributeValue("class");
			structureHandler.setAttribute("class", classesExistentes + " is-active");
		}
		
		/**
		 *	if (uri.startsWith(menu)) {
				String classesExistentes = tag.getAttributeValue("class");
				structureHandler.setAttribute("class", classesExistentes + " is-active");
			} 		 
		 */
		
		
	}

}
