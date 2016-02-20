package com.porter.common.serialization;

import java.util.HashMap;
import java.util.Map;

/**
 * Serializer Factory handles the mapping of all available serializers and will
 * return the one required by the Runner.
 * 
 * @author fpiagent
 * 
 */
public class SerializerFactory {

	private static Map<String, ISerializer> serializersMap;

	static {
		serializersMap = new HashMap<String, ISerializer>();
		serializersMap.put("json", new GsonSerializer());
		serializersMap.put("java", new JavaSerializer());
		serializersMap.put("thift", new ThriftSerializer());
		serializersMap.put("protobuf", new ProtobufSerializer());
		serializersMap.put("avro", new AvroSerializer());
		serializersMap.put("xml", new XstreamSerializer());
		serializersMap.put("kryo", new KryoSerializer());
		serializersMap.put("boon", new BoonSerializer());
	}

	public static ISerializer getSerializer(String key) {
		return serializersMap.get(key);
	}
}
