package com.porter.common.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

/**
 * Default Java Serialization. Will only work with classes that implement
 * Serializable and that don't have circular references.
 * 
 * @author fpiagent
 * 
 */
public class JavaSerializer implements ISerializer {

	private Logger log = Logger.getLogger(JavaSerializer.class.getName());

	public String serializeObject(Object o) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(o);
		} catch (IOException e) {
			log.error("Java Serialization Error", e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
				// ignore close exception
			}
			try {
				bos.close();
			} catch (IOException ex) {
				// ignore close exception
			}
		}
		return new String(bos.toByteArray());
	}

	public Object deserializeObject(String o) {
		ByteArrayInputStream bis = new ByteArrayInputStream(o.getBytes());
		ObjectInput in = null;
		try {
			in = new ObjectInputStream(bis);
			return in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			log.error("Java Deserialization Error", e);
		} finally {
			try {
				bis.close();
			} catch (IOException ex) {
				// ignore close exception
			}
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				// ignore close exception
			}
		}
		o.getBytes();
		throw new UnsupportedOperationException();
	}
}
