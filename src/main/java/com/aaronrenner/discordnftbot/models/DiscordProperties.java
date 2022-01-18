package com.aaronrenner.discordnftbot.models;

import java.util.Map;
import com.aaronrenner.discordnftbot.discord.DiscordBot;
import lombok.Data;

@Data
public class DiscordProperties {
	
	private static final String TOKEN = "token";
	private static final String COMMANDPREFIX = "commandPrefix";
	
	private String discordToken;
	private String commandPrefix;
	private ContractCollection contractCollection;

	public DiscordBot configProperties(Map<Object, Object> discordInfo, ContractCollection contractCollection) throws RuntimeException {
		/** Variables of the discord root node*/
		this.contractCollection = contractCollection;
		this.discordToken = (String) discordInfo.get(TOKEN);
		this.commandPrefix = (String) discordInfo.get(COMMANDPREFIX);
		
		return new DiscordBot(this);
	}

}
