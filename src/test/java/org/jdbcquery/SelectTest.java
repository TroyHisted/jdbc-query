package org.jdbcquery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the Select Statement Object.
 *
 * @author Troy Histed
 */
public class SelectTest {

	MockSelect<String> select;
	MockConnection connection;
	MockResultSet resultSet;
	RowMapper<String> rowMapper;

	/**
	 * Resets the instance members to new instances.
	 */
	@Before
	public void constructStatement() {
		this.connection = new MockConnection();
		this.rowMapper = new RowMapper<String>() {
			@Override
			protected String mapRow(ResultSet aResultSet) throws SQLException {
				return aResultSet.getString("col1");
			}};

		this.select = new MockSelect<String>(
				"Select 'test' from table where something = :param1", this.rowMapper, this.connection);

		this.resultSet = new MockResultSet();
		final LinkedHashMap<String, Object> row = new LinkedHashMap<String, Object>();
		row.put("col1", "test");
		this.resultSet.getValues().add(row);
		this.connection.getPreparedStatement().setResultSet(this.resultSet);
	}

	/**
	 *
	 */
	@Test
	public void test() {
		Assert.assertNotNull(this.select);
		Assert.assertNotNull(this.connection);
	}

	/**
	 * @throws SQLException
	 *
	 */
	@Test
	public void testExecute() throws SQLException {
		final String value = this.select.execute();
		Assert.assertEquals("test", value);
		Assert.assertTrue(this.connection.isClosed());
		Assert.assertTrue(this.connection.getPreparedStatement().isClosed());
		Assert.assertTrue(this.connection.getPreparedStatement().getResultSet().isClosed());
	}

	/**
	 * @throws SQLException
	 *
	 */
	@Test
	public void testExecuteAll() throws SQLException {
		final List<String> values = this.select.executeForAll();
		Assert.assertEquals("test", values.get(0));
		Assert.assertTrue(this.connection.isClosed());
		Assert.assertTrue(this.connection.getPreparedStatement().isClosed());
		Assert.assertTrue(this.connection.getPreparedStatement().getResultSet().isClosed());
	}

	/**
	 * @throws SQLException
	 *
	 */
	@SuppressWarnings("resource")
	@Test(expected=DaoException.class)
	public void testSQLExceptionPreparingStatement() throws SQLException {

		final MockConnection badConnection = new MockConnection() {
			@Override
			public PreparedStatement prepareStatement(String sql) throws SQLException {
				throw new SQLException();
			}
		};

		this.select = new MockSelect<String>("Select...", this.rowMapper, badConnection);


		Assert.assertTrue(this.connection.isClosed());
		Assert.assertTrue(this.connection.getPreparedStatement().isClosed());
		Assert.assertTrue(this.connection.getPreparedStatement().getResultSet().isClosed());
	}

	/**
	 * @throws SQLException
	 *
	 */
	@SuppressWarnings("resource")
	@Test(expected=NullPointerException.class)
	public void testNPExceptionPreparingStatement() throws SQLException {

		final MockConnection badConnection = new MockConnection() {
			@Override
			public PreparedStatement prepareStatement(String sql) throws SQLException {
				throw new NullPointerException();
			}
		};

		this.select = new MockSelect<String>("Select...", this.rowMapper, badConnection);

		Assert.assertTrue(this.connection.isClosed());
		Assert.assertTrue(this.connection.getPreparedStatement().isClosed());
		Assert.assertTrue(this.connection.getPreparedStatement().getResultSet().isClosed());
	}

	/**
	 * @throws SQLException
	 *
	 */
	@Test(expected=NullPointerException.class)
	public void testExceptionSettingValue() throws SQLException {


		this.select = new MockSelect<String>("Select...", this.rowMapper, this.connection) {
			@Override
			public Select<String> set(String aName, String aValue) {
				throw new NullPointerException();
			}
		};

		this.select.set("thing", "bad");

		Assert.assertTrue(this.connection.isClosed());
		Assert.assertTrue(this.connection.getPreparedStatement().isClosed());
		Assert.assertTrue(this.connection.getPreparedStatement().getResultSet().isClosed());
	}

}
