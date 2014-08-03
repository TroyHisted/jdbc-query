package com.jdbcquery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a query that can be executed against a JDBC connection.
 *
 * <p>
 * The named parameter must be terminated by either a space, comma, or a right
 * paren.
 *
 * @author Troy Histed
 *
 * @param <T>
 *            The object type that can be queried for
 */
public class Query<T> {

	private static final NamedStatementParserStrategy STATEMENT_PARSER = new NamedStatementParserStrategy();

	private final String query;
	private final JdbcConnection connection;
	private final PreparedStatement preparedStatement;
	private final RowMapper<T> rowMapper;
	private T defaultWhenNull = null;
	private List<String> parameters;

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
	 * @param aRowMapper
	 *            the row mapping to use
	 */
	public Query(String aQuery, RowMapper<T> aRowMapper) {
		this(aQuery, aRowMapper, (String) null);
	}

	/**
	 * Constructs a query and performs initialization.
	 *
	 * @param aQuery
	 *            the query to be executed
	 * @param aRowMapper
	 *            the row mapping to use
	 * @param aConnection
	 *            an enum representing the connection to use
	 */
	public Query(String aQuery, RowMapper<T> aRowMapper, Enum<?> aConnection) {
		this(aQuery, aRowMapper, aConnection.name());
	}

	/**
	 * Constructs a query and performs initialization.
	 *
	 * @param aQuery
	 *            the query to be executed
	 * @param aRowMapper
	 *            the row mapping to use
	 * @param aConnection
	 *            the name of the connection to use
	 */
	public Query(String aQuery, RowMapper<T> aRowMapper, String aConnectionName) {

		this.query = aQuery;
		final ParsedNamedStatement preparedQuery = Query.STATEMENT_PARSER.prepareNamedStatement(aQuery);
		this.parameters = preparedQuery.getParameters();

		try {
			this.connection = JdbcConnection.connect(aConnectionName);
			this.preparedStatement = this.connection.prepareStatement(preparedQuery.getStatement());
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
	 * @param aConnection the connection name to use
	 * @return the Update
	 */
	public static Update update(String aStatement, String aConnection) {
		return new Update(aStatement, aConnection);
	}

	/**
	 * Static constructor for an Update.
	 *
	 * @param aStatement
	 *            the statement to execute
	 * @param aConnection the connection name to use
	 * @return the Update
	 */
	public static Update update(String aStatement, Enum<?> aConnection) {
		return new Update(aStatement, aConnection);
	}

	/**
	 * Convenience constructor for building a query for an Object.
	 *
	 * @param query
	 *            the query to execute
	 * @param rowMapper
	 *            the row mapper to use
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
	 * Executes the query and maps the result to a list of new instances of the
	 * specified class using the specified row mapper.
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
	 * Sets a string into the prepared statement at the specified parameter
	 * index.
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
	 * Sets a string into the prepared statement using the specified parameter
	 * name.
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
	 * Sets an int into the prepared statement using the specified parameter
	 * name.
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
	 * Sets a long into the prepared statement using the specified parameter
	 * name.
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
	 * Sets a short into the prepared statement at the specified parameter
	 * index.
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
	 * Sets a short into the prepared statement using the specified parameter
	 * name.
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
	 * Sets a float into the prepared statement at the specified parameter
	 * index.
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
	 * Sets a float into the prepared statement using the specified parameter
	 * name.
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
	 * Sets a double into the prepared statement at the specified parameter
	 * index.
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
	 * Sets a double into the prepared statement using the specified parameter
	 * name.
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
	 * Sets a boolean into the prepared statement at the specified parameter
	 * index.
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
	 * Sets a boolean into the prepared statement using the specified parameter
	 * name.
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
	 * @return The update (so that method calls can be chained);
	 */
	public Query<T> set(int index, java.util.Date value) {
		try {
			this.preparedStatement.setTimestamp(index, new Timestamp(value.getTime()));
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
	 * @return The update (so that method calls can be chained);
	 */
	public Query<T> set(String name, java.util.Date value) {
		try {
			for (int i = 0; i < this.parameters.size(); i++) {
				if (this.parameters.get(i).equals(name)) {
					this.preparedStatement.setTimestamp(i + 1, new Timestamp(value.getTime()));
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
