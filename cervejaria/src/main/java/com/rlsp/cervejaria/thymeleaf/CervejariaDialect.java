package com.rlsp.cervejaria.thymeleaf;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

import com.rlsp.cervejaria.thymeleaf.processor.ClassForErrorAttritbuteTagProcessor;
import com.rlsp.cervejaria.thymeleaf.processor.MenuAttributeTagProcessor;
import com.rlsp.cervejaria.thymeleaf.processor.MessageElementTagProcessor;
import com.rlsp.cervejaria.thymeleaf.processor.OrderElementTagProcessor;
import com.rlsp.cervejaria.thymeleaf.processor.PaginationElementTagProcessor;


@Component
public class CervejariaDialect extends AbstractProcessorDialect {


	public CervejariaDialect() {
		super("RLSP Cervejaria", "cervejaria", StandardDialect.PROCESSOR_PRECEDENCE);
	}
	
	@Override
	public Set<IProcessor> getProcessors(String dialectPrefix) {
		
		final Set<IProcessor> processadores = new HashSet<>();
	
		processadores.add(new ClassForErrorAttritbuteTagProcessor(dialectPrefix));
		processadores.add(new MessageElementTagProcessor(dialectPrefix));
		processadores.add(new OrderElementTagProcessor(dialectPrefix));
		processadores.add(new PaginationElementTagProcessor(dialectPrefix));
		processadores.add(new MenuAttributeTagProcessor(dialectPrefix));
		
		return processadores;
	}

}