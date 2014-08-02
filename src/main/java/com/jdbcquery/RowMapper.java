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

	public static RowMapper<Integer> INTEGER_MAPPER = new RowMapper<Integer>(){
		@Override
		protected Integer mapRow(ResultSet aResultSet) throws SQLException {
			final int value = aResultSet.getInt(1);
			if (aResultSet.wasNull()) {
				return null;
			}
			return Integer.valueOf(value);
		}
	};

	public static RowMapper<Long> LONG_MAPPER = new RowMapper<Long>(){
		@Override
		protected Long mapRow(ResultSet aResultSet) throws SQLException {
			final long value = aResultSet.getLong(1);
			if (aResultSet.wasNull()) {
				return null;
			}
			return Long.valueOf(value);
		}
	};

	public static RowMapper<String> STRING_MAPPER = new RowMapper<String>(){
		@Override
		protected String mapRow(ResultSet aResultSet) throws SQLException {
			return aResultSet.getString(1);
		}
	};
}
