package com.porter.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.porter.common.serialization.ISerializer;
import com.porter.common.serialization.SerializerFactory;
import com.porter.common.strategy.IStrategy;
import com.porter.common.strategy.StrategyFactory;
import com.porter.objects.OrderBuilder;
import com.porter.objects.order.Order;

/**
 * Main Class for DMachine, centralizes all the activity from:
 * - Chosen Serializer (Invoked Using Factory)
 * - Chosen Strategy (Invoked Using Factory)
 * - Chosen Product (Runner Implementation in Strategy Design Pattern)
 * 
 * After run it's in charge of printing generic STATS from the run and handling exceptions.
 * 
 * @author fpiagent
 *
 */
public abstract class AbstractPerformanceRunner {

	protected static Random rnd;

	private Order o;
	private ISerializer serializer;

	private static Logger log = Logger
			.getLogger(AbstractPerformanceRunner.class.getName());

	protected Map<CRUDEnum, Integer> exceptions = new HashMap<CRUDEnum, Integer>();

	/**
	 * INPUT PROPERTIES
	 */
	protected static InputProperties props;

	public AbstractPerformanceRunner() {
		log.info("=> WELCOME TO THE DMACHINE");
		log.info("=> About to Run " + getProductName() + " DMachine Test");
		log.info("=> Using Object: Order.java from AF");

		OrderBuilder ob = new OrderBuilder();
		o = ob.build();
	}

	public static void startTest(AbstractPerformanceRunner test, String[] args) {
		processArgs(args);
		test.runTest();
	}

	/**
	 * First and only argument should be the path to the properties file (name
	 * included)
	 * 
	 * @param args
	 */
	private static void processArgs(String[] args) {
		String propsPath = null;
		if (args != null && args.length > 0) {
			log.warn("=>WARNING: NO PATH TO PROPERTIES PROVIDED, USING DEFAULT VALUES");
			propsPath = args[0];
		}
		log.info("=>Loading props from:" + propsPath);
		props = new InputProperties(propsPath);

		/**
		 * ACTIVATE DEBUG MODE
		 */
		if (props.isDebugMode()) {
			log.setLevel(Level.DEBUG);
			log.info("=>DEBUG MODE ON");
		}

		/**
		 * SET DETERMINISTIC / NON DETERMINISTIC
		 */
		if (props.isDeterministic()) {
			rnd = new Random(props.getRndSeed());
		} else {
			rnd = new Random();
		}

		/**
		 * SLEEP TO ALLOW TESTER USER TO SET UP
		 */
		try {
			TimeUnit.SECONDS.sleep(props.getDelaySeconds());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 1- Start Timer 2- Create all assigned entries (using idFrom and idTo) 3-
	 * run actionRnd to choose between C,R,U,D
	 */
	public void runTest() {
		log.info("=> Serialization Method: " + props.getSerialization());
		serializer = SerializerFactory.getSerializer(props.getSerialization());

		exceptions.put(CRUDEnum.C, 0);
		exceptions.put(CRUDEnum.R, 0);
		exceptions.put(CRUDEnum.U, 0);
		exceptions.put(CRUDEnum.D, 0);

		try {
			beforeTest();
		} catch (Exception e) {
			log.error("ERROR: Connection To Caching Service Failed", e);
		}

		StopWatch myWatch = new StopWatch();

		log.info("=>RUNNING ...");

		// CREATE ALL ASSIGNED ENTRIES
		for (int i = props.getIdFrom(); i < props.getIdTo(); i++) {
			log.debug(">INITIAL INSERT:" + i);
			try {
				doCreate(i);
			} catch (Exception e) {
				log.error("ERROR: Initial Creation Failed", e);
				return;
			}
		}

		myWatch.start();

		/**
		 * FETCHING STRATEGY AND RUNNNING IT
		 */
		IStrategy strategy = StrategyFactory.getStrategy(props.getStrategy());
		strategy.run(props, this, rnd);

		myWatch.stop();

		afterTest();

		// DELETE ALL ASSIGNED ENTRIES
		for (int i = props.getIdFrom(); i < props.getIdTo(); i++) {
			log.debug(">FINAL REMOVE:" + i);
			try {
				doDelete(i);
			} catch (Exception e) {
				log.debug(">>FINAL REMOVE EXCEPTION", e);
			}
		}

		log.info("=> TOTAL Milisecs Taken:" + myWatch.getTime());
		log.info("=========================");
		log.info("== EXCEPTIONS ANALYSIS ==");
		log.info("=========================");
		log.info("=> TOTAL Errors: "
				+ (exceptions.get(CRUDEnum.C) + exceptions.get(CRUDEnum.R)
						+ exceptions.get(CRUDEnum.U) + exceptions
							.get(CRUDEnum.D)));
		log.info("=> Create Errors: " + exceptions.get(CRUDEnum.C));
		log.info("=> Read Errors: " + exceptions.get(CRUDEnum.R));
		log.info("=> Update Errors: " + exceptions.get(CRUDEnum.U));
		log.info("=> Delete Errors: " + exceptions.get(CRUDEnum.D));
	}

	public int doCreateAction(int Crun, int id, StopWatch cWatch) {
		log.debug("> INSERT:" + id);
		cWatch.resume();
		try {
			boolean r;
			if (props.isAsync())
				r = doAsyncCreate(id);
			else
				r = doCreate(id);

			if (r)
				Crun++;
		} catch (Exception e) {
			exceptions.put(CRUDEnum.C, exceptions.get(CRUDEnum.C) + 1);
			log.error(">> EXCEPTION: ", e);
		}
		cWatch.suspend();

		return Crun;
	}

	public int doReadAction(int Rrun, int id, StopWatch rWatch) {
		log.debug("> GET:" + id);
		rWatch.resume();
		try {
			boolean r;
			if (props.isAsync())
				r = doAsyncRead(id);
			else
				r = doRead(id);
			if (r)
				Rrun++;
		} catch (Exception e) {
			exceptions.put(CRUDEnum.R, exceptions.get(CRUDEnum.R) + 1);
			log.error(">> EXCEPTION: ", e);
		}
		rWatch.suspend();

		return Rrun;
	}

	public int doUpdateAction(int Urun, int id, StopWatch uWatch) {
		log.debug("> UPDATE:" + id);
		uWatch.resume();
		try {
			boolean r;
			if (props.isAsync())
				r = doAsyncUpdate(id);
			else
				r = doUpdate(id);
			if (r)
				Urun++;
		} catch (Exception e) {
			exceptions.put(CRUDEnum.U, exceptions.get(CRUDEnum.U) + 1);
			log.error(">> EXCEPTION: ", e);
		}
		uWatch.suspend();
		return Urun;
	}

	public int doDeleteAction(int Drun, int id, StopWatch dWatch) {
		log.debug("> DELETE:" + id);
		dWatch.resume();
		try {
			boolean r;
			if (props.isAsync())
				r = doAsyncDelete(id);
			else
				r = doDelete(id);
			if (r)
				Drun++;
		} catch (Exception e) {
			exceptions.put(CRUDEnum.D, exceptions.get(CRUDEnum.D) + 1);
			log.error(">> EXCEPTION: ", e);
		}
		dWatch.suspend();

		return Drun;
	}

	/**
	 * Serializes Object Using the selected Serialization Method
	 * 
	 * @return
	 */
	protected String getSerializedObject() {
		String serializedObj = serializer.serializeObject(o);
		// PAYLOAD MEASUREMENT
		log.debug("> Serialized Payload:" + serializedObj.getBytes().length);
		return serializedObj;
	}

	/**
	 * Deserializes Object using the selected Serialization Method
	 * 
	 * @param toDeserialize
	 * @return
	 */
	protected Object deserializeObject(Object toDeserialize) {
		if (toDeserialize == null) {
			return null;
		}
		return serializer.deserializeObject((String) toDeserialize);
	}

	/**
	 * To Prepare the test, connect to the Cluster and whatever is needed.
	 */
	public abstract void beforeTest() throws Exception;

	/**
	 * Close connection to the Cluster
	 */
	public abstract void afterTest();

	/**
	 * ATOMIC OPERATIONS DEFINITION
	 */

	public abstract boolean doCreate(int id) throws Exception;

	public abstract boolean doAsyncCreate(int id) throws Exception;

	public abstract boolean doRead(int id) throws Exception;

	public abstract boolean doAsyncRead(int id) throws Exception;

	public abstract boolean doUpdate(int id) throws Exception;

	public abstract boolean doAsyncUpdate(int id) throws Exception;

	public abstract boolean doDelete(int id) throws Exception;

	public abstract boolean doAsyncDelete(int id) throws Exception;

	public abstract String getProductName();

}
