package org.onlperations.services;

import opennlp.tools.util.Span;

public interface NLPServices {
	
	public String[] tag(String sentence);
	
	public Span[] findNamedEntity(String sentence, String entityType);

}
