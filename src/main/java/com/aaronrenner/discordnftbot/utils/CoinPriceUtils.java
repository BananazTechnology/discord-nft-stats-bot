package com.aaronrenner.discordnftbot.utils;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class CoinPriceUtils {
	
	private final String BASE_URL = "https://api.coinstats.app/public/v1/tickers?exchange=coinbase";
	private final String ETH_PAIR = "&pair=ETH-USD";
	private final String SOL_PAIR = "&pair=SOL-USD";
	private UrlUtils uUtils = new UrlUtils();
	
	public double ethToSol(double ethPrice) throws Exception {
		JSONObject ethResponse = uUtils.getRequest(BASE_URL+ETH_PAIR);
		JSONArray ethTicker    = (JSONArray) ethResponse.get("tickers");
		JSONObject ethObj      = (JSONObject) ethTicker.get(0);
		double ethInUsd        = Double.valueOf(ethObj.getAsString("price"));
		double totalPriceInUSD = ethPrice * ethInUsd;
		
		JSONObject solResponse = uUtils.getRequest(BASE_URL+SOL_PAIR);
		JSONArray solTicker    = (JSONArray) solResponse.get("tickers");
		JSONObject solObj      = (JSONObject) solTicker.get(0);
		double solInUsd        = Double.valueOf(solObj.getAsString("price"));
		return (totalPriceInUSD / solInUsd);
	}

}
