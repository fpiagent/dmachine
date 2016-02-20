package com.porter.product.memcached;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.porter.common.AbstractPerformanceRunner;

import net.spy.memcached.MemcachedClient;

/**
 * MemcacheD is a relatively old technology, simple and with little features,
 * but simple enough to make it easy to build upon it. There is no communication
 * between Server Nodes, no Replication and no security features.
 * 
 * Built in C, easy to deploy and manage, allows sharding on client side, use of
 * proxies and given it's Open Source, you may add any feature.
 * 
 * @author fpiagent
 * 
 */
public class MemcacheDPerformanceRunner extends AbstractPerformanceRunner {

	private MemcachedClient c;

	public static void main(String[] args) {
		startTest(new MemcacheDPerformanceRunner(), args);
	}

	@Override
	public String getProductName() {
		return "MEMCACHED";
	}

	@Override
	public boolean doCreate(int id) {
		c.set(String.valueOf(id), 60 * 60, super.getSerializedObject());
		// ASYNC by default
		return true;
	}

	@Override
	public boolean doRead(int id) {
		Object result = c.get(String.valueOf(id));
		deserializeObject(result);
		return result != null;
	}

	@Override
	public boolean doUpdate(int id) {
		c.replace(String.valueOf(id), 60 * 60, super.getSerializedObject());
		// ASYNC by default
		return true;
	}

	@Override
	public boolean doDelete(int id) {
		c.delete(String.valueOf(id));
		// ASYNC by default
		return true;
	}

	@Override
	public void beforeTest() throws Exception {
		List<InetSocketAddress> addrs = new ArrayList<InetSocketAddress>();
		addrs.add(new InetSocketAddress("sample.hostname.com", 11212));
		addrs.add(new InetSocketAddress("sample.hostname.com", 11212));
		addrs.add(new InetSocketAddress("sample.hostname.com", 11212));
		c = new MemcachedClient(addrs);
	}

	@Override
	public void afterTest() {
		c.shutdown(10, TimeUnit.SECONDS);
	}

	@Override
	public boolean doAsyncCreate(int id) {
		c.set(String.valueOf(id), 60 * 60, super.getSerializedObject());
		return true;
	}

	@Override
	public boolean doAsyncRead(int id) {
		c.asyncGet(String.valueOf(id));
		return true;
	}

	@Override
	public boolean doAsyncUpdate(int id) {
		c.replace(String.valueOf(id), 60 * 60, super.getSerializedObject());
		return true;
	}

	@Override
	public boolean doAsyncDelete(int id) {
		c.delete(String.valueOf(id));
		return true;
	}

}
