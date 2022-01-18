package com.aaronrenner.discordnftbot.discord;

import java.awt.Color;
import java.util.Optional;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import com.aaronrenner.discordnftbot.discord.commands.*;
import com.aaronrenner.discordnftbot.models.ContractCollection;

public class Commands implements MessageCreateListener {

	// Custom
	private String commandPrefix = null;
	private Color botColor 		 = Color.ORANGE;
	
	// Required
	private ContractCollection contractCollection;
	private static final PermissionType adminPerms = PermissionType.ADMINISTRATOR;
	
	public Commands(String commandPrefix, ContractCollection contCollect) {
		this.contractCollection = contCollect;
		this.commandPrefix 		= commandPrefix;
	}
	
	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		/** The message event for this action */
		Message message = event.getMessage();
		Optional<User> userSender = message.getUserAuthor();
		Optional<Server> serverSentIn = message.getServer();
		
		if (message.getContent().startsWith(commandPrefix)) {
			/** Prints the bots activity */
			if(message.getContent().equalsIgnoreCase(commandPrefix+"info"))
				new Info(this.botColor, message, this.contractCollection);
			
			/** Can toggle role based ping system */
			if(message.getContent().equalsIgnoreCase(commandPrefix+"refreshFloors")
				&& userHasAdminPerms(userSender, serverSentIn))
					new RefreshFloors(message, this.contractCollection);
			
			/** Prints help message */
			if(message.getContent().equalsIgnoreCase(commandPrefix+"help"))
				new Help(userSender, serverSentIn, this.botColor, message, this.contractCollection, this.commandPrefix);
        }
	}
	
	private boolean userHasAdminPerms(Optional<User> user, Optional<Server> server) {
		boolean response = false;
		if(!user.isEmpty() && !server.isEmpty()) {
			User userObj = user.get();
			Server serverObj = server.get();
			if(serverObj.getPermissions(userObj).getAllowedPermission().contains(adminPerms)) {
				response = true;
			}
		}
		return response;
	}

}
