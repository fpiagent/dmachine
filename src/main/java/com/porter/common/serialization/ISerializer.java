package com.porter.common.serialization;

/**
 * Interface for all Serializer implementations
 * 
 * @author fpiagent
 *
 */
public interface ISerializer {
	
	public String serializeObject(Object o);
	
	public Object deserializeObject(String o);
}
