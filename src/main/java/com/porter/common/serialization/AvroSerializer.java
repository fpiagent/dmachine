package com.porter.common.serialization;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.log4j.Logger;

import com.porter.objects.order.Order;

/**
 * Requires Avro Schema Definition: http://avro.apache.org/
 * 
 * Automatic Schema Definition doesn't work well with Collection.
 * 
 * @author fpiagent
 * 
 */
public class AvroSerializer implements ISerializer {

	private Logger log = Logger.getLogger(AvroSerializer.class.getName());
	/**
	 * FIXME: Temporary to keep adding functionality
	 */
	// private Schema schema = ReflectData.get().getSchema(Order.class);
	private Schema schema = null;

	public String serializeObject(Object o) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
		DatumWriter<Order> writer = new SpecificDatumWriter<Order>(schema);

		try {
			writer.write((Order) o, encoder);
			encoder.flush();
			out.close();
		} catch (IOException e) {
			log.error("ERROR: Avro Serializer Exception", e);
		}

		return new String(out.toByteArray());
	}

	public Object deserializeObject(String o) {
		SpecificDatumReader<Order> reader = new SpecificDatumReader<Order>(
				schema);
		Decoder decoder = DecoderFactory.get()
				.binaryDecoder(o.getBytes(), null);
		try {
			return reader.read(null, decoder);
		} catch (IOException e) {
			log.error("ERROR: Avro Deserializer Exception", e);
		}
		return null;
	}

}
