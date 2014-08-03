package com.jdbcquery;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles converting a single row from a result set into an object of type T.
 *
 * @author Troy Histed
 *
 * @param <T> The object type that will be created with each row
 */
public abstract class RowMapper<T> {

	/**
	 * Maps a single result set record to an instance of the query type.
	 *
	 * @param aResultSet
	 *            the result set record to process
	 * @return the mapped row
	 * @throws SQLException
	 *             the sql exception
	 */
	protected abstract T mapRow(ResultSet aResultSet) throws SQLException;

}
