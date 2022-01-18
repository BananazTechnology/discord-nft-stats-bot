package com.aaronrenner.discordnftbot.utils;

import java.util.Map.Entry;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

public class JsonUtils {

	/**
	 * Gets a value from a JSONObject
	 * 
	 * @param jsonObject
	 *            The JSONObject to get the value from
	 * @param key
	 *            The key to get the value from
	 * @return The value of the key
	 */
	public JSONObject stringToJsonObject(String data) {
		JSONObject jsonData = null;
		try {
			JSONParser jsonParser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
			Object bufferObject = jsonParser.parse(data);
			jsonData = (JSONObject) bufferObject;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jsonData;
	}

	public JSONArray stringToJsonArray(String data) {
		JSONArray jsonData = null;
		try {
			JSONParser jsonParser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
			Object bufferObject = jsonParser.parse(data);
			jsonData = (JSONArray) bufferObject;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jsonData;
	}
	
	/*
	 * This method is used to get a value from a JSONObject.
	 * 
	 * @param jsonObject The JSONObject to get the value from.
	 * @param key The key to get the value from.
	 * @return The value of the key.
	 */
	public String searchObjForKey(JSONObject metadata, String key) {
		String response = "";
		if(metadata.toJSONString().contains("\""+key+"\"")) {
			if(metadata.containsKey(key)) return metadata.getAsString(key);
			for(Entry<String, Object> kv : metadata.entrySet()) {
				if(kv.getValue().getClass().equals(JSONObject.class)) {
					String newResponse = searchObjForKey((JSONObject) kv.getValue(), key);
					if(!newResponse.equals("")) return newResponse;
				}
				if(kv.getValue().getClass().equals(JSONArray.class)) {
					JSONArray value = (JSONArray) kv.getValue();
					for(int i = 0; i < value.size(); i++) {
						JSONObject nestedObj = (JSONObject) value.get(i);
						String newResponse = searchObjForKey(nestedObj, key);
						if(!newResponse.equals("")) return newResponse;
					}
				}
			}
		}
		return response;
	}
	
	
}
