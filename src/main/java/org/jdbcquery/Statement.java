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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Represents an SQL statement.
 *
 * @author Troy Histed
 */
public abstract class Statement {

	/**
	 * @return the parameters
	 */
	protected abstract List<String> getParameters();

	/**
	 * @return the prepared statement
	 */
	protected abstract PreparedStatement getPreparedStatement();

	/**
	 * @return the connection
	 */
	protected abstract JdbcConnection getConnection();

	/**
	 * Sets a string into the prepared statement using the specified parameter name.
	 *
	 * @param aName
	 *            the name of the parameter to set
	 * @param aValue
	 *            the value to set
	 * @return the statement (for method chaining)
	 */
	public Statement set(String aName, String aValue) {
		try {
			for (int i = 0; i < this.getParameters().size(); i++) {
				if (this.getParameters().get(i).equals(aName)) {
					this.getPreparedStatement().setString(i + 1, aValue);
				}
			}
		} catch (final SQLException e) {
			this.getConnection().cleanUp();
			throw new DaoException("Error setting " + aName + " to " + aValue, e);
		}
		return this;
	}

	/**
	 * <p>
	 * Sets an int into the prepared statement using the specified parameter name.
	 *
	 * @param aName
	 *            the name of the parameter to set
	 * @param aValue
	 *            the value to set
	 * @return the statement (for method chaining)
	 */
	public Statement set(String aName, int aValue) {
		try {
			for (int i = 0; i < this.getParameters().size(); i++) {
				if (this.getParameters().get(i).equals(aName)) {
					this.getPreparedStatement().setInt(i + 1, aValue);
				}
			}
		} catch (final SQLException e) {
			this.getConnection().cleanUp();
			throw new DaoException("Error setting " + aName + " to " + aValue, e);
		}
		return this;
	}

	/**
	 * Sets a long into the prepared statement using the specified parameter name.
	 *
	 * @param aName
	 *            the name of the parameter to set
	 * @param aValue
	 *            the aValue to set
	 * @return the statement (for method chaining)
	 */
	public Statement set(String aName, long aValue) {
		try {
			for (int i = 0; i < this.getParameters().size(); i++) {
				if (this.getParameters().get(i).equals(aName)) {
					this.getPreparedStatement().setLong(i + 1, aValue);
				}
			}
		} catch (final SQLException e) {
			this.getConnection().cleanUp();
			throw new DaoException("Error setting " + aName + " to " + aValue, e);
		}
		return this;
	}

	/**
	 * Sets a short into the prepared statement using the specified parameter name.
	 *
	 * @param aName
	 *            the name of the parameter to set
	 * @param aValue
	 *            the value to set
	 * @return the statement (for method chaining)
	 */
	public Statement set(String aName, short aValue) {
		try {
			for (int i = 0; i < this.getParameters().size(); i++) {
				if (this.getParameters().get(i).equals(aName)) {
					this.getPreparedStatement().setShort(i + 1, aValue);
				}
			}
		} catch (final SQLException e) {
			this.getConnection().cleanUp();
			throw new DaoException("Error setting " + aName + " to " + aValue, e);
		}
		return this;
	}

	/**
	 * Sets a float into the prepared statement using the specified parameter name.
	 *
	 * @param aName
	 *            the name of the parameter to set
	 * @param aValue
	 *            the value to set
	 * @return the statement (for method chaining)
	 */
	public Statement set(String aName, float aValue) {
		try {
			for (int i = 0; i < this.getParameters().size(); i++) {
				if (this.getParameters().get(i).equals(aName)) {
					this.getPreparedStatement().setFloat(i + 1, aValue);
				}
			}
		} catch (final SQLException e) {
			this.getConnection().cleanUp();
			throw new DaoException("Error setting " + aName + " to " + aValue, e);
		}
		return this;
	}

	/**
	 * Sets a double into the prepared statement using the specified parameter name.
	 *
	 * @param aName
	 *            the name of the parameter to set
	 * @param aValue
	 *            the value to set
	 * @return the statement (for method chaining)
	 */
	public Statement set(String aName, double aValue) {
		try {
			for (int i = 0; i < this.getParameters().size(); i++) {
				if (this.getParameters().get(i).equals(aName)) {
					this.getPreparedStatement().setDouble(i + 1, aValue);
				}
			}
		} catch (final SQLException e) {
			this.getConnection().cleanUp();
			throw new DaoException("Error setting " + aName + " to " + aValue, e);
		}
		return this;
	}

	/**
	 * Sets a boolean into the prepared statement using the specified parameter name.
	 *
	 * @param aName
	 *            the name of the parameter to set
	 * @param aValue
	 *            the value to set
	 * @return the statement (for method chaining)
	 */
	public Statement set(String aName, boolean aValue) {
		try {
			for (int i = 0; i < this.getParameters().size(); i++) {
				if (this.getParameters().get(i).equals(aName)) {
					this.getPreparedStatement().setBoolean(i + 1, aValue);
				}
			}
		} catch (final SQLException e) {
			this.getConnection().cleanUp();
			throw new DaoException("Error setting " + aName + " to " + aValue, e);
		}
		return this;
	}

	/**
	 * Sets a Date into the prepared statement using the specified parameter name.
	 *
	 * @param aName
	 *            the name of the parameter to set
	 * @param aValue
	 *            the value to set
	 * @return the statement (for method chaining)
	 */
	public Statement set(String aName, java.util.Date aValue) {
		try {
			for (int i = 0; i < this.getParameters().size(); i++) {
				if (this.getParameters().get(i).equals(aName)) {
					this.getPreparedStatement().setTimestamp(i + 1, new Timestamp(aValue.getTime()));
				}
			}
		} catch (final SQLException e) {
			this.getConnection().cleanUp();
			throw new DaoException("Error setting " + aName + " to " + aValue, e);
		}
		return this;
	}

	/**
	 * Sets the specified parameter name to give a null value.
	 *
	 * @param aName
	 *            the name of the parameter to set
	 * @param aSqlType
	 *            the java.sql.Type of the column to set to null
	 * @return the statement (for method chaining)
	 */
	public Statement setNull(String aName, int aSqlType) {
		try {
			for (int i = 0; i < this.getParameters().size(); i++) {
				if (this.getParameters().get(i).equals(aName)) {
					this.getPreparedStatement().setNull(i + 1, aSqlType);
				}
			}
		} catch (final SQLException e) {
			this.getConnection().cleanUp();
			throw new DaoException("Error setting " + aName + " of type " + aSqlType + " to null", e);
		}
		return this;
	}
}
