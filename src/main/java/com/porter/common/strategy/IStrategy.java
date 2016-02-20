package com.porter.common.strategy;

import java.util.Random;

import com.porter.common.AbstractPerformanceRunner;
import com.porter.common.InputProperties;

/**
 * Simple interface to manage all strategies from Runner
 * 
 * @author fpiagent
 * 
 */
public interface IStrategy {
	public void run(InputProperties props, AbstractPerformanceRunner runner,
			Random rnd);
}
