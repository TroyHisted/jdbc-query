package org.jdbcquery;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Mocks a prepared statement.
 *
 * @author Troy Histed
 */
public class MockPreparedStatement implements PreparedStatement {

	private final Map<Integer, Object> values = new HashMap<Integer, Object>();
	private boolean open = true;
	private MockResultSet resultSet;
	int batchCount = 0;

	/**
	 * --------------------------
	 * Mocked methods
	 * --------------------------
	 */

	/**
	 * @return the values
	 */
	public Map<Integer, Object> getValues() {
		return this.values;
	}

	public void close() throws SQLException {
		this.open = false;
	}

	public boolean isClosed() throws SQLException {
		return !this.open;
	}

	public ResultSet executeQuery() throws SQLException {
		if (this.resultSet == null) {
			this.resultSet = new MockResultSet();
		}
		return this.resultSet;
	}

	public int executeUpdate() throws SQLException {
		return 1;
	}

	public void addBatch() throws SQLException {
		this.batchCount += 1;
	}

	public int[] executeBatch() throws SQLException {
		return new int[this.batchCount];
	}

	/**
	 * @param resultSet the resultSet to set
	 */
	public void setResultSet(MockResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public MockResultSet getResultSet() throws SQLException {
		return this.resultSet;
	}

	/**
	 * --------------------------
	 * Mocked setter methods
	 * --------------------------
	 */

	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		this.values.put(Integer.valueOf(parameterIndex), null);
	}

	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		this.values.put(Integer.valueOf(parameterIndex), x);
	}

	public void setByte(int parameterIndex, byte x) throws SQLException {
		this.values.put(Integer.valueOf(parameterIndex), x);
	}

	public void setShort(int parameterIndex, short x) throws SQLException {
		this.values.put(Integer.valueOf(parameterIndex), x);
	}

	public void setInt(int parameterIndex, int x) throws SQLException {
		this.values.put(Integer.valueOf(parameterIndex), x);
	}

	public void setLong(int parameterIndex, long x) throws SQLException {
		this.values.put(Integer.valueOf(parameterIndex), x);
	}

	public void setFloat(int parameterIndex, float x) throws SQLException {
		this.values.put(Integer.valueOf(parameterIndex), x);
	}

	public void setDouble(int parameterIndex, double x) throws SQLException {
		this.values.put(Integer.valueOf(parameterIndex), x);
	}

	public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
		this.values.put(Integer.valueOf(parameterIndex), x);
	}

	public void setString(int parameterIndex, String x) throws SQLException {
		this.values.put(Integer.valueOf(parameterIndex), x);
	}

	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		this.values.put(Integer.valueOf(parameterIndex), x);
	}

	public void setDate(int parameterIndex, Date x) throws SQLException {
		this.values.put(Integer.valueOf(parameterIndex), x);
	}

	public void setTime(int parameterIndex, Time x) throws SQLException {
		this.values.put(Integer.valueOf(parameterIndex), x);
	}

	public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
		this.values.put(Integer.valueOf(parameterIndex), x);
	}

	public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
		this.values.put(Integer.valueOf(parameterIndex), x);
	}

	public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
		this.values.put(Integer.valueOf(parameterIndex), x);
	}

	public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
		this.values.put(Integer.valueOf(parameterIndex), x);
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
		this.values.put(Integer.valueOf(parameterIndex), x);
	}

	public void setObject(int parameterIndex, Object x) throws SQLException {
		this.values.put(Integer.valueOf(parameterIndex), x);
	}

	public void setBlob(int parameterIndex, Blob x) throws SQLException {
		this.values.put(Integer.valueOf(parameterIndex), x);
	}

	public void setClob(int parameterIndex, Clob x) throws SQLException {
		this.values.put(Integer.valueOf(parameterIndex), x);
	}

	public void setArray(int parameterIndex, Array x) throws SQLException {
		this.values.put(Integer.valueOf(parameterIndex), x);
	}

	/**
	 * --------------------------
	 * Unimplemented methods
	 * --------------------------
	 */

	public boolean execute() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void clearParameters() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public int executeUpdate(String sql) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setRef(int parameterIndex, Ref x) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public int getMaxFieldSize() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setMaxFieldSize(int max) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public int getMaxRows() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setMaxRows(int max) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setEscapeProcessing(boolean enable) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public int getQueryTimeout() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setQueryTimeout(int seconds) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void cancel() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public SQLWarning getWarnings() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void clearWarnings() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setCursorName(String name) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public boolean execute(String sql) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public int getUpdateCount() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public boolean getMoreResults() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setFetchDirection(int direction) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public int getFetchDirection() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setFetchSize(int rows) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public int getFetchSize() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public int getResultSetConcurrency() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public int getResultSetType() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void addBatch(String sql) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void clearBatch() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public Connection getConnection() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public boolean getMoreResults(int current) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public boolean execute(String sql, String[] columnNames) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public int getResultSetHoldability() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setPoolable(boolean poolable) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public boolean isPoolable() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void closeOnCompletion() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public boolean isCloseOnCompletion() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setURL(int parameterIndex, URL x) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setNString(int parameterIndex, String value) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength)
			throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}

	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		throw new UnsupportedOperationException("This is a mock object");
	}
}
