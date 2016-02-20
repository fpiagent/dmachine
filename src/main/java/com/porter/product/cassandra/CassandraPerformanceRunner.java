package com.porter.product.cassandra;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.porter.common.AbstractPerformanceRunner;

/**
 * Cassandra is a NoSQL DB, even if DMachine is focused on Caching
 * solutions the time of this commit and I already have some insight on the application.
 * Facebook Use Case discusses using thousands of server nodes with even more
 * clients which speaks of good scalability capabilities.
 * 
 * @author fpiagent
 * 
 */
public class CassandraPerformanceRunner extends AbstractPerformanceRunner {

	private PreparedStatement insertStatement;
	private PreparedStatement selectStatement;
	private PreparedStatement updateStatement;
	private PreparedStatement deleteStatement;

	private Session session;
	private Cluster cluster;

	public static void main(String[] args) {
		startTest(new CassandraPerformanceRunner(), args);
	}

	@Override
	public void beforeTest() throws Exception {
		// Product Specific Steps
		cluster = Cluster.builder().withPort(9042)
				.addContactPoint("sample.hostname.com")
				.addContactPoint("sample.hostname2.com")
				.addContactPoint("sample.hostname3.com").build();
		session = cluster.connect();
		// RUN ONLY ONCE
		// session.execute("CREATE KEYSPACE IF NOT EXISTS test WITH replication "
		// + "= {'class':'SimpleStrategy', 'replication_factor':3};");

		// session.execute("DROP TABLE IF EXISTS test.orders;");

		// session.execute("CREATE TABLE IF NOT EXISTS test.orders ("
		// + "id text PRIMARY KEY, data text);");

		// Inserting a value
		insertStatement = session.prepare("INSERT INTO test.orders "
				+ "(id, data) VALUES (?, ?);");

		selectStatement = session
				.prepare("SELECT * from test.orders WHERE id = ?");

		updateStatement = session
				.prepare("UPDATE test.orders SET data = ? WHERE id = ?;");

		deleteStatement = session
				.prepare("DELETE FROM test.orders WHERE id = ?;");
	}

	@Override
	public void afterTest() {
		cluster.closeAsync();
	}

	@Override
	public String getProductName() {
		return "CASSANDRA";
	}

	@Override
	public boolean doCreate(int id) {
		BoundStatement boundCStatement = insertStatement.bind(
				String.valueOf(id), super.getSerializedObject());
		ResultSet result = session.execute(boundCStatement);
		return result != null;
	}

	@Override
	public boolean doRead(int id) {
		BoundStatement boundRStatement = selectStatement.bind(String
				.valueOf(id));
		ResultSet result = session.execute(boundRStatement);
		// MUST BRING ONE AND ONE ONLY RESULT
		return (result.one() != null && result.isExhausted() == true);
	}

	@Override
	public boolean doUpdate(int id) {
		BoundStatement boundUStatement = updateStatement.bind(
				super.getSerializedObject(), String.valueOf(id));
		ResultSet result = session.execute(boundUStatement);
		return result != null;
	}

	@Override
	public boolean doDelete(int id) {
		BoundStatement boundDStatement = deleteStatement.bind(String
				.valueOf(id));
		ResultSet result = session.execute(boundDStatement);
		return result != null;
	}

	@Override
	public boolean doAsyncCreate(int id) {
		BoundStatement boundCStatement = insertStatement.bind(
				String.valueOf(id), super.getSerializedObject());
		session.executeAsync(boundCStatement);
		return true;
	}

	@Override
	public boolean doAsyncRead(int id) {
		BoundStatement boundRStatement = selectStatement.bind(String
				.valueOf(id));
		session.executeAsync(boundRStatement);
		return true;
	}

	@Override
	public boolean doAsyncUpdate(int id) {
		BoundStatement boundUStatement = updateStatement.bind(
				super.getSerializedObject(), String.valueOf(id));
		session.executeAsync(boundUStatement);
		return true;
	}

	@Override
	public boolean doAsyncDelete(int id) {
		BoundStatement boundDStatement = deleteStatement.bind(String
				.valueOf(id));
		session.executeAsync(boundDStatement);
		return true;
	}

}
