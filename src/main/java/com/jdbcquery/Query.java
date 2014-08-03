package com.jdbcquery;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utility class for generating SQL statements.
 *
 * @author Troy Histed
 */
public final class Query {

	private static RowMapper<Integer> INTEGER_MAPPER = new RowMapper<Integer>() {
		@Override
		protected Integer mapRow(ResultSet aResultSet) throws SQLException {
			final int value = aResultSet.getInt(1);
			if (aResultSet.wasNull()) {
				return null;
			}
			return Integer.valueOf(value);
		}
	};

	private static RowMapper<Long> LONG_MAPPER = new RowMapper<Long>() {
		@Override
		protected Long mapRow(ResultSet aResultSet) throws SQLException {
			final long value = aResultSet.getLong(1);
			if (aResultSet.wasNull()) {
				return null;
			}
			return Long.valueOf(value);
		}
	};

	private static RowMapper<String> STRING_MAPPER = new RowMapper<String>() {
		@Override
		protected String mapRow(ResultSet aResultSet) throws SQLException {
			return aResultSet.getString(1);
		}
	};

	/**
	 * Static constructor for an Update.
	 *
	 * @param aStatement
	 *            the statement to execute
	 * @return the Update
	 */
	public static Update update(String aStatement) {
		return new Update(aStatement);
	}

	/**
	 * Static constructor for an Update.
	 *
	 * @param aStatement
	 *            the statement to execute
	 * @param aConnectionName
	 *            the connection name to use
	 * @return the Update
	 */
	public static Update update(String aStatement, String aConnectionName) {
		return new Update(aStatement, aConnectionName);
	}

	/**
	 * Static constructor for building a select for an Object.
	 *
	 * @param aStatement
	 *            the select statement to execute
	 * @param aRowMapper
	 *            the row mapping to use
	 * @return the Select
	 */
	public static <T> Select<T> forObject(String aStatement, RowMapper<T> aRowMapper) {
		return new Select<T>(aStatement, aRowMapper);
	}

	/**
	 * Static constructor for building a select for an Object.
	 *
	 * @param aStatement
	 *            the select statement to execute
	 * @param aRowMapper
	 *            the row mapping to use
	 * @param aConnectionName
	 *            the connection name to use
	 * @return the Select
	 */
	public static <T> Select<T> forObject(String aStatement, RowMapper<T> aRowMapper, String aConnectionName) {
		return new Select<T>(aStatement, aRowMapper, aConnectionName);
	}

	/**
	 * Static constructor for building a select for an Integer.
	 *
	 * @param aStatement
	 *            the select statement to execute
	 * @return the Select
	 */
	public static Select<Integer> forInteger(String aStatement) {
		return new Select<Integer>(aStatement, INTEGER_MAPPER);
	}

	/**
	 * Static constructor for building a select for an Integer.
	 *
	 * @param aStatement
	 *            the select statement to execute
	 * @param aConnectionName
	 *            the connection name to use
	 * @return the Select
	 */
	public static Select<Integer> forInteger(String aStatement, String aConnectionName) {
		return new Select<Integer>(aStatement, INTEGER_MAPPER, aConnectionName);
	}

	/**
	 * Static constructor for building a select for a Long.
	 *
	 * @param aStatement
	 *            the select statement to execute
	 * @return the Select
	 */
	public static Select<Long> forLong(String aStatement) {
		return new Select<Long>(aStatement, LONG_MAPPER);
	}

	/**
	 * Static constructor for building a select for a Long.
	 *
	 * @param aStatement
	 *            the select statement to execute
	 * @param aConnectionName
	 *            the connection name to use
	 * @return the Select
	 */
	public static Select<Long> forLong(String aStatement, String aConnectionName) {
		return new Select<Long>(aStatement, LONG_MAPPER, aConnectionName);
	}

	/**
	 * Static constructor for building a select for a String.
	 *
	 * @param aStatement
	 *            the select statement to execute
	 * @return the Select
	 */
	public static Select<String> forString(String aStatement) {
		return new Select<String>(aStatement, STRING_MAPPER);
	}

	/**
	 * Static constructor for building a select for a String.
	 *
	 * @param aStatement
	 *            the select statement to execute
	 * @param aConnectionName
	 *            the connection name to use
	 * @return the Select
	 */
	public static Select<String> forString(String aStatement, String aConnectionName) {
		return new Select<String>(aStatement, STRING_MAPPER, aConnectionName);
	}
}
