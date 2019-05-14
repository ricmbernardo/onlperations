package org.onlperations.services;

import java.io.IOException;
import java.util.List;

import twitter4j.Status;
import twitter4j.Trends;
import twitter4j.TwitterException;

public interface TwitterServices {

	public List<Status> searchTweets(String queryStr) throws TwitterException;
	
	public Trends getTrends(int woeid) throws TwitterException;
	
	public List<String> getTweets(String queryStr, String countryCode, String altLang, int resultCount) throws IOException;
	
	public List<String> getTrendingTweets(int woeid, int tweetCountPerTrend, boolean includeTrendInOutput) throws IOException;

}
