package com.porter.common.serialization;

/**
 * Thrift is designed for RPC mainly using a persistent connection protocol
 * different to TCP.
 * 
 * @author fpiagent
 * 
 */
public class ThriftSerializer implements ISerializer {

	public String serializeObject(Object o) {
		throw new UnsupportedOperationException();
	}

	public Object deserializeObject(String o) {
		throw new UnsupportedOperationException();
	}

}
