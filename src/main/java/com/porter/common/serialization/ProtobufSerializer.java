package com.porter.common.serialization;

/**
 * Requires the Generation of a .proto file to serialize. There is no direct
 * autogeneration from Java Object to proto files. There are aux libs like
 * proto-stuff to go from json to proto and back, so we could theorically do
 * java Object to JSON to Proto one time but this will probably loose some type
 * declaration in the way.
 * 
 * @author fpiagent
 * 
 */
public class ProtobufSerializer implements ISerializer {

	public String serializeObject(Object o) {
		throw new UnsupportedOperationException();
	}

	public Object deserializeObject(String o) {
		throw new UnsupportedOperationException();
	}

}
