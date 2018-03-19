package com.tone.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.XADataSource;

import com.tone.constant.ConfigFactory;
import com.tone.exception.SystemException;


public class ConnectionFactory {
	private static Map dataSourceList = new HashMap();
	private static String dataSource;
	
	private static synchronized Object getSynchronizedDataSource(String dbMaster) throws NamingException {
		Object ds = dataSourceList.get(dbMaster);
		if (ds == null) {
			ds = DataSourceBuilder.getInstance().getDataSource(dbMaster);
			dataSourceList.put(dbMaster, ds);
		}
		return ds;
	}

	public static Connection getConnection() throws SQLException {

		Object txnKey1 = ThreadLocalResourceManager.getThreadId();
		return wrappedConnection(txnKey1, getDataSourceFromConfig());
		
	}
	public static Connection getConnection(String dbMaster) throws SQLException {

		Object txnKey1 = ThreadLocalResourceManager.getThreadId();
		return wrappedConnection(txnKey1, getDataSourceFromConfig(dbMaster));
		
	}
	
	

	public static Connection getConnection(Object threadId) {
		return wrappedConnection(threadId, getDataSourceFromConfig());
	}

	private static String getDataSourceFromConfig() {
		if (dataSource == null) {
//			dataSource = "jdbc/datasource";
			dataSource=ConfigFactory.getInstance().getConfig().getString("dsList", "getMaster");
		}
		return dataSource;
	}
	private static String getDataSourceFromConfig(String dbMaster) {
		String dataMasterSource=null;
			if(dbMaster==null)
			{
				dataMasterSource=getDataSourceFromConfig();
			}else{
				dataMasterSource=dbMaster;
			}

		return dataMasterSource;
	}
	
	
	

	private static Connection wrappedConnection(Object threadId, String dbMaster) {
		WrappedConnection connObj = (WrappedConnection) ThreadLocalResourceManager.getResource(threadId, dbMaster);
		try {
			if (connObj == null || connObj.isClosed()) {
				ThreadLocalResourceManager.unbindResource(threadId, dbMaster);
				Connection e = null;
				e = getInnerConnection(dbMaster);
				connObj = new WrappedConnection(e);
				connObj.setJta(false);
				ThreadLocalResourceManager.bindResource(threadId, dbMaster, connObj);
			}
		} catch (SQLException arg3) {
			arg3.printStackTrace();
			throw new SystemException(
					"Cannot create the connection by DataSource ("
							+ dbMaster + ")", arg3);
		}
		return connObj;		
	}

	private static Connection getInnerConnection(String dbMaster) {
		if (dbMaster == null || dbMaster.length() == 0) {
			dbMaster = getDataSourceFromConfig(null);
		}
		Connection conn = null;
		Object ds = getDataSource(dbMaster);
		if (ds == null) {
			throw new IllegalArgumentException("Don\'t found DataSource  name (" + dbMaster + ")");
		} else {
			try {
				if (ds instanceof DataSource) {
					conn = ((DataSource) ds).getConnection();
				} else if (ds instanceof XADataSource) {
					conn = ((XADataSource) ds).getXAConnection().getConnection();
				}
				// conn=jdbc.getConnection();
				if (conn == null) {
					throw new IllegalArgumentException("Cannot create the connection by DataSource (" + dbMaster + ")");
				} else {
					return conn;
				}
			} catch (SQLException arg3) {
				arg3.printStackTrace();
				throw new SystemException(
						"Cannot create the connection by DataSource ("
								+ dbMaster + ")", arg3);
			}
		}
	}

	public static Object getDataSource(String dbMaster) {
		Object ds = null;
		try {
			ds = dataSourceList.get(dbMaster);
			if (ds == null) {
				ds = getSynchronizedDataSource(dbMaster);
			}
			return ds;
		} catch (SystemException arg2) {
			throw arg2;
		} catch (Exception arg3) {
			arg3.printStackTrace();
			throw new SystemException("Don\'t found DataSource  name ("
					+ dbMaster + ")", arg3);
		}
	}

}
