package com.rlsp.cervejaria.thymeleaf.processor;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring5.util.FieldUtils;
import org.thymeleaf.templatemode.TemplateMode;


/**
 * PROCESSADOR para o DIALETO = CervejariaDialect
 *  - processa a seguinte tag do thymeleaf :
 *  	** th:classappend="${#fields.hasErrors('valor')} ? has-error"
 */
public class ClassForErrorAttritbuteTagProcessor extends AbstractAttributeTagProcessor{

	
	private static final String NOME_ATRIBUTO = "classforerror";
	private static final int PRECEDENCIA = 1000;

	
//	protected ClassForErroAttritbuteTagProcessor(TemplateMode templateMode, String dialectPrefix, String elementName,
//			boolean prefixElementName, String attributeName, boolean prefixAttributeName, int precedence,
//			boolean removeAttribute) {
//		super(templateMode, dialectPrefix, elementName, prefixElementName, attributeName, prefixAttributeName, precedence,
//				removeAttribute);		
//	}

		
	public ClassForErrorAttritbuteTagProcessor(String dialectPrefix) {
		//super(templateMode, dialectPrefix, elementName, prefixElementName, attributeName, prefixAttributeName, precedence,removeAttribute);
		super(TemplateMode.HTML, dialectPrefix, null, false, NOME_ATRIBUTO, true, PRECEDENCIA, true);
	}

		
	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
			String attributeValue, IElementTagStructureHandler structureHandler) {
		
		boolean temErro = FieldUtils.hasErrors(context, attributeValue);
		
		if (temErro) {
			String classesExistentes = tag.getAttributeValue("class");					// Pegaa as classes existentes no CONTEXTO (pagina html)
			structureHandler.setAttribute("class", classesExistentes + " has-error");   // Substitui (deleta e acrescenta) a classes + classe "has-error"
		}
	}

}
