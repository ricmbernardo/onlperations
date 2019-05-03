package org.onlperations.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	//public ResponseEntity<List<Status>> getTweetsTest() {
	public ResponseEntity<List<String>> getTweetsTest() {

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
				LOGGER.info("Name: " + name);
				LOGGER.info("Query: " + queryStr);
				
				query = new Query(queryStr);
				query.setCount(5);
				query.setResultType(Query.POPULAR);
				result = twitter.search(query);
				
				for(Status status : result.getTweets()) {
					LOGGER.info("Adding tweet: " + status.getText() + "\n");
					tweets.add(status.getText());
				}
				
			}
			
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.info("Tweets: " + result.getTweets());
		HttpHeaders responseHeaders = new HttpHeaders();
		//return new ResponseEntity<List<Status>>(result.getTweets(), responseHeaders, HttpStatus.OK);
		return new ResponseEntity<List<String>>(tweets, responseHeaders, HttpStatus.OK);
	}

}
