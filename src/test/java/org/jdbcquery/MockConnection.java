package org.jdbcquery;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * Mock connection.
 *
 * @author Troy Histed
 */
public class MockConnection implements Connection {

	private boolean open = false;
	MockPreparedStatement preparedStatement;

	/**
	 * --------------------------
	 * Mocked methods
	 * --------------------------
	 */

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		this.open = true;
		this.preparedStatement = new MockPreparedStatement();
		return this.preparedStatement;
	}

	public void close() throws SQLException {
		this.open = false;
	}

	public boolean isClosed() throws SQLException {
		return !this.open;
	}

	/**
	 * @return the mocked prepared statement
	 */
	public MockPreparedStatement getPreparedStatement() {
		return this.preparedStatement;
	}

	/**
	 * --------------------------
	 * Unimplemented methods
	 * --------------------------
	 */

	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public Statement createStatement() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public String nativeSQL(String sql) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public boolean getAutoCommit() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void commit() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void rollback() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public boolean isReadOnly() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setCatalog(String catalog) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public String getCatalog() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setTransactionIsolation(int level) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public int getTransactionIsolation() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public SQLWarning getWarnings() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void clearWarnings() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
			throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
			throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public Map<String, Class<?>> getTypeMap() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setHoldability(int holdability) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public int getHoldability() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public Savepoint setSavepoint() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public Clob createClob() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public Blob createBlob() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public NClob createNClob() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public SQLXML createSQLXML() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public boolean isValid(int timeout) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setClientInfo(String name, String value) throws SQLClientInfoException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public String getClientInfo(String name) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public Properties getClientInfo() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setSchema(String schema) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public String getSchema() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void abort(Executor executor) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public int getNetworkTimeout() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

}
