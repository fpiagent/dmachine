package com.porter.common;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Object Container and Parsing Class, fetches properties config file from args[0] when running the
 * jar and overrides default properties stated statically here.
 * 
 * @author fpiagent
 * 
 */
public class InputProperties {

	/**
	 * INPUT PARAMETERS
	 */
	private Long rndSeed = 123L;
	private Long actionRndSeed = 111L;
	private Integer C = 50; // %5
	private Integer R = 600; // %60
	private Integer U = 330; // %33
	private Integer D = 20; // %2
	private Boolean async = true;
	private String host = "sample.hostname.com";
	private Integer idFrom = 0;
	private Integer idTo = 50;
	private Boolean deterministic = false;
	private Boolean debugMode = false;
	private Integer actions = 1000;
	private Integer runSeconds = 0;
	private String strategy = "direct";
	private String serialization = "json";
	private Integer delaySeconds = 5;

	private Properties prop;

	private static Logger log = Logger.getLogger(InputProperties.class
			.getName());

	public InputProperties(String path) {
		prop = new Properties();
		try {
			prop.load(new FileInputStream(path));

			log.info("--- TEST PROPERTIES ---");
			async = extractBooleanNotNull(async, "ASYNC");
			C = extractIntegerNotNull(C, "CREATE");
			R = extractIntegerNotNull(R, "READ");
			U = extractIntegerNotNull(U, "UPDATE");
			D = extractIntegerNotNull(D, "DELETE");
			host = extractStringNotNull(host, "HOST");
			idFrom = extractIntegerNotNull(idFrom, "ID_FROM");
			idTo = extractIntegerNotNull(idTo, "ID_TO");
			rndSeed = extractLongNotNull(rndSeed, "ACCESS_RANDOM_SEED");
			actionRndSeed = extractLongNotNull(actionRndSeed,
					"ACTIONS_RANDOM_SEED");
			deterministic = extractBooleanNotNull(deterministic,
					"DETERMINISTIC");
			debugMode = extractBooleanNotNull(debugMode, "DEBUG");
			actions = extractIntegerNotNull(actions, "ACTIONS");
			runSeconds = extractIntegerNotNull(runSeconds, "TIME");
			strategy = extractStringNotNull(strategy, "STRATEGY");
			serialization = extractStringNotNull(serialization, "SERIALIZATION");
			delaySeconds = extractIntegerNotNull(delaySeconds, "BEGIN_DELAY");

		} catch (Exception e) {
			log.error("Properties Could not be loaded", e);
		}

		log.info("- Asynchronious Actions: " + async);
		log.info("- Server Host: " + host);
		log.info("- Id Created From: " + idFrom);
		log.info("- Id Created To: " + idTo);
		log.info("- CREATE: " + C);
		log.info("- READ: " + R);
		log.info("- UPDATE: " + U);
		log.info("- DELETE: " + D);
		log.info("- Deterministic: " + deterministic);
		log.info("- DEBUG: " + debugMode);
		log.info("- ACTIONS: " + actions);
		log.info("- TIME: " + runSeconds);
		log.info("- STRATEGY: " + strategy);
		log.info("- SERIALIZATION: " + serialization);
		log.info("--- ---");
		log.info("=>Test Starting in " + delaySeconds + " Seconds");
	}

	private Long extractLongNotNull(Long defaultVal, String key) {
		if (prop.getProperty(key) != null) {
			return Long.valueOf(prop.getProperty(key));
		}
		return defaultVal;
	}

	private String extractStringNotNull(String defaultVal, String key) {
		if (prop.getProperty(key) != null) {
			return prop.getProperty(key);
		}
		return defaultVal;
	}

	private Integer extractIntegerNotNull(Integer defaultVal, String key) {
		if (prop.getProperty(key) != null) {
			return Integer.valueOf(prop.getProperty(key));
		}
		return defaultVal;
	}

	private Boolean extractBooleanNotNull(Boolean defaultVal, String key) {
		if (prop.getProperty(key) != null) {
			return Boolean.valueOf(prop.getProperty(key));
		}
		return defaultVal;
	}

	public Long getRndSeed() {
		return rndSeed;
	}

	public void setRndSeed(Long rndSeed) {
		this.rndSeed = rndSeed;
	}

	public Long getActionRndSeed() {
		return actionRndSeed;
	}

	public void setActionRndSeed(Long actionRndSeed) {
		this.actionRndSeed = actionRndSeed;
	}

	public Integer getC() {
		return C;
	}

	public void setC(Integer c) {
		C = c;
	}

	public Integer getR() {
		return R;
	}

	public void setR(Integer r) {
		R = r;
	}

	public Integer getU() {
		return U;
	}

	public void setU(Integer u) {
		U = u;
	}

	public Integer getD() {
		return D;
	}

	public void setD(Integer d) {
		D = d;
	}

	public Boolean isAsync() {
		return async;
	}

	public void setAsync(Boolean async) {
		this.async = async;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getIdFrom() {
		return idFrom;
	}

	public void setIdFrom(Integer idFrom) {
		this.idFrom = idFrom;
	}

	public Integer getIdTo() {
		return idTo;
	}

	public void setIdTo(Integer idTo) {
		this.idTo = idTo;
	}

	public Boolean isDeterministic() {
		return deterministic;
	}

	public void setDeterministic(Boolean deterministic) {
		this.deterministic = deterministic;
	}

	public Boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(Boolean debugMode) {
		this.debugMode = debugMode;
	}

	public Integer getActions() {
		return actions;
	}

	public void setActions(Integer actions) {
		this.actions = actions;
	}

	public Integer getRunSeconds() {
		return runSeconds;
	}

	public void setRunSeconds(Integer runSeconds) {
		this.runSeconds = runSeconds;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public String getSerialization() {
		return serialization;
	}

	public void setSerialization(String serialization) {
		this.serialization = serialization;
	}

	public Integer getDelaySeconds() {
		return delaySeconds;
	}

	public void setDelaySeconds(Integer delaySeconds) {
		this.delaySeconds = delaySeconds;
	}
}
