package org.onlperations.services;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	
	public List<Status> searchTweets() throws TwitterException {
		Query query = new Query("#MyFamilyIsStrangerThanYours");
		QueryResult result = null;
		//try {
			result = twitter.search(query);
		//} catch (TwitterException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
		return result.getTweets();
	}
	
	
	public Trends getTrends(int woeid) throws TwitterException {
		//Trends trends = new Trends();
		//try {
			return twitter.trends().getPlaceTrends(woeid);
		//} catch (TwitterException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
		//return null;
	}
}
