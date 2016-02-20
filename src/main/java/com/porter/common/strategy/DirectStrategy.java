package com.porter.common.strategy;

import java.util.Random;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

import com.porter.common.AbstractPerformanceRunner;
import com.porter.common.InputProperties;

/**
 * Direct Strategy will execute exactly the number of CRUD Operations stated in
 * the config file. Random order of them defined upon beginning of execution
 * using RANDOM_SEED configurations.
 * 
 * @author fpiagent
 * 
 */
public class DirectStrategy implements IStrategy {

	private Logger log = Logger.getLogger(DirectStrategy.class.getName());

	private static Random actionRnd;

	public void run(InputProperties props, AbstractPerformanceRunner runner,
			Random rnd) {

		/**
		 * SET DETERMINISTIC / NON DETERMINISTIC
		 */
		if (props.isDeterministic()) {
			actionRnd = new Random(props.getActionRndSeed());
		} else {
			actionRnd = new Random();
		}

		int Ccount = 0;
		int Rcount = 0;
		int Ucount = 0;
		int Dcount = 0;

		int Crun = 0;
		int Rrun = 0;
		int Urun = 0;
		int Drun = 0;

		StopWatch cWatch = new StopWatch();
		StopWatch rWatch = new StopWatch();
		StopWatch uWatch = new StopWatch();
		StopWatch dWatch = new StopWatch();
		cWatch.start();
		cWatch.suspend();
		rWatch.start();
		rWatch.suspend();
		uWatch.start();
		uWatch.suspend();
		dWatch.start();
		dWatch.suspend();

		// CHOOSE RANDOM ID
		// CHOOSE RANDOM ACTION TO APPLY
		while (Ccount < props.getC() || Rcount < props.getR()
				|| Ucount < props.getU() || Dcount < props.getD()) {

			int id;
			if (props.isDeterministic()) {
				id = rnd.nextInt(props.getIdTo() - props.getIdFrom());
				id += props.getIdFrom();
			} else {
				id = rnd.nextInt(props.getC());
			}

			switch (actionRnd.nextInt(4)) {
			case 0: // CREATE
				if (Ccount >= props.getC())
					continue;

				Ccount++;
				Crun = runner.doCreateAction(Crun, id, cWatch);

				break;
			case 1: // READ
				if (Rcount >= props.getR())
					continue;

				Rcount++;
				Rrun = runner.doReadAction(Rrun, id, rWatch);

				break;
			case 2: // UPDATE
				if (Ucount >= props.getU())
					continue;

				Ucount++;
				Urun = runner.doUpdateAction(Urun, id, uWatch);

				break;
			case 3: // DELETE
				if (Dcount >= props.getD())
					continue;

				Dcount++;
				Drun = runner.doDeleteAction(Drun, id, dWatch);

				break;
			}

		}

		cWatch.stop();
		rWatch.stop();
		uWatch.stop();
		dWatch.stop();

		log.info("=>TEST FINISHED: RESULTS");
		log.info("========================");
		int totalSuccess = Crun + Rrun + Urun + Drun;
		int totalRun = props.getC() + props.getR() + props.getU()
				+ props.getD();

		log.info("=> STRATEGY: DIRECT");
		log.info("=> CRUD Total Actions Count: " + totalRun);

		log.info("=> Hit Rate:" + ((totalSuccess * 100) / totalRun)
				+ " % (Doesn't Define Success)");

		log.info("=> Create RUN / Requested: " + Crun + " / " + props.getC());
		log.info("=> Read RUN / Requested: " + Rrun + " / " + props.getR());
		log.info("=> Update RUN / Requested: " + Urun + " / " + props.getU());
		log.info("=> Delete RUN / Requested: " + Drun + " / " + props.getD());

		log.info("=> Create Milisecs Taken:" + cWatch.getTime());
		log.info("=> Read Milisecs Taken:" + rWatch.getTime());
		log.info("=> Update Milisecs Taken:" + uWatch.getTime());
		log.info("=> Delete Milisecs Taken:" + dWatch.getTime());

	}

}
