package com.aaronrenner.discordnftbot.utils;

import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import net.minidev.json.JSONObject;

public class UrlUtils {

	private RestTemplate restTemplate	= new RestTemplate();
	private StringUtils sUtils			= new StringUtils();
	private JsonUtils jUtils    	 	= new JsonUtils();
	private static final Logger LOGGER  = LoggerFactory.getLogger(UrlUtils.class);
	
	public JSONObject getRequest(String getURL) throws Exception {
		// Variables for runtime
		URI createURI = sUtils.getURIFromString(getURL);
		ResponseEntity<String> result = null;
		JSONObject newResponse = new JSONObject();
		// Variables for timing
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();
		try {
			result = restTemplate.getForEntity(createURI, String.class);
			newResponse = jUtils.stringToJsonObject(result.getBody());
		} catch (Exception e) {
			LOGGER.error("Failed HTTP GET");
			throw new Exception("Failed HTTP GET");
		}
		LOGGER.debug(String.format("GET request took %sms", Long.valueOf(endTime-startTime).toString()));
		return newResponse;
	}
}
