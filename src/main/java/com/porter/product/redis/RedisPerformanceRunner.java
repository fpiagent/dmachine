package com.porter.product.redis;

import java.util.ArrayList;
import java.util.List;

import com.porter.common.AbstractPerformanceRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;

/**
 * Redis is a light product created in Product with the smallest memory
 * footprint in the market. Provides a sturdy Master-Slave deployment
 * architecture which has limitations against the Cluster deployment of some
 * competitors. They provide lots of data structures, high speed and easy
 * scalability as exchange.
 * 
 * @author fpiagent
 * 
 */
public class RedisPerformanceRunner extends AbstractPerformanceRunner {

	private ShardedJedis jedis;
	private ShardedJedisPipeline p;

	public static void main(String[] args) {
		startTest(new RedisPerformanceRunner(), args);
	}

	@Override
	public String getProductName() {
		return "REDIS";
	}

	@Override
	public boolean doCreate(int id) {
		String result = jedis.set(String.valueOf(id),
				super.getSerializedObject());
		return result != null && result.equals("OK");
	}

	@Override
	public boolean doRead(int id) {
		String result = jedis.get(String.valueOf(id));
		deserializeObject(result);
		return (result != null && result.length() > 0);
	}

	@Override
	public boolean doUpdate(int id) {
		String result = jedis.set(String.valueOf(id),
				super.getSerializedObject());
		return result != null && result.equals("OK");
	}

	@Override
	public boolean doDelete(int id) {
		Long result = jedis.del(String.valueOf(id));
		return result != null && result > 0;
	}

	@Override
	public void beforeTest() throws Exception {
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo("sample.hostname.com", 6379));
		shards.add(new JedisShardInfo("sample.hostname2.com", 6379));
		shards.add(new JedisShardInfo("sample.hostname3.com", 6379));

		jedis = new ShardedJedis(shards);
		p = jedis.pipelined();
	}

	@Override
	public void afterTest() {
		p.sync();
		jedis.close();
	}

	@Override
	public boolean doAsyncCreate(int id) {
		p.set(String.valueOf(id), (String) super.getSerializedObject());
		return true;
	}

	@Override
	public boolean doAsyncRead(int id) {
		p.get(String.valueOf(id));
		return true;
	}

	@Override
	public boolean doAsyncUpdate(int id) {
		p.set(String.valueOf(id), (String) super.getSerializedObject());
		return true;
	}

	@Override
	public boolean doAsyncDelete(int id) {
		p.del(String.valueOf(id));
		return true;
	}

}
