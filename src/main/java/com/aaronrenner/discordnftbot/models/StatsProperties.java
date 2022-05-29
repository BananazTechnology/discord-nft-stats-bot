package com.aaronrenner.discordnftbot.models;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.aaronrenner.discordnftbot.discord.DiscordBot;

public class StatsProperties {
	
	/** Declared FINAL variables */
	private static final int FIELDSCOUNT = 3;
	private static final String CONTRACTADDRESS = "contractAddress";
	private static final String COLLECTIONSLUG  = "collectionSlug";
	private static final String INTERVAL = "interval";
	private static final String ENABLEHOLDERS = "enableHolders";
	private static final String HOLDERSOUTPUT = "holdersOutputChannelId";
	private static final String[] FIELDS = {CONTRACTADDRESS, INTERVAL};
	private static final String[] OPTIONALFIELDS = {ENABLEHOLDERS, HOLDERSOUTPUT};
	private static final Logger LOGGER = LoggerFactory.getLogger(StatsProperties.class);

	public void configProperties(Set<Map<String, String>> listingsArray, DiscordBot bot, ContractCollection contractCollection, String apiKeyOpensea) throws RuntimeException, InterruptedException {
		if(listingsArray != null && listingsArray.size() > 0 && bot != null && apiKeyOpensea != null) {
			for(Map<?,?> contract : listingsArray) {
				if(contract.size() >= FIELDSCOUNT && !apiKeyOpensea.equals("")) {
					try {
					
						/** Validation and setting of contractAddress */
						String contractAddress = (String) contract.get(CONTRACTADDRESS);
						String collectionSlug  = (String) contract.get(COLLECTIONSLUG);
						String searchString;
						Boolean isSlug = false;
						if(contractAddress != null) {
							searchString = contractAddress;
							if(!contractAddress.matches("^[a-zA-Z0-9]{30,43}$")) throw new RuntimeException("Check contractAddress " + contractAddress + ", does not match ^[a-zA-Z0-9]{30,43}$!");
						} else {
							searchString = collectionSlug;
							isSlug = true;
						}
						
						/** Validation and setting of contractAddress */
						int interval = Integer.valueOf(contract.get(INTERVAL).toString());
						
						/** Validation and setting of server and the outputChannel!!! */
						Server thisServer = null;
						ServerChannel outputChannel = null;
						
						/** Grab metadata filter */
						String enableHoldersValue = (String) contract.get(ENABLEHOLDERS);
						boolean enableHolders = (enableHoldersValue != null) ? Boolean.valueOf(enableHoldersValue) : false;

						if(enableHolders) {
							/** Attempt connection to Discord, attempt loading server and channel */
							try {
								Collection<Server> server = bot.getDiscordApi().getServers();
								if(!(server.size() > 0)) throw new RuntimeException("Bot not in any servers!");
								for(Server newServer : server) {
									for(ServerChannel channel : newServer.getChannels()) {
										if(channel.canYouSee() && channel.getIdAsString().equalsIgnoreCase(contract.get(HOLDERSOUTPUT).toString())) {
											
											thisServer    = newServer;
											outputChannel = channel;
										}
									}
									if(outputChannel == null) throw new RuntimeException(String.format("No channels on the server matched the ID \"%s\", Maybe you cannot see it?", contract.get(HOLDERSOUTPUT).toString()));
								}
							} catch(Exception ex) {
								if(ex.getMessage() != null) LOGGER.error(ex.getMessage());
								LOGGER.error("Seems the bot is trying to be started without discord connection on {}, if this is not intended check configuration!", contractAddress);
							}
						}
						
						/** If no server or outputChannel then throw exception */
						Contract newContract = new Contract(searchString, interval);
						newContract.setOpenseaApiKey(apiKeyOpensea);
						if(bot != null) newContract.setBot(bot);
						newContract.setEnableHolders(enableHolders);
						if(enableHolders) {
							newContract.setServer(thisServer);
							newContract.setServerChannel(outputChannel);
						}
						newContract.startListingsScheduler();
						newContract.setIsSlug(isSlug);
						contractCollection.addContract(newContract);
						
					} catch (Exception e) {
						LOGGER.error("Check properties ($.contracts.listins[]) should contain EACH: " + Arrays.toString(FIELDS) + " with optional: " + Arrays.toString(OPTIONALFIELDS) + ", Exception: " + e.getMessage());
						throw new RuntimeException("Check properties ($.contracts.listins[]) should contain EACH: " + Arrays.toString(FIELDS) + " with optional: " + Arrays.toString(OPTIONALFIELDS) + ", Exception: " + e.getMessage());
					}
				} else {
					LOGGER.error("Check properties ($.contracts.listins[]) should contain EACH: " + Arrays.toString(FIELDS) + " with optional: " + Arrays.toString(OPTIONALFIELDS));
					throw new RuntimeException("Check properties ($.contracts.listins[]) should contain EACH: " + Arrays.toString(FIELDS) + " with optional: " + Arrays.toString(OPTIONALFIELDS));
				}
				Thread.sleep(2000);
			}
			if(contractCollection.size()  < listingsArray.size()) throw new RuntimeException(
				"Check properties ($.contracts.listings) one or more entries could not be registered."
				+ " Make sure Discord Server is the server NAME and serverTextChannel is the name of the channel."
				+ " Wrap the values in 'quotes' for words with spaces! PLEASE try to use common '-' or '_' symbols!");
		} else {
			throw new RuntimeException("Check properties https://github.com/Aman7123/discord-nft-bot/README.md");
		}
	}

}
