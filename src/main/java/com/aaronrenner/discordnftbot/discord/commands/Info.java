package com.aaronrenner.discordnftbot.discord.commands;

import java.awt.Color;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import com.aaronrenner.discordnftbot.models.Contract;
import com.aaronrenner.discordnftbot.models.ContractCollection;

public class Info {

	public Info(Color botColor, Message message, ContractCollection contractCollection) {
		EmbedBuilder messageBuilder = new EmbedBuilder().setColor(botColor).setAuthor("Currently watching 👀:");
		
		for(Contract c : contractCollection.getContractCollection()) {
			String contractAddress 	 = c.getContractAddress();
			String interval 		 = String.valueOf(c.getInterval());
			String watchingInChannel = c.getServerChannel().getName();
			String inServer 		 = c.getServer().getName();
			
			String responseMessage = 
				"Address: "  			  + contractAddress + "\n" +
				"Interval: " 			  + interval 		+ "\n" +
				"Enable Holders: "		  + c.isEnableHolders();
			
			messageBuilder.addField(
				// Header
				"Server: " 	 			  + inServer + ", Channel: " + watchingInChannel,
				// Data
				responseMessage
			);
		}
		
		new MessageBuilder().setEmbed(messageBuilder).send(message.getUserAuthor().get());
	}

}
