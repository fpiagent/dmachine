package com.porter.common;

import com.porter.common.AbstractPerformanceRunner;

public class MockPerformanceRunner extends AbstractPerformanceRunner {

	@Override
	public void beforeTest() throws Exception {
	}

	@Override
	public void afterTest() {
	}

	@Override
	public boolean doCreate(int id) throws Exception {
		return true;
	}

	@Override
	public boolean doAsyncCreate(int id) throws Exception {
		return true;
	}

	@Override
	public boolean doRead(int id) throws Exception {
		return true;
	}

	@Override
	public boolean doAsyncRead(int id) throws Exception {
		return true;
	}

	@Override
	public boolean doUpdate(int id) throws Exception {
		return true;
	}

	@Override
	public boolean doAsyncUpdate(int id) throws Exception {
		return true;
	}

	@Override
	public boolean doDelete(int id) throws Exception {
		return true;
	}

	@Override
	public boolean doAsyncDelete(int id) throws Exception {
		return true;
	}

	@Override
	public String getProductName() {
		return "MockRunner";
	}

}
