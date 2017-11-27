package com.demo.dao;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.bdcc.waf.common.Closeable;
//import com.bdcc.waf.common.Rollbackable;
//import com.bdcc.waf.db.WrappedConnection;
//import com.bdcc.waf.db.WrappedPreparedStatement;
//import com.bdcc.waf.db.WrappedStatement;
//import com.bdcc.waf.exception.SystemException;
//import com.bdcc.waf.logging.Logger;
//import com.bdcc.waf.logging.LoggerFactory;

public class WrappedConnection implements Connection, Closeable, Rollbackable {
	protected Connection origConn;
	protected boolean isJta = false;
	private static Logger log = LoggerFactory.getLogger(WrappedConnection.class);

	public WrappedConnection(Connection conn) throws SQLException {
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

//			throw new SystemException("Connection commit error", arg11);
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
//			throw new SystemException("Connection Rollback error", arg8);
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
		if (log.isDebugEnabled()) {
			WrappedStatement wrappedstatement = new WrappedStatement();
			wrappedstatement.setStatement((Statement) statement);
			statement = wrappedstatement;
		}

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
		if (log.isDebugEnabled()) {
			WrappedStatement wrappedstatement = new WrappedStatement();
			wrappedstatement.setStatement((Statement) statement);
			statement = wrappedstatement;
		}

		return (Statement) statement;
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		Object statement = this.origConn.prepareStatement(sql, resultSetType,
				resultSetConcurrency);
		if (log.isDebugEnabled()) {
			WrappedPreparedStatement wrappedstatement = new WrappedPreparedStatement();
			wrappedstatement.setStatement((PreparedStatement) statement);
			wrappedstatement.setSqlString(sql);
			statement = wrappedstatement;
		}

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
		if (log.isDebugEnabled()) {
			WrappedStatement wrappedstatement = new WrappedStatement();
			wrappedstatement.setStatement((Statement) statement);
			statement = wrappedstatement;
		}

		return (Statement) statement;
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		Object statement = this.origConn.prepareStatement(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability);
		if (log.isDebugEnabled()) {
			WrappedPreparedStatement wrappedstatement = new WrappedPreparedStatement();
			wrappedstatement.setStatement((PreparedStatement) statement);
			wrappedstatement.setSqlString(sql);
			statement = wrappedstatement;
		}

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
		if (log.isDebugEnabled()) {
			WrappedPreparedStatement wrappedstatement = new WrappedPreparedStatement();
			wrappedstatement.setStatement((PreparedStatement) statement);
			wrappedstatement.setSqlString(sql);
			statement = wrappedstatement;
		}

		return (PreparedStatement) statement;
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		Object statement = this.origConn.prepareStatement(sql, columnIndexes);
		if (log.isDebugEnabled()) {
			WrappedPreparedStatement wrappedstatement = new WrappedPreparedStatement();
			wrappedstatement.setStatement((PreparedStatement) statement);
			wrappedstatement.setSqlString(sql);
			statement = wrappedstatement;
		}

		return (PreparedStatement) statement;
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		Object statement = this.origConn.prepareStatement(sql, columnNames);
		if (log.isDebugEnabled()) {
			WrappedPreparedStatement wrappedstatement = new WrappedPreparedStatement();
			wrappedstatement.setStatement((PreparedStatement) statement);
			wrappedstatement.setSqlString(sql);
			statement = wrappedstatement;
		}

		return (PreparedStatement) statement;
	}

	public <T> T unwrap(Class<T> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public SQLXML createSQLXML() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isValid(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public void setClientInfo(String arg0, String arg1) throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}

	public void setClientInfo(Properties arg0) throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}

	public String getClientInfo(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Properties getClientInfo() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Array createArrayOf(String arg0, Object[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Struct createStruct(String arg0, Object[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSchema(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public String getSchema() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void abort(Executor arg0) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNetworkTimeout(Executor arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public int getNetworkTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
}
