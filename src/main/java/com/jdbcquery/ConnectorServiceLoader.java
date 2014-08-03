package com.jdbcquery;

import java.util.ServiceLoader;

/**
 * Handles loading the designated DaoConnector.
 *
 * @author Troy Histed
 */
public class ConnectorServiceLoader {

	private static ServiceLoader<JdbcConnector> CONNECTION_LOADER = ServiceLoader.load(JdbcConnector.class);

	/**
	 * Returns the first DaoConnector that is available through the Service
	 * Provider Interface.
	 * @param aConnectionName
	 *
	 * @return connection loader
	 */
	public static JdbcConnector getConnector(String aConnectionName) {
		for (final JdbcConnector connector : ConnectorServiceLoader.CONNECTION_LOADER) {
			if (aConnectionName == null || aConnectionName.equals(connector.getName())) {
				return connector;
			}
		}
		throw new IllegalStateException("No DaoConnector defined as a provided service. " + aConnectionName);
	}
}
