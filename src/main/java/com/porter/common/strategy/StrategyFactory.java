package com.porter.common.strategy;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for Strategies, will return the one required by the Runner
 * 
 * @author fpiagent
 * 
 */
public class StrategyFactory {

	private static Map<String, IStrategy> strategiesMap;

	static {
		strategiesMap = new HashMap<String, IStrategy>();
		strategiesMap.put("direct", new DirectStrategy());
		strategiesMap.put("actions", new ActionsStrategy());
		strategiesMap.put("time", new TimeStrategy());
		strategiesMap.put("mixed", new MixedStrategy());
	}

	public static IStrategy getStrategy(String key) {
		return strategiesMap.get(key);
	}
}
