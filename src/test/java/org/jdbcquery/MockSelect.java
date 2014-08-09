package org.jdbcquery;

import java.sql.Connection;

/**
 *
 * @author Troy
 *
 * @param <T>
 */
public class MockSelect<T> extends Select<T> {

	/**
	 *
	 * @param aSelect
	 * @param aRowMapper
	 * @param aConnection
	 */
	public MockSelect(String aSelect, RowMapper<T> aRowMapper, Connection aConnection) {
		super(aSelect, aRowMapper, aConnection);
	}

}
