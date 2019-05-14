package org.onlperations.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vdurmont.emoji.EmojiParser;

import opennlp.tools.util.Span;

public class NLPServiceUtilities {
	
	private static final Logger LOGGER = LogManager.getLogger(NLPServiceUtilities.class);
	
	private static final String urlRegex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	private static final String hashtagRegex = "#\\p{L}+";
	private static final String mentionRegex = "@\\p{L}+";
	private static final String unnecessaryCharsRegex = "[^a-zA-Z0-9\\s+!,.]";
	
	public static <T> List<T> getValueAtSpan(Span span, T[] objectArr) {
		
		List<T> spanList = new ArrayList<T>();
		
		//T spanArr[] = new T[span.length()];
		//int spanArrCnt = 0;
		
		for(int cnt = span.getStart(); cnt < span.getEnd(); cnt++) {
			//spanArr[spanArrCnt++] = objectArr[cnt];
			spanList.add(objectArr[cnt]);
		}
		
		return spanList;
		
	}
	
	public static String cleanUpSentence(String sentence) {
		
		//Remove emojis
		String cleanSentence = EmojiParser.removeAllEmojis(sentence);		

		//Remove URL, mentions, hashtags
		try {
			cleanSentence = cleanSentence.replaceAll(urlRegex, "");
			//cleanSentence = cleanSentence.replaceAll(hashtagRegex, "");
			cleanSentence = cleanSentence.replaceAll("#", "");
			cleanSentence = cleanSentence.replaceAll(mentionRegex, "");
			cleanSentence = cleanSentence.replaceAll(unnecessaryCharsRegex, "");
			cleanSentence = cleanSentence.replaceAll("\\n", " ");
			cleanSentence = cleanSentence.replaceAll("\\r", " ");
			cleanSentence = cleanSentence.replaceAll("\\s{2,}", " ");
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
			LOGGER.debug("Error encountered " + e);
		}
		
		
		return cleanSentence;
		
	}
	
	private static boolean isMatch(String s, String regexPattern) {
		
		boolean isMatch = true;
		
		try {
			Pattern pattern = Pattern.compile(regexPattern);
			Matcher matcher = pattern.matcher(s);
		} catch (RuntimeException e) {
			// TODO: handle exception
			LOGGER.debug("Error matching " + s);
			isMatch = false;
		}
		
		return isMatch;
		
	}
	
	public static void writeToFile(String filePath, List<String> tweets) {
		
		try {
			
			//filePath = "./data/train/trendingtweets.txt"
			File f = new File(filePath);
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
			
			for(String tweet : tweets) {
				fw.write(tweet + "\n");
			}
			fw.close();

		} catch (IOException e) {
			// TODO: handle exception
			LOGGER.debug("Error encountered: " + e);
		} finally {
			
		}
		
		
	}

}
