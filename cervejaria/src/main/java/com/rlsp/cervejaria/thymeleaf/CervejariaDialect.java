package com.rlsp.cervejaria.thymeleaf;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

import com.rlsp.cervejaria.thymeleaf.processor.ClassForErrorAttritbuteTagProcessor;
import com.rlsp.cervejaria.thymeleaf.processor.MessageElementTagProcessor;



public class CervejariaDialect extends AbstractProcessorDialect {

//	protected CervejariaDialect(String name, String prefix, int processorPrecedence) {
//		super(name, prefix, processorPrecedence);
//		
//	}

	public CervejariaDialect() {
		super("RLSP Cervejaria", "cervejaria", StandardDialect.PROCESSOR_PRECEDENCE);
	}
	
	@Override
	public Set<IProcessor> getProcessors(String dialectPrefix) {
		
		final Set<IProcessor> processadores = new HashSet<>();
	
		processadores.add(new ClassForErrorAttritbuteTagProcessor(dialectPrefix));
		processadores.add(new MessageElementTagProcessor(dialectPrefix));
		
		return processadores;
	}

}