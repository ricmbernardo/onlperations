package org.onlperations.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.onlperations.services.NLPServiceUtilities;
import org.onlperations.services.TwitterServices;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledOperations {
	
	private static final Logger LOGGER = LogManager.getLogger(ScheduledOperations.class);
	
	@Resource
	TwitterServices twitterServices;
	
	@Scheduled(cron = "0 0/15 * * * *")
	public void createSentenceDetectorTrainingData() {
		
		LOGGER.info("Running job to update Sentence Detector Training Data");
		
		try {
			List<String> tweets = twitterServices.getTrendingTweets(23424934, 500);
			NLPServiceUtilities.writeToFile("./data/train/trendingtweets.txt", tweets);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.debug("Error encountered while creating Sentence Detector Training Data: " + e);
		}

	}

}
