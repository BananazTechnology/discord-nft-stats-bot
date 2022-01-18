package com.aaronrenner.discordnftbot.models;

import java.math.BigDecimal;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.server.Server;
import com.aaronrenner.discordnftbot.discord.DiscordBot;
import com.aaronrenner.discordnftbot.services.StatsScheduler;
import com.aaronrenner.discordnftbot.utils.JsonUtils;
import com.aaronrenner.discordnftbot.utils.OpenseaUtils;
import lombok.Data;
import lombok.ToString;
import net.minidev.json.JSONObject;

@ToString(includeFieldNames=true)
@Data
public class Contract {
	
	/** Permanent */
	private JsonUtils jsonUtils    				= new JsonUtils();

	/** Passed into the Class */
	private String contractAddress;
	private int interval;
	private Server server;
	private ServerChannel serverChannel;
	private DiscordBot bot;
	private StatsScheduler newRequest;
	private String openseaApiKey;

	// Holds the current floor when needing to be refreshed
	private BigDecimal floor;
	private int holders;
	
	private boolean enableHolders = false;
	
	public Contract(String newContractAddress, int newInterval) {
		this.contractAddress   = newContractAddress;
		this.interval 		   = newInterval;
	}
	
	public void startListingsScheduler() {
		newRequest = new StatsScheduler(this, bot);
		newRequest.start();
	}
	
	public void stopListingsScheduler() {
		newRequest.stop();
	}
	
	public BigDecimal getFloor() {
		setFloor();
		return this.floor;
	}
	
	public int getHolders() {
		setHolders();
		return this.holders;
	}
	
	public void setFloor() {
		setImportant();
	}
	
	public void setHolders() {
		setImportant();
	}
	
	private void setImportant() {
		try {
			/** Make request */
			OpenseaUtils api = new OpenseaUtils(this.openseaApiKey);
			JSONObject getCollectionStats = api.getCollectionStatsByContractAddress(this.contractAddress);
			JSONObject getStats = (JSONObject) getCollectionStats.get("stats");
			/** Get statistic items */
			// Floor
			String collectionFloor = getStats.getAsString("floor_price");
			this.floor = BigDecimal.valueOf(Double.valueOf(collectionFloor));

			String holders = getStats.getAsString("num_owners");
			this.holders = Integer.valueOf(holders);
		} catch (Exception e) {}
	}
}
