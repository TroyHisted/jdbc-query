package com.jdbcquery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Represents an SQL update statement.
 *
 * @author Troy Histed
 */
public class Update extends Statement {

	private static final NamedStatementParserStrategy STATEMENT_PARSER = new NamedStatementParserStrategy();

	private final String statement;
	private final JdbcConnection connection;
	private final PreparedStatement preparedStatement;
	private final List<String> parameters;

	/**
	 * Constructs an update statement and performs initialization.
	 *
	 * @param aStatement
	 *            the statement to be executed
	 */
	public Update(String aStatement) {
		this(aStatement, null);
	}

	/**
	 * Constructs an update statement and performs initialization.
	 *
	 * @param aStatement
	 *            the statement to be executed
	 * @param aConnection
	 *            the name of the connection to use
	 */
	public Update(String aStatement, String aConnection) {

		this.statement = aStatement;
		final ParsedNamedStatement preparedStatement = Update.STATEMENT_PARSER.prepareNamedStatement(aStatement);
		this.parameters = preparedStatement.getParameters();

		try {
			this.connection = JdbcConnection.connect(aConnection);
			this.preparedStatement = this.connection.prepareStatementWithGeneratedKeys(
					preparedStatement.getStatement());
		} catch (final RuntimeException e) {
			if (this.connection != null) {
				this.connection.cleanUp();
			}
			throw e;
		} catch (final SQLException e) {
			if (this.connection != null) {
				this.connection.cleanUp();
			}
			throw new DaoException("Error creating connection and preparing statement: " + aStatement, e);
		}
	}

	/**
	 * Executes the statement.
	 *
	 * @return the auto-generated key or, if no keys were generated, the number of records updated
	 */
	public int execute() {
		ResultSet resultSet = null;
		try {
			final int updateCount = this.preparedStatement.executeUpdate();
			resultSet = this.preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
			return updateCount;
		} catch (final SQLException e) {
			throw new DaoException("Error executing : " + this, e);
		} finally {
			this.connection.cleanUp(resultSet);
		}
	}

	/**
	 * Adds a set of parameters to this objects batch of commands.
	 */
	public void addBatch() {
		try {
			this.preparedStatement.addBatch();
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error adding batch: " + this, e);
		}
	}

	/**
	 * Executes the batch statements that have been added to this object.
	 *
	 * @return array containing the number of records updated for each batch statement
	 */
	public int[] executeBatch() {
		ResultSet resultSet = null;
		try {
			final int[] updateCount = this.preparedStatement.executeBatch();

			resultSet = this.preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				final int [] generatedKeys = new int[updateCount.length];
				int i = 0;
				generatedKeys[i] = resultSet.getInt(1);
				i++;
				while(resultSet.next()) {
					generatedKeys[i] = resultSet.getInt(1);
					i++;
				}
				return generatedKeys;
			}
			return updateCount;
		} catch (final SQLException e) {
			throw new DaoException("Error executing batch: " + this, e);
		} finally {
			this.connection.cleanUp(resultSet);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Update set(String aName, String aValue) {
		return (Update) super.set(aName, aValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Update set(String aName, int aValue) {
		return (Update) super.set(aName, aValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Update set(String aName, long aValue) {
		return (Update) super.set(aName, aValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Update set(String aName, short aValue) {
		return (Update) super.set(aName, aValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Update set(String aName, float aValue) {
		return (Update) super.set(aName, aValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Update set(String aName, double aValue) {
		return (Update) super.set(aName, aValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Update set(String aName, boolean aValue) {
		return (Update) super.set(aName, aValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Update set(String aName, java.util.Date aValue) {
		return (Update) super.set(aName, aValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Update setNull(String aName, int aSqlType) {
		return (Update) super.set(aName, aSqlType);
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
		return "Update [statement=" + this.statement + ", connection=" + this.connection
				+ ", preparedStatement=" + this.preparedStatement + ", parameters=" + this.parameters + "]";
	}
}
