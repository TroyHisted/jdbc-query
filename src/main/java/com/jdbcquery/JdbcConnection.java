package com.jdbcquery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents a connection to a database.
 *
 * @author Troy Histed
 */
public class JdbcConnection {

	private Connection connection = null;

	/**
	 * Constructs a DaoConnection with an SQL connection object.
	 *
	 * @param aConnection
	 *            the SQL connection to use
	 */
	private JdbcConnection(Connection aConnection) {
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
		return this.connection.prepareStatement(aStatement);
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
		}
		this.cleanUp();
	}
}