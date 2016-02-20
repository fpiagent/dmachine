package com.porter.common.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

import com.porter.common.AbstractPerformanceRunner;
import com.porter.common.CRUDEnum;
import com.porter.common.InputProperties;

/**
 * Executes actions (formed by the CRUD values) until the TIME in seconds is
 * met. Returns stats on how many actions and operations have been executed
 * during that time.
 * 
 * @author fpiagent
 * 
 */
public class TimeStrategy implements IStrategy {

	private Logger log = Logger.getLogger(TimeStrategy.class.getName());

	public void run(InputProperties props, AbstractPerformanceRunner runner,
			Random rnd) {

		List<CRUDEnum> actionsTemplate = new ArrayList<CRUDEnum>();
		for (int i = 0; i < props.getC(); i++) {
			actionsTemplate.add(CRUDEnum.C);
		}
		for (int i = 0; i < props.getR(); i++) {
			actionsTemplate.add(CRUDEnum.R);
		}
		for (int i = 0; i < props.getU(); i++) {
			actionsTemplate.add(CRUDEnum.U);
		}
		for (int i = 0; i < props.getD(); i++) {
			actionsTemplate.add(CRUDEnum.D);
		}

		Collections.shuffle(actionsTemplate);

		/**
		 * Run The actionsTemplate a number of ACTIONS times
		 */
		int Crun = 0;
		int Rrun = 0;
		int Urun = 0;
		int Drun = 0;

		StopWatch cWatch = new StopWatch();
		cWatch.start();
		cWatch.suspend();

		StopWatch rWatch = new StopWatch();
		rWatch.start();
		rWatch.suspend();

		StopWatch uWatch = new StopWatch();
		uWatch.start();
		uWatch.suspend();

		StopWatch dWatch = new StopWatch();
		dWatch.start();
		dWatch.suspend();

		long startMilisecs = new Date().getTime();
		long currentMilisecs = new Date().getTime();
		int actionsCount = 0;

		while ((currentMilisecs - startMilisecs) / 1000 < props.getRunSeconds()) {
			for (int j = 0; j < actionsTemplate.size(); j++) {

				int id = rnd.nextInt(props.getIdTo() - props.getIdFrom());
				id += props.getIdFrom();

				switch (actionsTemplate.get(j)) {
				case C:
					Crun = runner.doCreateAction(Crun, id, cWatch);
					break;
				case R:
					Rrun = runner.doReadAction(Rrun, id, rWatch);
					break;
				case U:
					Urun = runner.doUpdateAction(Urun, id, uWatch);
					break;
				case D:
					Drun = runner.doDeleteAction(Drun, id, dWatch);
					break;
				}
			}
			actionsCount++;
			currentMilisecs = new Date().getTime();
		}

		log.info("=>TEST FINISHED: RESULTS");
		log.info("========================");
		int totalSuccess = Crun + Rrun + Urun + Drun;
		int totalRun = props.getC() * actionsCount + props.getR()
				* actionsCount + props.getU() * actionsCount + props.getD()
				* actionsCount;

		log.info("=> STRATEGY: TIME");
		log.info("=> Seconds Test: " + props.getRunSeconds());
		log.info("=> Actions Run: " + actionsCount);
		log.info("=> Hit Rate:" + ((totalSuccess * 100) / totalRun)
				+ " % (Doesn't Define Success)");

		log.info("=> Create RUN / Requested: " + Crun + " / " + props.getC()
				* actionsCount);
		log.info("=> Read RUN / Requested: " + Rrun + " / " + props.getR()
				* actionsCount);
		log.info("=> Update RUN / Requested: " + Urun + " / " + props.getU()
				* actionsCount);
		log.info("=> Delete RUN / Requested: " + Drun + " / " + props.getD()
				* actionsCount);

		log.info("=> Create Milisecs Taken:" + cWatch.getTime());
		log.info("=> Read Milisecs Taken:" + rWatch.getTime());
		log.info("=> Update Milisecs Taken:" + uWatch.getTime());
		log.info("=> Delete Milisecs Taken:" + dWatch.getTime());
	}

}
