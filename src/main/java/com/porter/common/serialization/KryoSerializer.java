package com.porter.common.serialization;

import java.io.ByteArrayOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Kryo Serializer was built with performance and flexibility in mind. Doesn't
 * require to implement Serializable and it's considered the fastest Java
 * Serializer in the market.
 * 
 * Con: It's only for JAVA
 * 
 * @author fpiagent
 * 
 */
public class KryoSerializer implements ISerializer {

	private Kryo kryo = new Kryo();

	public String serializeObject(Object o) {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		Output output = new Output(outStream, 4096);
		kryo.writeClassAndObject(output, o);
		return new String(output.toBytes());
	}

	public Object deserializeObject(String o) {
		Input input = new Input(o.getBytes());
		return kryo.readClassAndObject(input);
	}

}
