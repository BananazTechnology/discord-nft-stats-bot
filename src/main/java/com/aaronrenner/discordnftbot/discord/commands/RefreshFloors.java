package com.aaronrenner.discordnftbot.discord.commands;

import org.javacord.api.entity.message.Message;
import com.aaronrenner.discordnftbot.models.Contract;
import com.aaronrenner.discordnftbot.models.ContractCollection;

public class RefreshFloors {
	
	public RefreshFloors(Message message, ContractCollection contractCollection) {
		for(Contract c : contractCollection.getContractCollection()) {
			c.setFloor();
		}
		message.addReaction("👍");
	}
}
