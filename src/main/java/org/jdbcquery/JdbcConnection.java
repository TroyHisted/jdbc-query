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
import java.sql.Statement;

/**
 * Represents a connection to a database.
 *
 * @author Troy Histed
 */
public class JdbcConnection {

	private Connection connection = null;
	private PreparedStatement preparedStatement = null;

	/**
	 * Constructs a DaoConnection with an SQL connection object.
	 *
	 * @param aConnection
	 *            the SQL connection to use
	 */
	JdbcConnection(Connection aConnection) {
		this.connection = aConnection;
	}

	/**
	 * Gets a connection to the data source provided by the DaoConnection.
	 * @param aConnectionName
	 *
	 * @return a connection
	 * @throws SQLException
	 *             error creating connection
	 */
	public static JdbcConnection connect(String aConnectionName) throws SQLException {
		return new JdbcConnection(ConnectorServiceLoader.getConnector(aConnectionName).getConnection());
	}

	/**
	 * Prepares a statement using the established connection.
	 *
	 * @param aStatement
	 *            the statement to prepare
	 * @return the prepared statement
	 * @throws SQLException
	 *             error building prepared statement
	 */
	PreparedStatement prepareStatement(String aStatement) throws SQLException {
		this.preparedStatement = this.connection.prepareStatement(aStatement);
		return this.preparedStatement;
	}

	/**
	 * Prepares a statement using the established connection.
	 *
	 * @param aStatement
	 *            the statement to prepare
	 * @return the prepared statement
	 * @throws SQLException
	 *             error building prepared statement
	 */
	PreparedStatement prepareStatementWithGeneratedKeys(String aStatement) throws SQLException {
		this.preparedStatement = this.connection.prepareStatement(aStatement, Statement.RETURN_GENERATED_KEYS);
		return this.preparedStatement;
	}

	/**
	 * Closes the connection.
	 */
	public void cleanUp() {
		try {
			if (this.connection != null) {
				this.connection.close();
			}
		} catch (final SQLException e) {
			throw new DaoException("Error closing connection: " + this.connection, e);
		} finally {
			if (this.preparedStatement != null) {
				try {
					this.preparedStatement.close();
				} catch (final SQLException e) {
					throw new DaoException("Error closing prepared statement: " + this.preparedStatement, e);
				}
			}
		}
	}

	/**
	 * Closes the connection and result set.
	 *
	 * @param aResultSet
	 *            the result set to close
	 */
	public void cleanUp(ResultSet aResultSet) {
		try {
			if (aResultSet != null) {
				aResultSet.close();
			}
		} catch (final SQLException e) {
			throw new DaoException("Error closing result set: " + aResultSet, e);
		} finally {
			this.cleanUp();
		}
	}
}