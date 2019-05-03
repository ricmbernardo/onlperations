package org.onlperations.services;

import java.util.List;

import twitter4j.Status;
import twitter4j.Trends;
import twitter4j.TwitterException;

public interface TwitterServices {

	public List<Status> searchTweets() throws TwitterException;
	
	public Trends getTrends(int woeid) throws TwitterException;
}
