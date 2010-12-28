package com.tone.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.XADataSource;

import org.omg.CORBA.SystemException;

import com.tone.constant.ConfigFactory;



public class ConnectionFactory {
	private static Map dataSourceList = new HashMap();
	private static String dataSource;
	
	private static synchronized Object getSynchronizedDataSource(String jndiName) throws NamingException {
		Object ds = dataSourceList.get(jndiName);
		if (ds == null) {
			ds = DataSourceBuilder.getInstance().getDataSource(jndiName);
			dataSourceList.put(jndiName, ds);
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
	
	
	

	private static Connection wrappedConnection(Object threadId, String jndiName) {
		WrappedConnection connObj = (WrappedConnection) ThreadLocalResourceManager.getResource(threadId, jndiName);
		try {
			if (connObj == null || connObj.isClosed()) {
				ThreadLocalResourceManager.unbindResource(threadId, jndiName);
				Connection e = null;
				e = getInnerConnection(jndiName);
				connObj = new WrappedConnection(e);
				connObj.setJta(false);
				ThreadLocalResourceManager.bindResource(threadId, jndiName, connObj);
			}
		} catch (SQLException arg3) {
			arg3.printStackTrace();
		}
		return connObj;		
	}

	private static Connection getInnerConnection(String jndiName) {
		if (jndiName == null || jndiName.length() == 0) {
			jndiName = getDataSourceFromConfig(null);
		}
		Connection conn = null;
		Object ds = getDataSource(jndiName);
		if (ds == null) {
			throw new IllegalArgumentException("Don\'t found DataSource JNDI name (" + jndiName + ")");
		} else {
			try {
				if (ds instanceof DataSource) {
					conn = ((DataSource) ds).getConnection();
				} else if (ds instanceof XADataSource) {
					conn = ((XADataSource) ds).getXAConnection().getConnection();
				}
				// conn=jdbc.getConnection();
				if (conn == null) {
					throw new IllegalArgumentException("Cannot create the connection by DataSource (" + jndiName + ")");
				} else {
					return conn;
				}
			} catch (SQLException arg3) {
				arg3.printStackTrace();
			}
		}
		return conn;
	}

	public static Object getDataSource(String jndiName) {
		// if (jndiName != null && jndiName.length() != 0) {
		Object ds = null;
		try {
			ds = dataSourceList.get(jndiName);
			if (ds == null) {
				ds = getSynchronizedDataSource(jndiName);
			}
		} catch (SystemException arg2) {
			throw arg2;
		} catch (Exception arg3) {
			arg3.printStackTrace();
		}
		return ds;
		// } else {
		// throw new SystemException("JNDI name is required");
		// }
	}

}
