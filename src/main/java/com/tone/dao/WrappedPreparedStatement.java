package com.tone.dao;

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
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.bdcc.waf.db.WrappedPreparedStatement;
//import com.bdcc.waf.db.WrappedStatement;
//import com.bdcc.waf.logging.Logger;
//import com.bdcc.waf.logging.LoggerFactory;

public class WrappedPreparedStatement extends WrappedStatement implements PreparedStatement {
	private static Logger log = LoggerFactory.getLogger(WrappedPreparedStatement.class);
	private PreparedStatement origStatement;

	public void setStatement(PreparedStatement statement) {
		this.origStatement = statement;
	}

	protected Statement getStatement() {
		return this.origStatement;
	}

	public ResultSet executeQuery() throws SQLException {
		if (this.origStatement != null) {
			if (log.isDebugEnabled()) {
				long rs2 = System.currentTimeMillis();
				ResultSet rs1 = this.origStatement.executeQuery();
				log.debug("[preparedStatement]<" + (System.currentTimeMillis() - rs2) + ">" + this.sqlString);
				return rs1;
			} else {
				ResultSet rs = this.origStatement.executeQuery();
				return rs;
			}
		} else {
			return null;
		}
	}

	public int executeUpdate() throws SQLException {
		if (this.origStatement != null) {
			if (log.isDebugEnabled()) {
				long tt1 = System.currentTimeMillis();
				int resu = this.origStatement.executeUpdate();
				log.debug("[preparedStatement]<" + (System.currentTimeMillis() - tt1) + ">" + this.sqlString);
				return resu;
			} else {
				return this.origStatement.executeUpdate();
			}
		} else {
			return 0;
		}
	}

	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		this.origStatement.setNull(parameterIndex, sqlType);
	}

	public void setBoolean(int parameterIndex, boolean value) throws SQLException {
		this.origStatement.setBoolean(parameterIndex, value);
	}

	public void setByte(int parameterIndex, byte value) throws SQLException {
		this.origStatement.setByte(parameterIndex, value);
	}

	public void setShort(int parameterIndex, short value) throws SQLException {
		this.origStatement.setShort(parameterIndex, value);
	}

	public void setInt(int parameterIndex, int value) throws SQLException {
		this.origStatement.setInt(parameterIndex, value);
	}

	public void setLong(int parameterIndex, long value) throws SQLException {
		this.origStatement.setLong(parameterIndex, value);
	}

	public void setFloat(int parameterIndex, float value) throws SQLException {
		this.origStatement.setFloat(parameterIndex, value);
	}

	public void setDouble(int parameterIndex, double value) throws SQLException {
		this.origStatement.setDouble(parameterIndex, value);
	}

	public void setBigDecimal(int parameterIndex, BigDecimal value) throws SQLException {
		this.origStatement.setBigDecimal(parameterIndex, value);
	}

	public void setString(int parameterIndex, String value) throws SQLException {
		this.origStatement.setString(parameterIndex, value);
	}

	public void setBytes(int parameterIndex, byte[] value) throws SQLException {
		this.origStatement.setBytes(parameterIndex, value);
	}

	public void setDate(int parameterIndex, Date value) throws SQLException {
		this.origStatement.setDate(parameterIndex, value);
	}

	public void setTime(int parameterIndex, Time value) throws SQLException {
		this.origStatement.setTime(parameterIndex, value);
	}

	public void setTimestamp(int parameterIndex, Timestamp value) throws SQLException {
		this.origStatement.setTimestamp(parameterIndex, value);
	}

	public void setAsciiStream(int parameterIndex, InputStream value, int length) throws SQLException {
		this.origStatement.setAsciiStream(parameterIndex, value, length);
	}

	public void setUnicodeStream(int parameterIndex, InputStream value, int length) throws SQLException {
		this.origStatement.setUnicodeStream(parameterIndex, value, length);
	}

	public void setBinaryStream(int parameterIndex, InputStream value, int length) throws SQLException {
		this.origStatement.setBinaryStream(parameterIndex, value, length);
	}

	public void clearParameters() throws SQLException {
		this.origStatement.clearParameters();
	}

	public void setObject(int parameterIndex, Object value, int targetSqlType, int scale) throws SQLException {
		this.origStatement.setObject(parameterIndex, value, targetSqlType, scale);
	}

	public void setObject(int parameterIndex, Object value, int targetSqlType) throws SQLException {
		this.origStatement.setObject(parameterIndex, value, targetSqlType);
	}

	public void setObject(int parameterIndex, Object value) throws SQLException {
		this.origStatement.setObject(parameterIndex, value);
	}

	public boolean execute() throws SQLException {
		if (this.origStatement != null) {
			if (log.isDebugEnabled()) {
				long tt1 = System.currentTimeMillis();
				boolean resu = this.origStatement.execute();
				log.debug("[preparedStatement]<" + (System.currentTimeMillis() - tt1) + ">" + this.sqlString);
				return resu;
			} else {
				return this.origStatement.execute();
			}
		} else {
			return false;
		}
	}

	public void addBatch() throws SQLException {
		this.origStatement.addBatch();
	}

	public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
		this.origStatement.setCharacterStream(parameterIndex, reader, length);
	}

	public void setRef(int paramIndex, Ref value) throws SQLException {
		this.origStatement.setRef(paramIndex, value);
	}

	public void setBlob(int paramIndex, Blob value) throws SQLException {
		this.origStatement.setBlob(paramIndex, value);
	}

	public void setClob(int paramIndex, Clob value) throws SQLException {
		this.origStatement.setClob(paramIndex, value);
	}

	public void setArray(int paramIndex, Array value) throws SQLException {
		this.origStatement.setArray(paramIndex, value);
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		return this.origStatement.getMetaData();
	}

	public void setDate(int parameterIndex, Date value, Calendar cal) throws SQLException {
		this.origStatement.setDate(parameterIndex, value, cal);
	}

	public void setTime(int parameterIndex, Time value, Calendar cal) throws SQLException {
		this.origStatement.setTime(parameterIndex, value, cal);
	}

	public void setTimestamp(int parameterIndex, Timestamp value, Calendar cal) throws SQLException {
		this.origStatement.setTimestamp(parameterIndex, value, cal);
	}

	public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException {
		this.origStatement.setNull(paramIndex, sqlType, typeName);
	}

	public void setURL(int parameterIndex, URL x) throws SQLException {
		this.origStatement.setURL(parameterIndex, x);
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		return this.origStatement.getParameterMetaData();
	}

	public Connection getConnection() throws SQLException {
		return this.origStatement.getConnection();
	}

	public void close() throws SQLException {
		if (this.origStatement != null) {
			try {
				this.origStatement.close();
				this.origStatement = null;
			} catch (Exception arg1) {
				;
			}
		}

	}

	public void setAsciiStream(int arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setAsciiStream(int arg0, InputStream arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setBinaryStream(int arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setBinaryStream(int arg0, InputStream arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setBlob(int arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setBlob(int arg0, InputStream arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setCharacterStream(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setClob(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setClob(int arg0, Reader arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setNCharacterStream(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setNCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setNClob(int arg0, NClob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setNClob(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setNClob(int arg0, Reader arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setNString(int arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setRowId(int arg0, RowId arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setSQLXML(int arg0, SQLXML arg1) throws SQLException {
		// TODO Auto-generated method stub

	}
}
