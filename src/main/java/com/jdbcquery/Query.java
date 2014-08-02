package com.jdbcquery;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a query that can be executed against a JDBC connection.
 *
 * <p>
 * The named parameter must be terminated by either a space, comma, or a right paren. 
 *
 * @author Troy Histed
 *
 * @param <T>
 *            The object type that can be queried for
 */
public class Query<T> {

	private final String query;
	private final JdbcConnection connection;
	private final PreparedStatement preparedStatement;
	private final RowMapper<T> rowMapper;
	private T defaultWhenNull = null;
	final List<String> parameters = new ArrayList<String>();

	/**
	 * Package visible constructor
	 */
	Query() {
		this.query = "";
		this.connection = null;
		this.preparedStatement = null;
		this.rowMapper = null;
		// NOP
	}
	
	/**
	 * Constructs a query and performs initialization.
	 *
	 * @param aQuery
	 *            the query to be executed
	 */
	public Query(String aQuery, RowMapper<T> aRowMapper) {

		this.query = aQuery;
		final String preparedQuery = this.prepareNamedStatement(aQuery);

		try {
			this.connection = JdbcConnection.connect();
			this.preparedStatement = this.connection.prepareStatement(preparedQuery);
			this.rowMapper = aRowMapper;
		} catch (final RuntimeException e) {
			this.connection.cleanUp();
			throw e;
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error occured while creating connection to datasource.");
		}
	}

	/**
	 * Convenience constructor for building a query for an Object.
	 *
	 * @param query the query to execute
	 * @param rowMapper the row mapper to use
	 * @return the Query
	 */
	public static <T> Query<T> forObject(String query, RowMapper<T> rowMapper) {
		return new Query<T>(query, rowMapper);
	}

	/**
	 * Convenience constructor for building a query for an Integer.
	 *
	 * @param aQuery
	 *            the query to execute
	 * @return the Query
	 */
	public static Query<Integer> forInteger(String aQuery) {
		return new Query<Integer>(aQuery, RowMapper.INTEGER_MAPPER);
	}

	/**
	 * Convenience constructor for building a query for a Long.
	 *
	 * @param aQuery
	 *            the query to execute
	 * @return the Query
	 */
	public static Query<Long> forLong(String aQuery) {
		return new Query<Long>(aQuery, RowMapper.LONG_MAPPER);
	}

	/**
	 * Convenience constructor for building a query for a String.
	 *
	 * @param aQuery
	 *            the query to execute
	 * @return the Query
	 */
	public static Query<String> forString(String aQuery) {
		return new Query<String>(aQuery, RowMapper.STRING_MAPPER);
	}

	/**
	 * Given a query this will extract the named parameters and replace them with the prepared statement variable
	 * marker "?". The named parameters will be inserted into an instance map where they can be used for
	 * injecting parameters by name.
	 *
	 * @param aQuery
	 *            the query to prepared for use as a named prepared statement
	 * @return the modified query
	 */
	String prepareNamedStatement(String aQuery) {

		final char[] query = aQuery.toCharArray();
		final char[] queryOut = new char[query.length];

		int i = 0;
		int j = 0;
		while (i < query.length) {
			if (query[i] == '/') {
				if (i + 1 < query.length && query[i + 1] == '*') {
					queryOut[j++] = ' ';
					i++; // skip the /
					i++; // skip the *
					while (query[i] != '/' || query[i - 1] != '*') {
						i++; // skip the body of the comment
					}
					i++; // skip the closing /
				} else {
					queryOut[j++] = query[i++]; // add the / since it's not a
												// comment
				}
			} else if (query[i] == '\'' && query[i - 1] != '\\') {
				queryOut[j++] = query[i++]; // Add the '
				while (query[i] != '\'' || query[i - 1] == '\\') {
					queryOut[j++] = query[i++];
				}
				queryOut[j++] = query[i++]; // add the final '
			} else if (query[i] == '\"' && query[i - 1] != '\\') {
				queryOut[j++] = query[i++]; // Add the '
				while (query[i] != '\"' || query[i - 1] == '\\') {
					queryOut[j++] = query[i++];
				}
				queryOut[j++] = query[i++]; // add the final '
			} else if (query[i] == ':') {
				queryOut[j++] = '?'; // replace with a prepared statement marker
				i++;
				int lengthOfParameterName = 0;
				while (i < query.length && query[i] != ' ' && query[i] != ')' && query[i] != ',') {
					lengthOfParameterName++;
					i++;
				}
				final char[] parameterName = new char[lengthOfParameterName];
				for (int index = 1; index <= lengthOfParameterName; index++) {
					parameterName[lengthOfParameterName - index] = query[i - index];
				}

				final String param = String.valueOf(parameterName);
				this.parameters.add(param);
			} else {
				queryOut[j++] = query[i++];
			}
		}

		while (j < queryOut.length) {
			queryOut[j++] = ' ';
		}

		return String.valueOf(queryOut);
	}

	/**
	 * Executes the query.
	 *
	 * @return a mapped object or the defaultWhenNull or null
	 */
	public T execute() {
		T t = null;
		ResultSet resultSet = null;

		try {
			resultSet = this.preparedStatement.executeQuery();
			if (resultSet.next()) {
				t = this.rowMapper.mapRow(resultSet);
			}
		} catch (final SQLException e) {
			throw new DaoException("Error executing : " + this, e);
		} finally {
			this.connection.cleanUp(resultSet);
		}

		if (t == null) {
			return this.defaultWhenNull;
		}
		return t;
	}

	/**
	 * Executes the query and maps the result to a list of new instances of the specified class using the
	 * specified row mapper.
	 *
	 * @return a non-null list containing instances of the specified class.
	 */
	public List<T> executeForAll() {
		final List<T> list = new ArrayList<T>();
		ResultSet resultSet = null;

		try {
			resultSet = this.preparedStatement.executeQuery();

			while (resultSet.next()) {
				list.add(this.rowMapper.mapRow(resultSet));
			}

		} catch (final SQLException e) {
			throw new DaoException("Error executing : " + this, e);
		} finally {
			this.connection.cleanUp(resultSet);
		}

		return list;
	}

	public Query<T> defaultWhenNull(T defaultValue) {
		this.defaultWhenNull = defaultValue;
		return this;
	}

	/**
	 * Sets a string into the prepared statement at the specified parameter index.
	 *
	 * @param value
	 *            the value to set
	 * @return The query (so that method calls can be chained);
	 */
	public Query<T> set(int index, String value) {
		try {
			this.preparedStatement.setString(index, value);
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a string into the prepared statement using the specified parameter name.
	 *
	 * @param name
	 *            the name of the parameter to set
	 * @param value
	 *            the value to set
	 * @return The query (so that method calls can be chained);
	 */
	public Query<T> set(String name, String value) {
		try {
			for (int i = 0; i < this.parameters.size(); i++) {
				if (this.parameters.get(i).equals(name)) {
					this.preparedStatement.setString(i + 1, value);
				}
			}
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * <p>
	 * Sets an int into the prepared statement at the specified parameter index.
	 *
	 * @param value
	 *            the value to set
	 * @return The query (so that method calls can be chained);
	 */
	public Query<T> set(int index, int value) {
		try {
			this.preparedStatement.setInt(index, value);
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * <p>
	 * Sets an int into the prepared statement using the specified parameter name.
	 *
	 * @param name
	 *            the name of the parameter to set
	 * @param value
	 *            the value to set
	 * @return The query (so that method calls can be chained);
	 */
	public Query<T> set(String name, int value) {
		try {
			for (int i = 0; i < this.parameters.size(); i++) {
				if (this.parameters.get(i).equals(name)) {
					this.preparedStatement.setInt(i + 1, value);
				}
			}
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a long into the prepared statement at the specified parameter index.
	 *
	 * @param value
	 *            the value to set
	 * @return The query (so that method calls can be chained);
	 */
	public Query<T> set(int index, long value) {
		try {
			this.preparedStatement.setLong(index, value);
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a long into the prepared statement using the specified parameter name.
	 *
	 * @param name
	 *            the name of the parameter to set
	 * @param value
	 *            the value to set
	 * @return The query (so that method calls can be chained);
	 */
	public Query<T> set(String name, long value) {
		try {
			for (int i = 0; i < this.parameters.size(); i++) {
				if (this.parameters.get(i).equals(name)) {
					this.preparedStatement.setLong(i + 1, value);
				}
			}
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a short into the prepared statement at the specified parameter index.
	 *
	 * @param value
	 *            the value to set
	 * @return The query (so that method calls can be chained);
	 */
	public Query<T> set(int index, short value) {
		try {
			this.preparedStatement.setShort(index, value);
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a short into the prepared statement using the specified parameter name.
	 *
	 * @param name
	 *            the name of the parameter to set
	 * @param value
	 *            the value to set
	 * @return The query (so that method calls can be chained);
	 */
	public Query<T> set(String name, short value) {
		try {
			for (int i = 0; i < this.parameters.size(); i++) {
				if (this.parameters.get(i).equals(name)) {
					this.preparedStatement.setShort(i + 1, value);
				}
			}
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a float into the prepared statement at the specified parameter index.
	 *
	 * @param value
	 *            the value to set
	 * @return The query (so that method calls can be chained);
	 */
	public Query<T> set(int index, float value) {
		try {
			this.preparedStatement.setFloat(index, value);
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a float into the prepared statement using the specified parameter name.
	 *
	 * @param name
	 *            the name of the parameter to set
	 * @param value
	 *            the value to set
	 * @return The query (so that method calls can be chained);
	 */
	public Query<T> set(String name, float value) {
		try {
			for (int i = 0; i < this.parameters.size(); i++) {
				if (this.parameters.get(i).equals(name)) {
					this.preparedStatement.setFloat(i + 1, value);
				}
			}
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a double into the prepared statement at the specified parameter index.
	 *
	 * @param value
	 *            the value to set
	 * @return The query (so that method calls can be chained);
	 */
	public Query<T> set(int index, double value) {
		try {
			this.preparedStatement.setDouble(index, value);
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a double into the prepared statement using the specified parameter name.
	 *
	 * @param name
	 *            the name of the parameter to set
	 * @param value
	 *            the value to set
	 * @return The query (so that method calls can be chained);
	 */
	public Query<T> set(String name, double value) {
		try {
			for (int i = 0; i < this.parameters.size(); i++) {
				if (this.parameters.get(i).equals(name)) {
					this.preparedStatement.setDouble(i + 1, value);
				}
			}
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a boolean into the prepared statement at the specified parameter index.
	 *
	 * @param value
	 *            the value to set
	 * @return The query (so that method calls can be chained);
	 */
	public Query<T> set(int index, boolean value) {
		try {
			this.preparedStatement.setBoolean(index, value);
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a boolean into the prepared statement using the specified parameter name.
	 *
	 * @param name
	 *            the name of the parameter to set
	 * @param value
	 *            the value to set
	 * @return The query (so that method calls can be chained);
	 */
	public Query<T> set(String name, boolean value) {
		try {
			for (int i = 0; i < this.parameters.size(); i++) {
				if (this.parameters.get(i).equals(name)) {
					this.preparedStatement.setBoolean(i + 1, value);
				}
			}
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a Date into the prepared statement at the specified parameter index.
	 *
	 * @param value
	 *            the value to set
	 * @return The query (so that method calls can be chained);
	 */
	public Query<T> set(int index, Date value) {
		try {
			this.preparedStatement.setDate(index, value);
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a Date into the prepared statement using the specified parameter name.
	 *
	 * @param name
	 *            the name of the parameter to set
	 * @param value
	 *            the value to set
	 * @return The query (so that method calls can be chained);
	 */
	public Query<T> set(String name, Date value) {
		try {
			for (int i = 0; i < this.parameters.size(); i++) {
				if (this.parameters.get(i).equals(name)) {
					this.preparedStatement.setDate(i + 1, value);
				}
			}
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets the specified parameter index to be null.
	 *
	 * @param index
	 *            the index of the parameter to set the value on
	 * @return The query (so that method calls can be chained);
	 */
	public Query<T> setNull(int index, int sqlType) {
		try {
			this.preparedStatement.setNull(index, sqlType);
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting null: " + sqlType, e);
		}
		return this;
	}

	/**
	 * Sets the specified parameter name to have a null value.
	 *
	 * @param name
	 *            the name of the parameter to set
	 * @param value
	 *            the value to set
	 * @return The query (so that method calls can be chained);
	 */
	public Query<T> setNull(String name, int sqlType) {
		try {
			for (int i = 0; i < this.parameters.size(); i++) {
				if (this.parameters.get(i).equals(name)) {
					this.preparedStatement.setNull(i + 1, sqlType);
				}
			}
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting null: " + sqlType, e);
		}
		return this;
	}

	/**
	 * @return the preparedStatement
	 */
	public PreparedStatement getPreparedStatement() {
		return this.preparedStatement;
	}

	@Override
	public String toString() {
		return "Query [query=" + this.query + ", connection=" + this.connection + ", preparedStatement="
				+ this.preparedStatement + "]";
	}
}
