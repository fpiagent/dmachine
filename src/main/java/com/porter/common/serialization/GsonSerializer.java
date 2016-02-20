package com.porter.common.serialization;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

/**
 * Gson Serializer is the most popular Json serializer, created by Google, not
 * the most performant but flexible and fast to use.
 * 
 * Without explicit instructions to serialize, it is common it gets 
 * 
 * @author fpiagent
 * 
 */
public class GsonSerializer implements ISerializer {

	private Gson gson = new Gson();

	public String serializeObject(Object o) {
		return gson.toJson(o);
	}

	/**
	 * Generic Map Deserialization
	 */
	public Object deserializeObject(String o) {
		return new JsonParser().parse(o);
	}

}
