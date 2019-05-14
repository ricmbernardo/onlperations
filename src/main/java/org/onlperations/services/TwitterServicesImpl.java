package org.onlperations.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

@Service
public class TwitterServicesImpl implements TwitterServices{
	
	private static final Logger LOGGER = LogManager.getLogger(TwitterServicesImpl.class);

	Twitter twitter;
	
	public TwitterServicesImpl() {
		this.twitter = TwitterFactory.getSingleton();
	}
	
	public List<Status> searchTweets(String queryStr) throws TwitterException {
		QueryResult result = null;		
		Query query = new Query(queryStr);
		result = twitter.search(query);
		return result.getTweets();
	}
	
	
	public Trends getTrends(int woeid) throws TwitterException {
		return twitter.trends().getPlaceTrends(woeid);
	}
	
	public List<String> getTweets(String queryStr, String countryCode, String altLang, int resultCount) throws IOException {

		List<String> tweets = new ArrayList<String>();		
		Twitter twitter = TwitterFactory.getSingleton();
		Query query = new Query(queryStr);
		query.setCount(resultCount);
		query.setResultType(Query.POPULAR);
		QueryResult result = null;
		
		try {
				result = twitter.search(query);
				for(Status status : result.getTweets()) {
					if((status.getFavoriteCount() > 10) 
							&& ((status.getPlace()!=null && status.getPlace().getCountryCode().equalsIgnoreCase(countryCode))
							|| status.getLang().equalsIgnoreCase(altLang)
							|| status.getLang().equalsIgnoreCase("en"))) {
						//LOGGER.info(status.getFavoriteCount() + "Adding tweet(" + status.getLang() + "," + status.getPlace() + "): " + status.getText() + "\n");
						tweets.add(NLPServiceUtilities.cleanUpSentence(status.getText()));
					}
				}
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tweets;
	}
	
	public List<String> getTrendingTweets(int woeid, int tweetCountPerTrend, boolean includeTrendInOutput) throws IOException {

		List<String> tweets = new ArrayList<String>();
		try {
			//Note: PH = 23424934
			Trends trends = this.getTrends(woeid);
			for(int i = 0; i < trends.getTrends().length; i++) {
				String trendStr = (trends.getTrends()[i]).getName();
				String queryStr = (trends.getTrends()[i]).getQuery();
				List<String> trendTweets = this.getTweets(queryStr, "PH", "tl", tweetCountPerTrend);
				for(String trendTweet : trendTweets) {
					if (includeTrendInOutput) {
						tweets.add(trendStr.replaceAll("#", "") + " " + trendTweet);
					} else {
						tweets.add(trendTweet);
					}
				}
			}
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tweets;
	}	
	
}
