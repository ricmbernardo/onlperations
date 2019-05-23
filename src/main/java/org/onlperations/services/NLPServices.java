package org.onlperations.services;

import java.io.IOException;

import opennlp.tools.util.Span;

public interface NLPServices {
	
	public String[] tag(String sentence);
	
	public Span[] findNamedEntity(String sentence, String entityType);
	
	public String[] stanfordTag(String sentence, String language);
	
	public String categorize(String sentence, String categorizerType);
	
	public void trainCategorizer() throws IOException;

}
