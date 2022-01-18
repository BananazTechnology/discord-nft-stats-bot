package com.aaronrenner.discordnftbot.models;

import java.util.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import com.aaronrenner.discordnftbot.discord.DiscordBot;

@ConfigurationProperties(prefix = "nft-bot")
public class RunetimeProperties {
	
	/** Important variables needed for Runtime */
	private String apiKeyOpensea;
	private ContractCollection contractListings = new ContractCollection();
	private DiscordBot bot;
	
	public void setApiKeyOpensea(String value) throws RuntimeException {
		this.apiKeyOpensea = value;
	}
	
	/**
	 * Sets the discord object!
	 * @param token
	 * @throws RuntimeException
	 */
	public void setDiscord(Map<Object, Object> discordInfo) throws RuntimeException {
		this.bot = new DiscordProperties().configProperties(discordInfo, this.contractListings);
	}

	/**
	 * Called by the SpringBoot Starter.
	 * Creates all new Listing lisener's!
	 * @param listingsArray
	 */
	public void setStats(Set<Map<String, String>> statsArray) throws Exception {
		new StatsProperties().configProperties(statsArray, this.bot, this.contractListings, this.apiKeyOpensea);
	}
}