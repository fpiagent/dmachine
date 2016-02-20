package com.porter.common;

import org.apache.commons.lang.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.porter.common.AbstractPerformanceRunner;

public class AbstractPerformanceRunnerTest {

	@Test
	public void testDefaultDirectRun() throws Exception {
		MockPerformanceRunner runner = Mockito.spy(new MockPerformanceRunner());
		AbstractPerformanceRunner.startTest(runner, null);

		Assert.assertNotNull(MockPerformanceRunner.props);

		// Check Properties File
		Assert.assertEquals("json",
				MockPerformanceRunner.props.getSerialization());
		Assert.assertEquals("sample.hostname.com", MockPerformanceRunner.props.getHost());
		Assert.assertEquals(new Integer(1000),
				MockPerformanceRunner.props.getActions());
		Assert.assertEquals("direct", MockPerformanceRunner.props.getStrategy());
		Assert.assertEquals(new Integer(50), MockPerformanceRunner.props.getC());

		// Check CRUD was called ASYNC in Direct Strategy
		Mockito.verify(runner,
				Mockito.times(MockPerformanceRunner.props.getC()))
				.doAsyncCreate(Mockito.anyInt());
		Mockito.verify(runner,
				Mockito.times(MockPerformanceRunner.props.getR())).doAsyncRead(
				Mockito.anyInt());
		Mockito.verify(runner,
				Mockito.times(MockPerformanceRunner.props.getU()))
				.doAsyncUpdate(Mockito.anyInt());
		Mockito.verify(runner,
				Mockito.times(MockPerformanceRunner.props.getD()))
				.doAsyncDelete(Mockito.anyInt());

		// Validates No SYNC action has been run
		Mockito.verify(
				runner,
				Mockito.times(MockPerformanceRunner.props.getIdTo()
						- MockPerformanceRunner.props.getIdFrom())).doCreate(
				Mockito.anyInt());
		Mockito.verify(runner, Mockito.times(0)).doRead(Mockito.anyInt());
		Mockito.verify(runner, Mockito.times(0)).doUpdate(Mockito.anyInt());
		Mockito.verify(
				runner,
				Mockito.times(MockPerformanceRunner.props.getIdTo()
						- MockPerformanceRunner.props.getIdFrom())).doDelete(
				Mockito.anyInt());
	}

	@Test
	public void testActionsRun() throws Exception {
		String propsPath = AbstractPerformanceRunnerTest.class
				.getResource("/crudTest-actions.properties").getPath()
				.toString();
		String[] inputArgs = { propsPath };

		MockPerformanceRunner runner = Mockito.spy(new MockPerformanceRunner());
		AbstractPerformanceRunner.startTest(runner, inputArgs);

		Assert.assertNotNull(MockPerformanceRunner.props);

		// Check Properties File
		Assert.assertEquals("kryo",
				MockPerformanceRunner.props.getSerialization());
		Assert.assertEquals("localhost", MockPerformanceRunner.props.getHost());
		Assert.assertEquals(new Integer(120),
				MockPerformanceRunner.props.getActions());
		Assert.assertEquals("actions",
				MockPerformanceRunner.props.getStrategy());
		Assert.assertEquals(new Integer(1), MockPerformanceRunner.props.getC());

		// Check CRUD was called SYNC in Actions Strategy

		// Create Property TIMES Actions PLUS Initial Create
		Mockito.verify(
				runner,
				Mockito.times(MockPerformanceRunner.props.getC()
						* MockPerformanceRunner.props.getActions()
						+ (MockPerformanceRunner.props.getIdTo() - MockPerformanceRunner.props
								.getIdFrom()))).doCreate(Mockito.anyInt());
		Mockito.verify(
				runner,
				Mockito.times(MockPerformanceRunner.props.getR()
						* MockPerformanceRunner.props.getActions())).doRead(
				Mockito.anyInt());
		Mockito.verify(
				runner,
				Mockito.times(MockPerformanceRunner.props.getU()
						* MockPerformanceRunner.props.getActions())).doUpdate(
				Mockito.anyInt());

		// Delete Property TIMES Actions PLUS Final Delete
		Mockito.verify(
				runner,
				Mockito.times(MockPerformanceRunner.props.getD()
						* MockPerformanceRunner.props.getActions()
						+ (MockPerformanceRunner.props.getIdTo() - MockPerformanceRunner.props
								.getIdFrom()))).doDelete(Mockito.anyInt());

		// Validates No ASYNC action has been run
		Mockito.verify(runner, Mockito.times(0))
				.doAsyncCreate(Mockito.anyInt());
		Mockito.verify(runner, Mockito.times(0)).doAsyncRead(Mockito.anyInt());
		Mockito.verify(runner, Mockito.times(0))
				.doAsyncUpdate(Mockito.anyInt());
		Mockito.verify(runner, Mockito.times(0))
				.doAsyncDelete(Mockito.anyInt());

		// Test It is Deterministic: ID Random generation and Action Random
		// Selection should always return the same
		InOrder order = Mockito.inOrder(runner);
		order.verify(runner).doRead(7);
		order.verify(runner).doDelete(0);
		order.verify(runner).doRead(1);
		order.verify(runner).doUpdate(14);
	}

	@Test
	public void testTimeRun() throws Exception {
		String propsPath = AbstractPerformanceRunnerTest.class
				.getResource("/crudTest-time.properties").getPath().toString();
		String[] inputArgs = { propsPath };

		StopWatch clock = new StopWatch();
		clock.start();

		MockPerformanceRunner runner = Mockito.spy(new MockPerformanceRunner());
		AbstractPerformanceRunner.startTest(runner, inputArgs);

		clock.stop();

		// BEGIN_DELAY is zero, validate the Test took at least the configured 2
		// Seconds
		Assert.assertTrue(clock.getTime() > 2000);
		// Validate it is still under 3 seconds
		Assert.assertTrue(clock.getTime() < 3000);

		// Check Properties File
		Assert.assertEquals("boon",
				MockPerformanceRunner.props.getSerialization());
		Assert.assertEquals("localhost", MockPerformanceRunner.props.getHost());
		Assert.assertEquals(new Integer(120),
				MockPerformanceRunner.props.getActions());
		Assert.assertEquals("time", MockPerformanceRunner.props.getStrategy());
		Assert.assertEquals(new Integer(2), MockPerformanceRunner.props.getC());

		// Validates No SYNC action has been run
		Mockito.verify(
				runner,
				Mockito.times(MockPerformanceRunner.props.getIdTo()
						- MockPerformanceRunner.props.getIdFrom())).doCreate(
				Mockito.anyInt());
		Mockito.verify(runner, Mockito.times(0)).doRead(Mockito.anyInt());
		Mockito.verify(runner, Mockito.times(0)).doUpdate(Mockito.anyInt());
		Mockito.verify(
				runner,
				Mockito.times(MockPerformanceRunner.props.getIdTo()
						- MockPerformanceRunner.props.getIdFrom())).doDelete(
				Mockito.anyInt());
	}
}
