package com.jdbcquery;

import java.sql.Connection;

/**
 * Provides a connection to a database.
 *
 * @author Troy Histed
 */
public interface JdbcConnector {

	/**
	 * Creates a connection to a data source.
	 *
	 * @return connection
	 */
	Connection getConnection();

	/**
	 * Returns a name for the connection that can be used to uniquely identify a connector when there are
	 * multiple implementations of JdbcConnector available.
	 *
	 * @return a name for the connection
	 */
	String getName();
}