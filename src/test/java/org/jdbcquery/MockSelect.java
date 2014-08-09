package org.jdbcquery;

import java.sql.Connection;

/**
 * Mock select statement.
 *
 * @author Troy Histed
 *
 * @param <T> the Object the select will return
 */
public class MockSelect<T> extends Select<T> {

	/**
	 * Constructs a mock select statement.
	 *
	 * @param aSelect the select statement
	 * @param aRowMapper the row mapper to use
	 * @param aConnection the connection to use
	 */
	public MockSelect(String aSelect, RowMapper<T> aRowMapper, Connection aConnection) {
		super(aSelect, aRowMapper, aConnection);
	}

}
