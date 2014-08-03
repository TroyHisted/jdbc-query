package com.jdbcquery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a select statement that can be executed against a JDBC connection.
 *
 * @author Troy Histed
 *
 * @param <T>
 *            The object type that will be constructed from the result set
 */
public class Select<T> extends Statement {

	private static final NamedStatementParserStrategy STATEMENT_PARSER = new NamedStatementParserStrategy();

	private final String statement;
	private final JdbcConnection connection;
	private final PreparedStatement preparedStatement;
	private final RowMapper<T> rowMapper;
	private T defaultWhenNull = null;
	private List<String> parameters;

	/**
	 * Constructs a select and performs initialization.
	 *
	 * @param aSelect
	 *            the select to be executed
	 * @param aRowMapper
	 *            the row mapping to use
	 */
	public Select(String aSelect, RowMapper<T> aRowMapper) {
		this(aSelect, aRowMapper, (String) null);
	}

	/**
	 * Constructs a select and performs initialization.
	 *
	 * @param aSelect
	 *            the select to be executed
	 * @param aRowMapper
	 *            the row mapping to use
	 * @param aConnectionName
	 *            the name of the connection to use
	 */
	public Select(String aSelect, RowMapper<T> aRowMapper, String aConnectionName) {

		this.statement = aSelect;
		final ParsedNamedStatement preparedSelect = Select.STATEMENT_PARSER.prepareNamedStatement(aSelect);
		this.parameters = preparedSelect.getParameters();

		try {
			this.connection = JdbcConnection.connect(aConnectionName);
			this.preparedStatement = this.connection.prepareStatement(preparedSelect.getStatement());
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
	 * Executes the select.
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
	 * Executes the select and maps the result to a list of new instances of the
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

	/**
	 * Defines a default value that will be returned instead of a null value.
	 *
	 * @param defaultValue the default value
	 * @return the Statement (for method chaining)
	 */
	public Select<T> defaultWhenNull(T defaultValue) {
		this.defaultWhenNull = defaultValue;
		return this;
	}


	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Select<T> set(String aName, String aValue) {
		return (Select<T>) super.set(aName, aValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Select<T> set(String aName, int aValue) {
		return (Select<T>) super.set(aName, aValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Select<T> set(String aName, long aValue) {
		return (Select<T>) super.set(aName, aValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Select<T> set(String aName, short aValue) {
		return (Select<T>) super.set(aName, aValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Select<T> set(String aName, float aValue) {
		return (Select<T>) super.set(aName, aValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Select<T> set(String aName, double aValue) {
		return (Select<T>) super.set(aName, aValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Select<T> set(String aName, boolean aValue) {
		return (Select<T>) super.set(aName, aValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Select<T> set(String aName, java.util.Date aValue) {
		return (Select<T>) super.set(aName, aValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Select<T> setNull(String aName, int aSqlType) {
		return (Select<T>) super.set(aName, aSqlType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PreparedStatement getPreparedStatement() {
		return this.preparedStatement;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<String> getParameters() {
		return this.parameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected JdbcConnection getConnection() {
		return this.connection;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Select [statement=" + this.statement + ", connection=" + this.connection + ", preparedStatement="
				+ this.preparedStatement + ", rowMapper=" + this.rowMapper + ", defaultWhenNull="
				+ this.defaultWhenNull + ", parameters=" + this.parameters + "]";
	}

}
