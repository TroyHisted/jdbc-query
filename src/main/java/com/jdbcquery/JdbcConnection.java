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

	private static final JdbcConnector DAO_CONNECTOR = ConnectorServiceLoader.getConnector();
	
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
	 * 
	 * @return a connection
	 * @throws SQLException
	 *             error creating connection
	 */
	public static JdbcConnection connect() throws SQLException {
		return new JdbcConnection(DAO_CONNECTOR.getConnection());
	}

	/**
	 * Prepares a statement using the established connection.
	 * 
	 * @param aQuery
	 *            the query to prepare
	 * @return the prepared statement
	 * @throws SQLException
	 *             error building prepared statement
	 */
	PreparedStatement prepareStatement(String aQuery) throws SQLException {
		return this.connection.prepareStatement(aQuery);
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
			throw new DaoException("Error closing connection", e);
		}
	}

	/**
	 * Closes the connection and result set.
	 *
	 * @param result
	 *            the result set to close
	 */
	public void cleanUp(ResultSet result) {
		try {
			if (result != null) {
				result.close();
			}
			this.cleanUp();
		} catch (final SQLException e) {
			throw new DaoException("Error closing result set", e);
		}
	}
}