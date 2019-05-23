package org.onlperations.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.onlperations.services.NLPServiceUtilities;
import org.onlperations.services.TwitterServices;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.ml.naivebayes.NaiveBayesTrainer;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

@Component
public class ScheduledOperations {
	
	private static final Logger LOGGER = LogManager.getLogger(ScheduledOperations.class);
	
	@Resource
	TwitterServices twitterServices;
	
	@Scheduled(cron = "0 0/15 * * * *")
	public void createSentenceDetectorTrainingData() {
		
		LOGGER.info("Running job to update Sentence Detector Training Data");
		
		try {
			List<String> tweets = twitterServices.getTrendingTweets(23424934, 500, false);
			NLPServiceUtilities.writeToFile("./data/train/trendingtweets.txt", tweets);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.debug("Error encountered while creating Sentence Detector Training Data: " + e);
		}

	}

	@Scheduled(cron = "0 0/15 * * * *")
	public void createDocumentCategorizerTrainingData() {
		
		LOGGER.info("Running job to update Sentence Detector Training Data");
		
		try {
			List<String> tweets = twitterServices.getTrendingTweets(23424934, 500, true);
			NLPServiceUtilities.writeToFile("./data/train/trendingtweetswithtweets.txt", tweets);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.debug("Error encountered while creating Sentence Detector Training Data: " + e);
		}

	}
	
	//@Scheduled(cron = "0 0/16 * * * *")
	public void trainCategorizer() throws IOException {
		
		DoccatModel model = null;
		File dataIn = null;
		File modelOutFile = null;
		
		try {
			dataIn = new File("./data/train/trendingtweetswithtweets.txt");
			ObjectStream<String> lineStream = new PlainTextByLineStream(new MarkableFileInputStreamFactory(dataIn), "UTF-8");
			ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);
			
			TrainingParameters params = new TrainingParameters();
			params.put(TrainingParameters.CUTOFF_PARAM, Integer.toString(0));
			params.put(TrainingParameters.ALGORITHM_PARAM, NaiveBayesTrainer.NAIVE_BAYES_VALUE);
			
			model = DocumentCategorizerME.train("en", sampleStream, params, new DoccatFactory());
			
			modelOutFile = new File("./src/main/resources/en-fil-doccat-naive-bayes.bin");
			modelOutFile.getParentFile().mkdirs();
			
			try(OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(modelOutFile))) {
				model.serialize(modelOut);
			}
			
			
		} catch (IOException e) {
			// TODO: handle exception
			LOGGER.info(e.getMessage());
		}
		
	}

}
