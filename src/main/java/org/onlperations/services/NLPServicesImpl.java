package org.onlperations.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.onlperations.controller.NLPOperationsController;
import org.onlperations.entity.ConversationInput;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.Span;

@Service
public class NLPServicesImpl implements NLPServices {
	
	private static final Logger LOGGER = LogManager.getLogger(NLPServicesImpl.class);
	
	private static final String ENTITY_TYPE_NAME = "NAM";
	private static final String ENTITY_TYPE_DATE = "DAT";
	private static final String ENTITY_TYPE_LOCATION = "LOC";
	private static final String ENTITY_TYPE_MONEY = "MON";
	private static final String ENTITY_TYPE_ORGANIZATION = "ORG";
	private static final String ENTITY_TYPE_TIME = "TIM";
	
	//Part of Speech tagger
	POSTaggerME tagger;
	
	//Person Name finder
	NameFinderME nameFinder;
	
	//Date Name finder
	NameFinderME dateFinder;
	
	//Location Name finder
	NameFinderME locationFinder;
	
	//Money Name finder
	NameFinderME moneyFinder;
	
	//Organization Name finder
	NameFinderME organizationFinder;
	
	//Time Name finder
	NameFinderME timeFinder;
	
	NLPServicesImpl() throws IOException{
		try(InputStream modelIn = new ClassPathResource("en-pos-maxent.bin").getInputStream()) {
			POSModel model = new POSModel(modelIn);
			this.tagger = new POSTaggerME(model);
		}
		
		try(InputStream modelIn = new ClassPathResource("en-ner-person.bin").getInputStream()) {
			TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
			this.nameFinder = new NameFinderME(model);
		}

		try(InputStream modelIn = new ClassPathResource("en-ner-date.bin").getInputStream()) {
			TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
			this.dateFinder = new NameFinderME(model);
		}

		try(InputStream modelIn = new ClassPathResource("en-ner-location.bin").getInputStream()) {
			TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
			this.locationFinder = new NameFinderME(model);
		}

//		try(InputStream modelIn = new ClassPathResource("en-ner-money.bin").getInputStream()) {
//			TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
//			this.moneyFinder = new NameFinderME(model);
//		}

		try(InputStream modelIn = new ClassPathResource("en-ner-organization.bin").getInputStream()) {
			TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
			this.organizationFinder = new NameFinderME(model);
		}

//		try(InputStream modelIn = new ClassPathResource("en-ner-time.bin").getInputStream()) {
//			TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
//			this.timeFinder = new NameFinderME(model);
//		}

		System.out.println("Init success");
	}

	public String[] tag(String sentence) {
		String[] sentenceArr = sentence.split(" ");
		return this.tagger.tag(sentenceArr);
	}
	
	public Span[] findNamedEntity(String sentence, String entityType) {
		
		LOGGER.info("sentence: " + sentence);
		LOGGER.info("entityType: " + entityType);
		
		String[] sentenceArr = sentence.split(" ");
		
		Span[] spanArr = null;
		
		switch (entityType) {
			case ENTITY_TYPE_NAME:
				spanArr = this.nameFinder.find(sentenceArr);
				break;

			case ENTITY_TYPE_DATE:
				spanArr = this.dateFinder.find(sentenceArr);
				break;

			case ENTITY_TYPE_LOCATION:
				spanArr = this.locationFinder.find(sentenceArr);
				break;

			case ENTITY_TYPE_MONEY:
				spanArr = this.moneyFinder.find(sentenceArr);
				break;

			case ENTITY_TYPE_ORGANIZATION:
				spanArr = this.organizationFinder.find(sentenceArr);
				break;

			case ENTITY_TYPE_TIME:
				spanArr = this.timeFinder.find(sentenceArr);
				break;

			default:
				break;
		}
		
		return spanArr;
	}

}
