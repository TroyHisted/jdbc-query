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

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles converting a single row from a result set into an object of type T.
 *
 * @author Troy Histed
 *
 * @param <T> The object type that will be created with each row
 */
public abstract class RowMapper<T> {

	/**
	 * Maps a single result set record to an instance of the query type.
	 *
	 * @param aResultSet
	 *            the result set record to process
	 * @return the mapped row
	 * @throws SQLException
	 *             the sql exception
	 */
	protected abstract T mapRow(ResultSet aResultSet) throws SQLException;

}
