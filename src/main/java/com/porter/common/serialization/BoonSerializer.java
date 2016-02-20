package com.porter.common.serialization;

import org.boon.json.JsonParser;
import org.boon.json.JsonParserFactory;
import org.boon.json.JsonSerializer;
import org.boon.json.JsonSerializerFactory;

/**
 * Boon is a JSON Serializer supposed to be the most performant in the market at
 * the time of this check in.
 * 
 * @author fpiagent
 * 
 */
public class BoonSerializer implements ISerializer {

	JsonSerializer serializer = new JsonSerializerFactory().create();
	JsonParser jsonParser = new JsonParserFactory().create();

	@Override
	public String serializeObject(Object o) {
		return serializer.serialize(o).toString();
	}

	@Override
	public Object deserializeObject(String o) {
		return jsonParser.parse(o);
	}

}
