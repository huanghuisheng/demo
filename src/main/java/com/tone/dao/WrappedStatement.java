package com.tone.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.bdcc.waf.db.WrappedStatement;
//import com.bdcc.waf.logging.Logger;
//import com.bdcc.waf.logging.LoggerFactory;

public class WrappedStatement implements Statement {
	protected Statement origStatement;
	protected String sqlString = null;
	private static Logger log = LoggerFactory.getLogger(WrappedStatement.class);

	public void setStatement(Statement statement) {
		this.origStatement = statement;
	}

	protected Statement getStatement() {
		return this.origStatement;
	}

	protected void setSqlString(String sql) {
		this.sqlString = sql;
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		if (this.getStatement() != null) {
			if (log.isDebugEnabled()) {
				long rs2 = System.currentTimeMillis();
				ResultSet rs1 = this.getStatement().executeQuery(sql);
				log.debug("[executeQuery]<"
						+ (System.currentTimeMillis() - rs2) + ">" + sql);
				return rs1;
			} else {
				ResultSet rs = this.getStatement().executeQuery(sql);
				return rs;
			}
		} else {
			return null;
		}
	}

	public int executeUpdate(String sql) throws SQLException {
		if (this.getStatement() != null) {
			if (log.isDebugEnabled()) {
				long tt1 = System.currentTimeMillis();
				int resu = this.getStatement().executeUpdate(sql);
				log.debug("[executeUpdate]<"
						+ (System.currentTimeMillis() - tt1) + ">" + sql);
				return resu;
			} else {
				return this.getStatement().executeUpdate(sql);
			}
		} else {
			return 0;
		}
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

	public int getMaxFieldSize() throws SQLException {
		return this.getStatement() == null ? 0 : this.getStatement()
				.getMaxFieldSize();
	}

	public void setMaxFieldSize(int max) throws SQLException {
		this.getStatement().setMaxFieldSize(max);
	}

	public int getMaxRows() throws SQLException {
		return this.getStatement() == null ? 0 : this.getStatement()
				.getMaxRows();
	}

	public void setMaxRows(int max) throws SQLException {
		this.getStatement().setMaxRows(max);
	}

	public void setEscapeProcessing(boolean enable) throws SQLException {
		this.getStatement().setEscapeProcessing(enable);
	}

	public int getQueryTimeout() throws SQLException {
		return this.getStatement() == null ? 0 : this.getStatement()
				.getQueryTimeout();
	}

	public void setQueryTimeout(int seconds) throws SQLException {
		this.getStatement().setQueryTimeout(seconds);
	}

	public void cancel() throws SQLException {
		this.getStatement().cancel();
	}

	public SQLWarning getWarnings() throws SQLException {
		return this.getStatement().getWarnings();
	}

	public void clearWarnings() throws SQLException {
		this.getStatement().clearWarnings();
	}

	public void setCursorName(String name) throws SQLException {
		this.getStatement().setCursorName(name);
	}

	public boolean execute(String sql) throws SQLException {
		if (this.getStatement() != null) {
			if (log.isDebugEnabled()) {
				long tt1 = System.currentTimeMillis();
				boolean result = this.getStatement().execute(sql);
				log.debug("[execute]<" + (System.currentTimeMillis() - tt1)
						+ ">" + sql);
				return result;
			} else {
				return this.getStatement().execute(sql);
			}
		} else {
			return false;
		}
	}

	public ResultSet getResultSet() throws SQLException {
		ResultSet rs = this.getStatement().getResultSet();
		return rs;
	}

	public int getUpdateCount() throws SQLException {
		return this.getStatement() == null ? -1 : this.getStatement()
				.getUpdateCount();
	}

	public boolean getMoreResults() throws SQLException {
		return this.getStatement().getMoreResults();
	}

	public void setFetchDirection(int direction) throws SQLException {
		this.getStatement().setFetchDirection(direction);
	}

	public int getFetchDirection() throws SQLException {
		return this.getStatement().getFetchDirection();
	}

	public void setFetchSize(int rows) throws SQLException {
		this.getStatement().setFetchSize(rows);
	}

	public int getFetchSize() throws SQLException {
		return this.getStatement().getFetchSize();
	}

	public int getResultSetConcurrency() throws SQLException {
		return this.getStatement().getResultSetConcurrency();
	}

	public int getResultSetType() throws SQLException {
		return this.getStatement().getResultSetType();
	}

	public void addBatch(String sql) throws SQLException {
		this.getStatement().addBatch(sql);
	}

	public void clearBatch() throws SQLException {
		this.getStatement().clearBatch();
	}

	public int[] executeBatch() throws SQLException {
		if (log.isDebugEnabled()) {
			long tt1 = System.currentTimeMillis();
			int[] result = this.getStatement().executeBatch();
			log.debug("[executeBatch]<" + (System.currentTimeMillis() - tt1)
					+ ">" + this.sqlString);
			return result;
		} else {
			return this.getStatement().executeBatch();
		}
	}

	public boolean getMoreResults(int current) throws SQLException {
		return this.getStatement().getMoreResults(current);
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		return this.getStatement().getGeneratedKeys();
	}

	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		if (log.isDebugEnabled()) {
			long tt1 = System.currentTimeMillis();
			int result = this.getStatement().executeUpdate(sql,
					autoGeneratedKeys);
			log.debug("[executeUpdate]<" + (System.currentTimeMillis() - tt1)
					+ ">" + sql);
			return result;
		} else {
			return this.getStatement().executeUpdate(sql, autoGeneratedKeys);
		}
	}

	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		if (log.isDebugEnabled()) {
			long tt1 = System.currentTimeMillis();
			int result = this.getStatement().executeUpdate(sql, columnIndexes);
			log.debug("[executeUpdate]<" + (System.currentTimeMillis() - tt1)
					+ ">" + sql);
			return result;
		} else {
			return this.getStatement().executeUpdate(sql, columnIndexes);
		}
	}

	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		if (log.isDebugEnabled()) {
			long tt1 = System.currentTimeMillis();
			int result = this.getStatement().executeUpdate(sql, columnNames);
			log.debug("[executeUpdate]<" + (System.currentTimeMillis() - tt1)
					+ ">" + sql);
			return result;
		} else {
			return this.getStatement().executeUpdate(sql, columnNames);
		}
	}

	public boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {
		if (log.isDebugEnabled()) {
			long tt1 = System.currentTimeMillis();
			boolean result = this.getStatement()
					.execute(sql, autoGeneratedKeys);
			log.debug("[execute]<" + (System.currentTimeMillis() - tt1) + ">"
					+ sql);
			return result;
		} else {
			return this.getStatement().execute(sql, autoGeneratedKeys);
		}
	}

	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		if (log.isDebugEnabled()) {
			long tt1 = System.currentTimeMillis();
			boolean result = this.getStatement().execute(sql, columnIndexes);
			log.debug("[execute]<" + (System.currentTimeMillis() - tt1) + ">"
					+ sql);
			return result;
		} else {
			return this.getStatement().execute(sql, columnIndexes);
		}
	}

	public boolean execute(String sql, String[] columnNames)
			throws SQLException {
		if (log.isDebugEnabled()) {
			long tt1 = System.currentTimeMillis();
			boolean result = this.getStatement().execute(sql, columnNames);
			log.debug("[execute]<" + (System.currentTimeMillis() - tt1) + ">"
					+ sql);
			return result;
		} else {
			return this.getStatement().execute(sql, columnNames);
		}
	}

	public int getResultSetHoldability() throws SQLException {
		return this.getStatement().getResultSetHoldability();
	}

	public Connection getConnection() throws SQLException {
		return this.getStatement().getConnection();
	}

	protected void finalize() throws Throwable {
		super.finalize();
		this.close();
	}

	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public <T> T unwrap(Class<T> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void closeOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public boolean isCloseOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public void setPoolable(boolean arg0) throws SQLException {
		// TODO Auto-generated method stub
		
	}
}
