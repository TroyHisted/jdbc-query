package com.jdbcquery;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Represents a connection to a database.
 *
 * @author Troy Histed
 */
public interface JdbcConnector {

	/**
	 * Creates a connection to a data source.
	 * 
	 * @return connection
	 * @throws SQLException
	 *             error creating connection
	 */
	public Connection getConnection() throws SQLException;
}