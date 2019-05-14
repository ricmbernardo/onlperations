package org.onlperations.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.onlperations.services.NLPServiceUtilities;
import org.onlperations.services.TwitterServices;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@RestController
@RequestMapping("/nllo")
public class NLLOperationsController {
	
	private static final Logger LOGGER = LogManager.getLogger(NLLOperationsController.class);
	
	@Resource
	TwitterServices twitterServices;
	
	@GetMapping(value="/getTweetsTest")
	public ResponseEntity<List<String>> getTweetsTest() throws IOException {

		List<String> tweets = new ArrayList<String>();
		
		Twitter twitter = TwitterFactory.getSingleton();
		Query query = new Query("#MyFamilyIsStrangerThanYours");
		QueryResult result = null;
		try {
			//result = twitter.search(query);
			Trends trends = twitterServices.getTrends(23424934);
			
			for(int i = 0; i < trends.getTrends().length; i++) {
				String name = (trends.getTrends()[i]).getName();
				String queryStr = (trends.getTrends()[i]).getQuery();
				LOGGER.info("=================================================");
				LOGGER.info("Name: " + name);
				LOGGER.info("Query: " + queryStr);
				
				query = new Query(queryStr);
				query.setCount(5);
				query.setResultType(Query.POPULAR);
				//query.setLocale("PH");
				result = twitter.search(query);
				
				for(Status status : result.getTweets()) {
					//LOGGER.info("Adding tweet(" + status.getLang() + "," + status.getPlace() + "): " + status.getText() + "\n");
					
//					if(status.getFavoriteCount() > 100
//							&& (status.getPlace()!=null && status.getPlace().getCountryCode().equalsIgnoreCase("PH"))) {
//						tweets.add(NLPServiceUtilities.cleanUpSentence(status.getText()));
//					}
					if((status.getFavoriteCount() > 50) 
							&& ((status.getPlace()!=null && status.getPlace().getCountryCode().equalsIgnoreCase("PH"))
							|| status.getLang().equalsIgnoreCase("tl")
							|| status.getLang().equalsIgnoreCase("en"))) {
						LOGGER.info(status.getFavoriteCount() + "Adding tweet(" + status.getLang() + "," + status.getPlace() + "): " + status.getText() + "\n");
						tweets.add(NLPServiceUtilities.cleanUpSentence(status.getText()));
					}
					
				}
				LOGGER.info("=================================================\n");
				
			}
			
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Path p = Paths.get("./data/train/trendingtweets.txt");
		//File f = p.toFile();
		File f = new File("./data/train/trendingtweets.txt");
		f.getParentFile().mkdirs();
		FileWriter fw = null;
		
		if(f.exists()) {
			LOGGER.info("Writing to: " + f.getAbsolutePath());
			fw = new FileWriter(f, true);
		} else {
			LOGGER.info("Creating: " + f.getAbsolutePath());
			f.createNewFile();
			fw = new FileWriter(f);
		}
		
		
		//FileWriter writer = new FileWriter("\data\train\trendingtweets.txt");
		//FileWriter writer = new FileWriter(p.toString());
		for(String tweet : tweets) {
			fw.write(tweet + "\n");
		}
		fw.close();
		
		
		//LOGGER.info("Tweets: " + result.getTweets());
		HttpHeaders responseHeaders = new HttpHeaders();
		//return new ResponseEntity<List<Status>>(result.getTweets(), responseHeaders, HttpStatus.OK);
		return new ResponseEntity<List<String>>(tweets, responseHeaders, HttpStatus.OK);
	}
	
	
	@GetMapping(value="/getTrendingTweetsTest")
	public ResponseEntity<List<String>> getTrendingTweets() throws IOException {

		List<String> tweets = twitterServices.getTrendingTweets(23424934, 500, true);
		
		//NLPServiceUtilities.writeToFile("./data/train/trendingtweets.txt", tweets);
		
		HttpHeaders responseHeaders = new HttpHeaders();
		return new ResponseEntity<List<String>>(tweets, responseHeaders, HttpStatus.OK);
	}
	

}
