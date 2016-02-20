package com.porter.product.couchbase;

import org.apache.log4j.Logger;

import rx.functions.Action1;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.transcoder.JsonTranscoder;
import com.porter.common.AbstractPerformanceRunner;
import com.porter.common.CRUDEnum;

/**
 * Couchbase has both a Community and Enterprise editions and a wide stack of
 * features. This has been included in the PoC. They usually use fewer nodes,
 * one per dedicated machine, but provide High Availabilty in a cluster-like
 * all-to-all topology and High Response time. Persistence to Disk is mandatory.
 * 
 * @author fpiagent
 * 
 */
public class CouchbasePerformanceRunner extends AbstractPerformanceRunner {

	private Logger log = Logger.getLogger(CouchbasePerformanceRunner.class
			.getName());

	private JsonTranscoder jsonTranscoder = new JsonTranscoder();
	private Bucket bucket;

	// Mixing up the entire CRUD. Create - Reads/Update/Deletes+Create
	public static void main(String[] args) {
		startTest(new CouchbasePerformanceRunner(), args);
	}

	@Override
	public String getProductName() {
		return "COUCHBASE";
	}

	@Override
	public boolean doCreate(int id) throws Exception {
		JsonDocument jdoc = JsonDocument.create(String.valueOf(id),
				getJsonObject());

		JsonDocument result = bucket.upsert(jdoc);
		return (result != null && result.content() != null);
	}

	@Override
	public boolean doRead(int id) throws Exception {
		JsonDocument result = bucket.get(String.valueOf(id));
		if (result != null) {
			if (props.getSerialization().equals("json")) {
				deserializeObject(jsonTranscoder.jsonObjectToString(result
						.content()));
			} else {
				deserializeObject(result.content().get("val"));
			}
			return result.id().equals(String.valueOf(id));
		}
		return false;
	}

	@Override
	public boolean doUpdate(int id) throws Exception {
		JsonDocument jdoc = JsonDocument.create(String.valueOf(id),
				getJsonObject());
		JsonDocument result = bucket.upsert(jdoc);
		return (result != null && result.content() != null);
	}

	@Override
	public boolean doDelete(int id) {
		JsonDocument result = bucket.remove(String.valueOf(id));
		return (result != null && result.id().equals(String.valueOf(id)));
	}

	@Override
	public void beforeTest() throws Exception {
		// Product Specific Steps
		Cluster cluster = CouchbaseCluster.create(props.getHost());
		bucket = cluster.openBucket("perfTest");
	}

	@Override
	public void afterTest() {
		bucket.close();
	}

	@Override
	public boolean doAsyncCreate(int id) throws Exception {
		JsonDocument jdoc = JsonDocument.create(String.valueOf(id),
				getJsonObject());
		bucket.async().upsert(jdoc).doOnError(new Action1<Throwable>() {
			public void call(Throwable t1) {
				log.error("Create Async Exception", t1);
				exceptions.put(CRUDEnum.C, exceptions.get(CRUDEnum.C) + 1);
			}
		});

		return true;
	}

	@Override
	public boolean doAsyncRead(int id) {
		bucket.async().get(String.valueOf(id))
				.doOnError(new Action1<Throwable>() {
					public void call(Throwable t1) {
						log.error("Read Async Exception", t1);
						exceptions.put(CRUDEnum.R,
								exceptions.get(CRUDEnum.R) + 1);
					}
				});
		return true;
	}

	@Override
	public boolean doAsyncUpdate(int id) throws Exception {
		JsonDocument jdoc = JsonDocument.create(String.valueOf(id),
				getJsonObject());
		bucket.async().upsert(jdoc).doOnError(new Action1<Throwable>() {
			public void call(Throwable t1) {
				log.error("Update Async Exception", t1);
				exceptions.put(CRUDEnum.U, exceptions.get(CRUDEnum.U) + 1);
			}
		});
		return true;
	}

	@Override
	public boolean doAsyncDelete(int id) {
		bucket.async().remove(String.valueOf(id))
				.doOnError(new Action1<Throwable>() {
					public void call(Throwable t1) {
						log.error("Delete Async Exception", t1);
						exceptions.put(CRUDEnum.D,
								exceptions.get(CRUDEnum.D) + 1);
					}
				});
		return true;
	}

	private JsonObject getJsonObject() throws Exception {
		if (props.getSerialization().equals("json")) {
			return jsonTranscoder.stringToJsonObject(getSerializedObject());
		} else {
			return JsonObject.create().put("val", getSerializedObject());
		}
	}

}
