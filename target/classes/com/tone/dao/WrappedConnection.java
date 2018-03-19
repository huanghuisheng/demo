package com.tone.dao;

import com.tone.exception.SystemException;
import com.tone.interfaces.Closeable;
import com.tone.interfaces.Rollbackable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;


public class WrappedConnection implements java.sql.Connection, Closeable, Rollbackable {
	protected java.sql.Connection origConn;
	protected boolean isJta = false;
	private static Logger log = LoggerFactory
			.getLogger(WrappedConnection.class);

	public WrappedConnection(java.sql.Connection conn) throws SQLException {
		this.origConn = conn;
	}

	public void setJta(boolean isJtaFlag) throws SQLException {
		this.isJta = isJtaFlag;
		this.setAutoCommit(false);
	}

	public void objectClose() {
		try {
			if (!this.isClosed()) {
				this.commit();
			}
		} catch (Exception arg11) {
			try {
				this.rollback();
			} catch (Exception arg10) {
				;
			}

			throw new SystemException("Connection commit error", arg11);
		} finally {
			try {
				this.closeReally();
			} catch (Exception arg9) {
				;
			}

		}

	}

	public void transactionRollback() {
		try {
			if (!this.isClosed() && !this.isJta) {
				this.rollback();
			}
		} catch (Exception arg8) {
			throw new SystemException("Connection Rollback error", arg8);
		} finally {
			try {
				this.closeReally();
			} catch (Exception arg7) {
				;
			}

		}

	}

	public Statement createStatement() throws SQLException {
		Object statement = this.origConn.createStatement();


		return (Statement) statement;
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		PreparedStatement statement = this.origConn.prepareStatement(sql);
		return statement;
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		CallableStatement statement = this.origConn.prepareCall(sql);
		if (log.isDebugEnabled()) {
			log.debug("[prepareCall]" + sql);
		}

		return statement;
	}

	public String nativeSQL(String sql) throws SQLException {
		return this.origConn.nativeSQL(sql);
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		if (!this.isJta) {
			this.origConn.setAutoCommit(autoCommit);
		}

	}

	public boolean getAutoCommit() throws SQLException {
		return this.origConn.getAutoCommit();
	}

	public void commit() throws SQLException {
		if (!this.isJta) {
			this.origConn.commit();
		}

	}

	public void rollback() throws SQLException {
		if (!this.isJta) {
			this.origConn.rollback();
		}

	}

	public void closeReally() throws SQLException {
		this.close();
		if (this.origConn != null) {
			if (!this.origConn.isClosed()) {
				this.origConn.close();
			}

			this.origConn = null;
		}

	}

	public void close() throws SQLException {
	}

	public boolean isClosed() throws SQLException {
		return this.origConn != null ? this.origConn.isClosed() : true;
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		return this.origConn.getMetaData();
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		this.origConn.setReadOnly(readOnly);
	}

	public boolean isReadOnly() throws SQLException {
		return this.origConn.isReadOnly();
	}

	public void setCatalog(String catalog) throws SQLException {
		this.origConn.setCatalog(catalog);
	}

	public String getCatalog() throws SQLException {
		return this.origConn.getCatalog();
	}

	public void setTransactionIsolation(int level) throws SQLException {
		this.origConn.setTransactionIsolation(level);
	}

	public int getTransactionIsolation() throws SQLException {
		return this.origConn.getTransactionIsolation();
	}

	public SQLWarning getWarnings() throws SQLException {
		return this.origConn.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		this.origConn.clearWarnings();
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		Object statement = this.origConn.createStatement(resultSetType,
				resultSetConcurrency);


		return (Statement) statement;
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
											  int resultSetConcurrency) throws SQLException {
		Object statement = this.origConn.prepareStatement(sql, resultSetType,
				resultSetConcurrency);

		return (PreparedStatement) statement;
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
										 int resultSetConcurrency) throws SQLException {
		CallableStatement statement = this.origConn.prepareCall(sql,
				resultSetType, resultSetConcurrency);
		if (log.isDebugEnabled()) {
			log.debug("[preparedCall]" + sql);
		}

		return statement;
	}

	public Map getTypeMap() throws SQLException {
		return this.origConn.getTypeMap();
	}

	public void setTypeMap(Map map) throws SQLException {
		this.origConn.setTypeMap(map);
	}

	public void setHoldability(int holdability) throws SQLException {
		this.origConn.setHoldability(holdability);
	}

	public int getHoldability() throws SQLException {
		return this.origConn.getHoldability();
	}

	public Savepoint setSavepoint() throws SQLException {
		return this.origConn.setSavepoint();
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		return this.origConn.setSavepoint(name);
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		this.origConn.rollback(savepoint);
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		this.origConn.releaseSavepoint(savepoint);
	}

	public Statement createStatement(int resultSetType,
									 int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		Object statement = this.origConn.createStatement(resultSetType,
				resultSetConcurrency, resultSetHoldability);


		return (Statement) statement;
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
											  int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		Object statement = this.origConn.prepareStatement(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability);


		return (PreparedStatement) statement;
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
										 int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		CallableStatement statement = this.origConn.prepareCall(sql,
				resultSetType, resultSetConcurrency, resultSetHoldability);
		if (log.isDebugEnabled()) {
			log.debug("[prepareCall]" + sql);
		}
		return statement;
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		Object statement = this.origConn.prepareStatement(sql,
				autoGeneratedKeys);
		return (PreparedStatement) statement;
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		Object statement = this.origConn.prepareStatement(sql, columnIndexes);
		return (PreparedStatement) statement;
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		Object statement = this.origConn.prepareStatement(sql, columnNames);

		return (PreparedStatement) statement;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setClientInfo(String name, String value) throws SQLClientInfoException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSchema(String schema) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSchema() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void abort(Executor executor) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
}
