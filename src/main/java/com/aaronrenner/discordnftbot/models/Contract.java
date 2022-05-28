package com.aaronrenner.discordnftbot.models;

import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.server.Server;
import com.aaronrenner.discordnftbot.discord.DiscordBot;
import com.aaronrenner.discordnftbot.services.StatsScheduler;
import com.aaronrenner.discordnftbot.utils.JsonUtils;
import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames=true)
@Data
public class Contract {
	
	/** Permanent */
	private JsonUtils jsonUtils = new JsonUtils();

	/** Passed into the Class */
	private String contractAddress;
	private int interval;
	private Server server;
	private ServerChannel serverChannel;
	private DiscordBot bot;
	private StatsScheduler newRequest;
	private String openseaApiKey;
	
	private boolean enableHolders = false;
	
	/** Support for slug based API requests in OpenSea **/
	private Boolean isSlug = false;
	
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
}
