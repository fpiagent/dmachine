package com.porter.common.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

import com.porter.common.AbstractPerformanceRunner;
import com.porter.common.CRUDEnum;
import com.porter.common.InputProperties;

/**
 * Use the Actions Strategy to run a number of actions and then stop execution.
 * One action is defined as a composition of CRUD actions determined at the
 * beginning of the execution so having 3 actions and a CRUD of 10 operations,
 * we'll get 30 operations executed (3 times the same set) and then the
 * execution stops.
 * 
 * @author fpiagent
 * 
 */
public class ActionsStrategy implements IStrategy {

	private Logger log = Logger.getLogger(ActionsStrategy.class.getName());

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

		for (int i = 0; i < props.getActions(); i++) {
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
		}

		log.info("=>TEST FINISHED: RESULTS");
		log.info("========================");
		int totalSuccess = Crun + Rrun + Urun + Drun;
		int totalRun = props.getC() * props.getActions() + props.getR()
				* props.getActions() + props.getU() * props.getActions()
				+ props.getD() * props.getActions();
		log.info("=> STRATEGY: ACTIONS");
		log.info("=> CRUD Template Run Count: " + props.getActions());
		log.info("=> Hit Rate:" + ((totalSuccess * 100) / totalRun)
				+ " % (Doesn't Define Success)");

		log.info("=> Create RUN / Requested: " + Crun + " / " + props.getC()
				* props.getActions());
		log.info("=> Read RUN / Requested: " + Rrun + " / " + props.getR()
				* props.getActions());
		log.info("=> Update RUN / Requested: " + Urun + " / " + props.getU()
				* props.getActions());
		log.info("=> Delete RUN / Requested: " + Drun + " / " + props.getD()
				* props.getActions());

		log.info("=> Create Milisecs Taken:" + cWatch.getTime());
		log.info("=> Read Milisecs Taken:" + rWatch.getTime());
		log.info("=> Update Milisecs Taken:" + uWatch.getTime());
		log.info("=> Delete Milisecs Taken:" + dWatch.getTime());

	}
}
