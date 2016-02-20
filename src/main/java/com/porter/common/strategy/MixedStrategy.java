package com.porter.common.strategy;

import java.util.Random;

import org.apache.commons.lang.NotImplementedException;

import com.porter.common.AbstractPerformanceRunner;
import com.porter.common.InputProperties;

/**
 * NOT IMPLEMENTED
 * 
 * MixedStrategy mixes Actions and Time strategy, whichever condition is met
 * first, will be used as control cut for the entire execution.
 * 
 * @author fpiagent
 * 
 */
public class MixedStrategy implements IStrategy {

	public void run(InputProperties props, AbstractPerformanceRunner runner,
			Random rnd) {
		throw new NotImplementedException("MIXED Strategy not implemented");
	}

}
