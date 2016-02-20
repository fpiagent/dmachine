package com.porter.common.serialization;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * Xstream is an XML serializer, the payload will always be greater than JAVA
 * and Json serializers, but we use it for comparissons reasons.
 * 
 * @author fpiagent
 * 
 */
public class XstreamSerializer implements ISerializer {

	XStream xstream = new XStream(new StaxDriver());

	public String serializeObject(Object o) {
		return xstream.toXML(o);
	}

	public Object deserializeObject(String o) {
		return xstream.fromXML(o);
	}

}
