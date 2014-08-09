package org.jdbcquery;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the Mock ResultSet.
 *
 * @author Troy Histed
 */
public class MockResultSetTest {

	MockResultSet resultSet;

	/**
	 * Resets the instance members to new instances.
	 */
	@Before
	public void constructStatement() {
		this.resultSet = new MockResultSet();
	}

	/**
	 *
	 * @throws SQLException
	 */
	@Test
	public void testEmptyResultSet() throws SQLException {
		Assert.assertFalse(this.resultSet.next());
		Assert.assertTrue(this.resultSet.isAfterLast());
	}

	/**
	 *
	 * @throws SQLException
	 */
	@Test
	public void testOneResult() throws SQLException {
		this.resultSet.getValues().add(new LinkedHashMap<String, Object>());
		Assert.assertTrue(this.resultSet.next());
		Assert.assertFalse(this.resultSet.isAfterLast());
	}

	/**
	 *
	 * @throws SQLException
	 */
	@Test(expected=SQLException.class)
	public void testPastOneResult() throws SQLException {
		this.resultSet.getValues().add(new LinkedHashMap<String, Object>());
		Assert.assertTrue(this.resultSet.next());
		Assert.assertFalse(this.resultSet.next());
		this.resultSet.next();
	}

	/**
	 *
	 * @throws SQLException
	 */
	@Test
	public void testOpen() throws SQLException {
		Assert.assertFalse(this.resultSet.isClosed());
	}

	/**
	 *
	 * @throws SQLException
	 */
	@Test
	public void testClosed() throws SQLException {
		this.resultSet.close();
		Assert.assertTrue(this.resultSet.isClosed());
	}

	/**
	 *
	 * @throws SQLException
	 */
	@Test
	public void testRow() throws SQLException {
		this.resultSet.getValues().add(new LinkedHashMap<String, Object>());
		Assert.assertEquals(0, this.resultSet.getRow());
		this.resultSet.next();
		Assert.assertEquals(1, this.resultSet.getRow());
		this.resultSet.next();
		Assert.assertEquals(2, this.resultSet.getRow());
	}


	/**
	 *
	 * @throws SQLException
	 */
	@Test
	public void testGetString() throws SQLException {
		final LinkedHashMap<String, Object> row = new LinkedHashMap<String, Object>();
		row.put("col1", "test");
		this.resultSet.getValues().add(row);

		this.resultSet.next();
		Assert.assertEquals("test", this.resultSet.getString("col1"));
	}

	/**
	 *
	 * @throws SQLException
	 */
	@Test
	public void testWasNull() throws SQLException {
		final LinkedHashMap<String, Object> row = new LinkedHashMap<String, Object>();
		row.put("col1", null);
		this.resultSet.getValues().add(row);

		this.resultSet.next();
		Assert.assertNull(this.resultSet.getString("col1"));
	}
}
