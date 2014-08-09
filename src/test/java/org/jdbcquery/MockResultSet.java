package org.jdbcquery;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a mocked result set.
 *
 * @author Troy Histed
 */
public class MockResultSet implements ResultSet {

	private final List<LinkedHashMap<String, Object>> values = new ArrayList<LinkedHashMap<String, Object>>();
	private LinkedHashMap<String, Object> currentRow = null;
	private boolean open = true;
	private int cursorIndex = -1;
	private boolean wasNull = false;

	/**
	 * --------------------------
	 * Mocked methods
	 * --------------------------
	 */

	public void close() throws SQLException {
		this.open = false;
	}

	public boolean isClosed() throws SQLException {
		return !this.open;
	}

	public boolean isAfterLast() throws SQLException {
		return this.cursorIndex >= this.values.size();
	}

	public boolean next() throws SQLException {
		if (this.isAfterLast()) {
			throw new SQLException("Cursor already after last");
		}
		this.cursorIndex += 1;
		if (this.isAfterLast()) {
			return false;
		}
		this.currentRow = this.values.get(this.cursorIndex);
		return true;
	}


	public boolean wasNull() throws SQLException {
		return this.wasNull;
	}

	public int getRow() throws SQLException {
		return this.cursorIndex + 1;
	}

	/**
	 * --------------------------
	 * Custom methods
	 * --------------------------
	 */

	/**
	 * @return the values
	 */
	public List<LinkedHashMap<String, Object>> getValues() {
		return this.values;
	}

	/**
	 * --------------------------
	 * Mocked getter methods
	 * --------------------------
	 */

	public String getString(String columnLabel) throws SQLException {
		final String value = (String) this.currentRow.get(columnLabel);
		this.wasNull = value == null;
		return value;
	}

	public int getInt(String columnLabel) throws SQLException {
		final Integer value = (Integer) this.currentRow.get(columnLabel);
		this.wasNull = value == null;
		return value == null ? 0 : value.intValue();
	}

	public long getLong(String columnLabel) throws SQLException {
		final Long value = (Long) this.currentRow.get(columnLabel);
		this.wasNull = value == null;
		return value == null ? 0 : value.longValue();
	}

	/**
	 * --------------------------
	 * Unimplemented methods
	 * --------------------------
	 */

	public String getString(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public int getInt(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public long getLong(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public boolean getBoolean(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public byte getByte(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public short getShort(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public float getFloat(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public double getDouble(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public byte[] getBytes(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Date getDate(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Time getTime(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public boolean getBoolean(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public byte getByte(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public short getShort(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public float getFloat(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public double getDouble(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public byte[] getBytes(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Date getDate(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Time getTime(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}


	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public InputStream getUnicodeStream(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public SQLWarning getWarnings() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void clearWarnings() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public String getCursorName() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Object getObject(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Object getObject(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public int findColumn(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Reader getCharacterStream(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Reader getCharacterStream(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public boolean isBeforeFirst() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public boolean isFirst() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public boolean isLast() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void beforeFirst() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void afterLast() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public boolean first() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public boolean last() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public boolean absolute(int row) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public boolean relative(int rows) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public boolean previous() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void setFetchDirection(int direction) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public int getFetchDirection() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void setFetchSize(int rows) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public int getFetchSize() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public int getType() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public int getConcurrency() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public boolean rowUpdated() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public boolean rowInserted() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public boolean rowDeleted() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateNull(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateByte(int columnIndex, byte x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateShort(int columnIndex, short x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateInt(int columnIndex, int x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateLong(int columnIndex, long x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateFloat(int columnIndex, float x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateDouble(int columnIndex, double x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateString(int columnIndex, String x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateDate(int columnIndex, Date x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateTime(int columnIndex, Time x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateObject(int columnIndex, Object x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateNull(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateBoolean(String columnLabel, boolean x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateByte(String columnLabel, byte x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateShort(String columnLabel, short x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateInt(String columnLabel, int x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateLong(String columnLabel, long x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateFloat(String columnLabel, float x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateDouble(String columnLabel, double x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateString(String columnLabel, String x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateBytes(String columnLabel, byte[] x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateDate(String columnLabel, Date x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateTime(String columnLabel, Time x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateObject(String columnLabel, Object x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void insertRow() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateRow() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void deleteRow() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void refreshRow() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void cancelRowUpdates() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void moveToInsertRow() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void moveToCurrentRow() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Statement getStatement() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Ref getRef(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Blob getBlob(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Clob getClob(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Array getArray(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Ref getRef(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Blob getBlob(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Clob getClob(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Array getArray(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Time getTime(String columnLabel, Calendar cal) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public URL getURL(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public URL getURL(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateRef(int columnIndex, Ref x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateRef(String columnLabel, Ref x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateBlob(String columnLabel, Blob x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateClob(int columnIndex, Clob x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateClob(String columnLabel, Clob x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateArray(int columnIndex, Array x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateArray(String columnLabel, Array x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public RowId getRowId(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public RowId getRowId(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public int getHoldability() throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateNString(int columnIndex, String nString) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateNString(String columnLabel, String nString) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public NClob getNClob(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public NClob getNClob(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public String getNString(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public String getNString(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateClob(String columnLabel, Reader reader) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public void updateNClob(String columnLabel, Reader reader) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}

	public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
		throw new IllegalStateException("This is a mock class");
	}
}