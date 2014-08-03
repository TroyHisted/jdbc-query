package com.jdbcquery;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Represents an SQL statement.
 *
 * <p>
 * The named parameter must be terminated by either a space, comma, or a right paren.
 *
 * @author Troy Histed
 *
 * @param <T>
 *            The object type that can be queried for
 */
public class Update {

	private static final NamedStatementParserStrategy STATEMENT_PARSER = new NamedStatementParserStrategy();

	private final String statement;
	private final JdbcConnection connection;
	private final PreparedStatement preparedStatement;
	private final List<String> parameters;

	/**
	 * Constructs a statement and performs initialization.
	 *
	 * @param aStatement
	 *            the statement to be executed
	 * @param aConnection
	 *            an enum representing the connection to use
	 */
	public Update(String aStatement, Enum<?> aConnection) {
		this(aStatement, aConnection.name());
	}

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
	 * @param aConnection
	 *            the name of the connection to use
	 */
	public Update(String aStatement, String aConnectionName) {

		this.statement = aStatement;
		final ParsedNamedStatement preparedStatement = Update.STATEMENT_PARSER.prepareNamedStatement(aStatement);
		this.parameters = preparedStatement.getParameters();

		try {
			this.connection = JdbcConnection.connect(aConnectionName);
			this.preparedStatement = this.connection.prepareStatement(preparedStatement.getStatement());
		} catch (final RuntimeException e) {
			if (this.connection != null) {
				this.connection.cleanUp();
			}
			throw e;
		} catch (final SQLException e) {
			if (this.connection != null) {
				this.connection.cleanUp();
			}
			throw new DaoException("Error creating connection and preparing statement: " + aStatement, e);
		}
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

	public void addBatch() {
		try {
			this.preparedStatement.addBatch();
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error adding batch: " + this, e);
		}
	}

	public void executeBatch() {
		try {
			this.preparedStatement.executeBatch();
		} catch (final SQLException e) {
			throw new DaoException("Error executing batch: " + this, e);
		} finally {
			this.connection.cleanUp();
		}
	}

	/**
	 * Sets a string into the prepared statement at the specified parameter index.
	 *
	 * @param value
	 *            the value to set
	 * @return The update (so that method calls can be chained);
	 */
	public Update set(int index, String value) {
		try {
			this.preparedStatement.setString(index, value);
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a string into the prepared statement using the specified parameter name.
	 *
	 * @param name
	 *            the name of the parameter to set
	 * @param value
	 *            the value to set
	 * @return The update (so that method calls can be chained);
	 */
	public Update set(String name, String value) {
		try {
			for (int i = 0; i < this.parameters.size(); i++) {
				if (this.parameters.get(i).equals(name)) {
					this.preparedStatement.setString(i + 1, value);
				}
			}
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * <p>
	 * Sets an int into the prepared statement at the specified parameter index.
	 *
	 * @param value
	 *            the value to set
	 * @return The update (so that method calls can be chained);
	 */
	public Update set(int index, int value) {
		try {
			this.preparedStatement.setInt(index, value);
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * <p>
	 * Sets an int into the prepared statement using the specified parameter name.
	 *
	 * @param name
	 *            the name of the parameter to set
	 * @param value
	 *            the value to set
	 * @return The update (so that method calls can be chained);
	 */
	public Update set(String name, int value) {
		try {
			for (int i = 0; i < this.parameters.size(); i++) {
				if (this.parameters.get(i).equals(name)) {
					this.preparedStatement.setInt(i + 1, value);
				}
			}
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a long into the prepared statement at the specified parameter index.
	 *
	 * @param value
	 *            the value to set
	 * @return The update (so that method calls can be chained);
	 */
	public Update set(int index, long value) {
		try {
			this.preparedStatement.setLong(index, value);
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a long into the prepared statement using the specified parameter name.
	 *
	 * @param name
	 *            the name of the parameter to set
	 * @param value
	 *            the value to set
	 * @return The update (so that method calls can be chained);
	 */
	public Update set(String name, long value) {
		try {
			for (int i = 0; i < this.parameters.size(); i++) {
				if (this.parameters.get(i).equals(name)) {
					this.preparedStatement.setLong(i + 1, value);
				}
			}
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a short into the prepared statement at the specified parameter index.
	 *
	 * @param value
	 *            the value to set
	 * @return The update (so that method calls can be chained);
	 */
	public Update set(int index, short value) {
		try {
			this.preparedStatement.setShort(index, value);
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a short into the prepared statement using the specified parameter name.
	 *
	 * @param name
	 *            the name of the parameter to set
	 * @param value
	 *            the value to set
	 * @return The update (so that method calls can be chained);
	 */
	public Update set(String name, short value) {
		try {
			for (int i = 0; i < this.parameters.size(); i++) {
				if (this.parameters.get(i).equals(name)) {
					this.preparedStatement.setShort(i + 1, value);
				}
			}
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a float into the prepared statement at the specified parameter index.
	 *
	 * @param value
	 *            the value to set
	 * @return The update (so that method calls can be chained);
	 */
	public Update set(int index, float value) {
		try {
			this.preparedStatement.setFloat(index, value);
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a float into the prepared statement using the specified parameter name.
	 *
	 * @param name
	 *            the name of the parameter to set
	 * @param value
	 *            the value to set
	 * @return The update (so that method calls can be chained);
	 */
	public Update set(String name, float value) {
		try {
			for (int i = 0; i < this.parameters.size(); i++) {
				if (this.parameters.get(i).equals(name)) {
					this.preparedStatement.setFloat(i + 1, value);
				}
			}
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a double into the prepared statement at the specified parameter index.
	 *
	 * @param value
	 *            the value to set
	 * @return The update (so that method calls can be chained);
	 */
	public Update set(int index, double value) {
		try {
			this.preparedStatement.setDouble(index, value);
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a double into the prepared statement using the specified parameter name.
	 *
	 * @param name
	 *            the name of the parameter to set
	 * @param value
	 *            the value to set
	 * @return The update (so that method calls can be chained);
	 */
	public Update set(String name, double value) {
		try {
			for (int i = 0; i < this.parameters.size(); i++) {
				if (this.parameters.get(i).equals(name)) {
					this.preparedStatement.setDouble(i + 1, value);
				}
			}
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a boolean into the prepared statement at the specified parameter index.
	 *
	 * @param value
	 *            the value to set
	 * @return The update (so that method calls can be chained);
	 */
	public Update set(int index, boolean value) {
		try {
			this.preparedStatement.setBoolean(index, value);
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a boolean into the prepared statement using the specified parameter name.
	 *
	 * @param name
	 *            the name of the parameter to set
	 * @param value
	 *            the value to set
	 * @return The update (so that method calls can be chained);
	 */
	public Update set(String name, boolean value) {
		try {
			for (int i = 0; i < this.parameters.size(); i++) {
				if (this.parameters.get(i).equals(name)) {
					this.preparedStatement.setBoolean(i + 1, value);
				}
			}
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a Date into the prepared statement at the specified parameter index.
	 *
	 * @param value
	 *            the value to set
	 * @return The update (so that method calls can be chained);
	 */
	public Update set(int index, java.util.Date value) {
		try {
			this.preparedStatement.setTimestamp(index, new Timestamp(value.getTime()));
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets a Date into the prepared statement using the specified parameter name.
	 *
	 * @param name
	 *            the name of the parameter to set
	 * @param value
	 *            the value to set
	 * @return The update (so that method calls can be chained);
	 */
	public Update set(String name, java.util.Date value) {
		try {
			for (int i = 0; i < this.parameters.size(); i++) {
				if (this.parameters.get(i).equals(name)) {
					this.preparedStatement.setTimestamp(i + 1, new Timestamp(value.getTime()));
				}
			}
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting : " + value, e);
		}
		return this;
	}

	/**
	 * Sets the specified parameter index to be null.
	 *
	 * @param index
	 *            the index of the parameter to set the value on
	 * @return The update (so that method calls can be chained);
	 */
	public Update setNull(int index, int sqlType) {
		try {
			this.preparedStatement.setNull(index, sqlType);
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting null: " + sqlType, e);
		}
		return this;
	}

	/**
	 * Sets the specified parameter name to have a null value.
	 *
	 * @param name
	 *            the name of the parameter to set
	 * @param value
	 *            the value to set
	 * @return The update (so that method calls can be chained);
	 */
	public Update setNull(String name, int sqlType) {
		try {
			for (int i = 0; i < this.parameters.size(); i++) {
				if (this.parameters.get(i).equals(name)) {
					this.preparedStatement.setNull(i + 1, sqlType);
				}
			}
		} catch (final SQLException e) {
			this.connection.cleanUp();
			throw new DaoException("Error setting null: " + sqlType, e);
		}
		return this;
	}

	/**
	 * @return the preparedStatement
	 */
	public PreparedStatement getPreparedStatement() {
		return this.preparedStatement;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Update [statement=" + this.statement + ", connection=" + this.connection + ", preparedStatement="
				+ this.preparedStatement + ", parameters=" + this.parameters + "]";
	}
}
