package com.porter.product.aerospike;

import com.porter.common.AbstractPerformanceRunner;

/**
 * NOT IMPLEMENTED
 * 
 * Aerospike is only an Enterprise Edition, thus not considered for this test.
 * But according to documentation and support, they have a couple of features
 * that win over Couchbase such as the possibility to disable disk persistence.
 * 
 * @author fpiagent
 * 
 */
public class AerospikePerformanceRunner extends AbstractPerformanceRunner {

	@Override
	public void beforeTest() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTest() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean doCreate(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doAsyncCreate(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doRead(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doAsyncRead(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doUpdate(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doAsyncUpdate(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doDelete(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doAsyncDelete(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getProductName() {
		// TODO Auto-generated method stub
		return null;
	}

}
