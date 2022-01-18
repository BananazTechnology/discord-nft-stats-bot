package com.aaronrenner.discordnftbot.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

public class StringUtils {
	
	private static final int MAXHASHTAGS = 4;

	/**
	 * This is a helper method, in this method you can supply a valid 
	 * String of a web address and receive a Java URI for other methods.
	 * 
	 * @param string A String containing a web address
	 * @return A java.net URI for all your web needs
	 * @since 1.0
	 */
	public URI getURIFromString(String string) {
		URI newURI = null;
		try {
			newURI = new URI(string);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return newURI;
	}
	
	public String addressSimple(String address, String bidderENSLookup) {
		String returnString;
		returnString = (address.equals(bidderENSLookup)) ? address.substring(0, 8) : bidderENSLookup;
		return returnString;
	}
	
	/**
	 * For twitter
	 * @param headerList
	 * @return
	 */
	public String getRandomHeader(List<String> headerList) {
		Collections.shuffle(headerList);
		return headerList.get(0);
	}
	
	/**
	 * For twitter
	 * @param hashtagList
	 * @return
	 */
	public String getRandomHashtags(List<String> hashtagList) {
		// init blank string for all hashtags
		String hashtagBuffer = "";
		// shuffle hashtag list
		Collections.shuffle(hashtagList);
		for(int i = 0; (i < MAXHASHTAGS) && (i < hashtagList.size()); i++) {
			// adds new hashtag to the hashtag buffer
			hashtagBuffer+=hashtagList.get(i);
			// dontn forget the space, better ui look
			hashtagBuffer+=" ";
		}
		return hashtagBuffer;
	}
}
