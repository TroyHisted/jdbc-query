/**
 * Copyright 2014 Troy Histed
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdbcquery;

import java.sql.Connection;
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
		this(aStatement, (String) null);
	}

	/**
	 * Constructs an update statement and performs initialization.
	 *
	 * @param aStatement
	 *            the statement to be executed
	 * @param aConnectionName
	 *            the name of the connection to use
	 */
	public Update(String aStatement, String aConnectionName) {

		this.statement = aStatement;

		final ParsedNamedStatement preparedStatement = Update.STATEMENT_PARSER.prepareNamedStatement(aStatement);
		this.parameters = preparedStatement.getParameters();

		try {
			this.connection = this.connect(aConnectionName);
			this.preparedStatement = this.connection.prepareStatementWithGeneratedKeys(preparedStatement
					.getStatement());
		} catch (final SQLException e) {
			if (this.connection != null) {
				this.connection.cleanUp();
			}
			throw new DaoException("Error creating connection and preparing statement: " + aStatement, e);
		} catch (final RuntimeException e) {
			if (this.connection != null) {
				this.connection.cleanUp();
			}
			throw e;
		}
	}

	/**
	 * Constructs an update statement and performs initialization.
	 *
	 * @param aStatement
	 *            the statement to be executed
	 * @param aConnection
	 *            the connection to use
	 */
	public Update(String aStatement, Connection aConnection) {

		this.statement = aStatement;
		this.connection = new JdbcConnection(aConnection);

		final ParsedNamedStatement preparedStatement = Update.STATEMENT_PARSER.prepareNamedStatement(aStatement);
		this.parameters = preparedStatement.getParameters();

		try {
			this.preparedStatement = this.connection.prepareStatementWithGeneratedKeys(preparedStatement
					.getStatement());
		} catch (final SQLException e) {
			if (this.connection != null) {
				this.connection.cleanUp();
			}
			throw new DaoException("Error preparing statement: " + aStatement, e);
		}
	}

	/**
	 * Gets a connection.
	 *
	 * @param aConnectionName
	 *            the connection name to use
	 * @return a connection
	 * @throws SQLException
	 */
	protected JdbcConnection connect(String aConnectionName) throws SQLException {
		return JdbcConnection.connect(aConnectionName);
	}

	/**
	 * Executes the statement.
	 *
	 * @return the number of records updated
	 */
	public int execute() {
		try {
			return this.preparedStatement.executeUpdate();
		} catch (final SQLException e) {
			throw new DaoException("Error executing : " + this, e);
		} finally {
			this.connection.cleanUp();
		}
	}

	/**
	 * Executes the statement.
	 *
	 * @return the auto-generated key
	 */
	@SuppressWarnings("resource")
	public long executeAndReturnKey() {
		ResultSet resultSet = null;
		try {
			this.preparedStatement.executeUpdate();
			resultSet = this.preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				return resultSet.getLong(1);
			}
			throw new DaoException("No key was generated for " + this);
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
		try {
			return this.preparedStatement.executeBatch();
		} catch (final SQLException e) {
			throw new DaoException("Error executing batch: " + this, e);
		} finally {
			this.connection.cleanUp();
		}
	}

	/**
	 * Executes the batch statements that have been added to this object.
	 *
	 * @return array containing the auto generated keys for each batch statement
	 */
	@SuppressWarnings("resource")
	public long[] executeBatchAndReturnKeys() {
		ResultSet resultSet = null;
		try {
			final int[] updateCount = this.preparedStatement.executeBatch();

			resultSet = this.preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				final long[] generatedKeys = new long[updateCount.length];
				int i = 0;
				generatedKeys[i] = resultSet.getLong(1);
				i++;
				while (resultSet.next()) {
					generatedKeys[i] = resultSet.getLong(1);
					i++;
				}
				return generatedKeys;
			}
			throw new DaoException("No key was generated for " + this);
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
	public Update setObject(String aName, Object aValue) {
		return (Update) super.setObject(aName, aValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Update setObject(String aName, Object aValue, int aSqlType) {
		return (Update) super.setObject(aName, aValue, aSqlType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Update setBean(Object aJavaBean) {
		return (Update) super.setBean(aJavaBean);
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
