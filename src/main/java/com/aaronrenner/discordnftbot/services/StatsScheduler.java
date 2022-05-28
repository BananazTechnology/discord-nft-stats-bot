package com.aaronrenner.discordnftbot.services;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.aaronrenner.discordnftbot.discord.DiscordBot;
import com.aaronrenner.discordnftbot.models.Contract;
import com.aaronrenner.discordnftbot.models.Ticker;
import com.aaronrenner.discordnftbot.utils.OpenseaUtils;

import net.minidev.json.JSONObject;

public class StatsScheduler extends TimerTask {

	/** Resources declared in Runtime */
	private Contract bindingContract;
	private DiscordBot bot;
	
	/** Resources and important */
	private boolean active			= false;
	private Timer timer 		 	= new Timer(); // creating timer
    private TimerTask task; // creating timer task
	
	/** Final */
	private static final Logger LOGGER 		   = LoggerFactory.getLogger(StatsScheduler.class);


	public StatsScheduler(Contract bindingContract, DiscordBot bot) {
		this.bindingContract 	 = bindingContract;
		this.bot 			     = bot;
	}
	
	@Override
	public void run() {
		try {
			if(this.bindingContract != null && this.active) {
				updateInfo();
			}
		} catch (Exception e) {
			LOGGER.error(String.format("Failed during get listing: %s, stack: %s", this.bindingContract.getContractAddress(), Arrays.toString(e.getStackTrace()))); 
		}
	}

	public boolean start() {
		// Creates a new integer between 1-5 and * by 1000 turns it into a second in milliseconds
		// first random number
		int startsIn = (ThreadLocalRandom.current().nextInt(1, 12)*5000);
		if(bot != null && bindingContract != null) {
			active = true;
			this.task = this;
			LOGGER.info(String.format("Starting new ListingsScheduler in %sms for: %s", startsIn, this.bindingContract.toString()));
			// Starts this new timer, starts at random time and runs per <interval> milliseconds
			this.timer.schedule(task, startsIn , this.bindingContract.getInterval());
		}
		return active;
	}
	
	public boolean stop() {
		active = false;
		LOGGER.info("Stopping ListingScheduler on " + this.bindingContract.toString());
		return active;
	}
	
	private void updateInfo() throws Exception {
		logInfo();
		/** Make request */
		OpenseaUtils api = new OpenseaUtils(this.bindingContract.getOpenseaApiKey());
		JSONObject getCollectionStats = 
				(this.bindingContract.getIsSlug()) ? api.getCollectionStatsBySlug(this.bindingContract.getContractAddress()) :
					api.getCollectionStatsByContractAddress(this.bindingContract.getContractAddress());
		JSONObject getStats = (JSONObject) getCollectionStats.get("stats");
		/** Get statistic items */
		// Floor
		String collectionFloor = getStats.getAsString("floor_price");
		String tickerSymbol    = 
				(this.bindingContract.getIsSlug()) ?
						Ticker.SOL.getSymbol() :
							Ticker.ETH.getSymbol();
		String floorValue = String.format("%s%s", collectionFloor, tickerSymbol);
		this.bot.updateStatus(floorValue);
		

		String holders = getStats.getAsString("num_owners");
		if(this.bindingContract.isEnableHolders()) {
			this.bot.updateHolders(this.bindingContract.getServerChannel(), holders);
		}
	}
	
	private void logInfo() {
		LOGGER.info("New check: " + this.bindingContract.toString());
	}
}
