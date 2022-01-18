package com.aaronrenner.discordnftbot.discord.commands;

import java.awt.Color;
import java.util.Optional;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import com.aaronrenner.discordnftbot.models.ContractCollection;

public class Help {
	
	private final PermissionType ADMINPERM = PermissionType.ADMINISTRATOR;

	public Help(Optional<User> userSender, Optional<Server> serverSentIn, Color botColor, Message message, ContractCollection contractCollection, String commandPrefix) {
		final String infoDescription = "Shows the current bot settings!";
		final String helpDescription = "Displays this menu!";
		final String refreshFloorsDescription = "Manually refreshes all floors for all collections!";
		
		EmbedBuilder generalCommands = new EmbedBuilder()
			.setColor(botColor)
			.addField(commandPrefix + "info", 										infoDescription)
			.addField(commandPrefix + "help", 									 	helpDescription)
		;
		
		MessageBuilder messageBuilder = new MessageBuilder();
		
		if(userHasAdminPerms(userSender, serverSentIn)) {
			generalCommands
				.addField(commandPrefix + "refreshFloors", 						refreshFloorsDescription);

		}
		
		messageBuilder.setEmbed(generalCommands).send(message.getUserAuthor().get());
	}
	
	private boolean userHasAdminPerms(Optional<User> user, Optional<Server> server) {
		boolean response = false;
		if(!user.isEmpty() && !server.isEmpty()) {
			User userObj = user.get();
			Server serverObj = server.get();
			if(serverObj.getPermissions(userObj).getAllowedPermission().contains(ADMINPERM)) {
				response = true;
			}
		}
		return response;
	}

}
