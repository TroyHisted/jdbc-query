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