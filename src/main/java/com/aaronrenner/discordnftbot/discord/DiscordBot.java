package com.aaronrenner.discordnftbot.discord;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.permission.Permissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.aaronrenner.discordnftbot.models.Contract;
import com.aaronrenner.discordnftbot.models.DiscordProperties;
import com.aaronrenner.discordnftbot.models.Ticker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DiscordBot {
	
	/** Required */
	private DiscordApi disc = null;
	private static final Logger LOGGER  = LoggerFactory.getLogger(DiscordBot.class);
	
	public DiscordBot(DiscordProperties config) {
		try {
	        this.disc = new DiscordApiBuilder().setToken(config.getDiscordToken()).login().join();
	        
	        startupLogger();
	        
	        if(!disc.getAccountType().name().equalsIgnoreCase("BOT")) {
	        	LOGGER.error("The account token is not for a BOT account!");
	        	throw new RuntimeException("The account token is not for a BOT account!");
	        }
		} catch (Exception e) {
			LOGGER.error("Failed starting bot! Exception: " + e.getMessage());
        	throw new RuntimeException(e.getMessage());
		}
	}
	
	public void updateStatus(Contract contract) {
		this.disc.updateActivity(ActivityType.WATCHING, String.format("the %s%s floor!", contract.getFloor(), Ticker.ETH.getSymbol()));
	}
	
	public void updateHolders(Contract contract) {
		contract.getServerChannel().updateName(String.format("Holders: %s", contract.getHolders()));
	}
	
	private void startupLogger() throws JsonProcessingException {
		LOGGER.info("--------");
		LOGGER.debug("Discord bot config: "+ new ObjectMapper().writeValueAsString(disc.getApplicationInfo().join()));
        LOGGER.debug("Discord connection started!");
        LOGGER.info("Need this bot in your server? " + disc.createBotInvite(Permissions.fromBitmask(19520)));
        LOGGER.info("--------");
	}

	public DiscordApi getDiscordApi() {
		return this.disc;
	}
}