package com.demo.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.XADataSource;

import org.omg.CORBA.SystemException;

//import com.bdcc.waf.db.ConnectionBroker;
//import com.bdcc.waf.db.WrappedConnection;
//import com.bdcc.waf.jdbc.jdbc;
//import com.bdcc.waf.resource.ThreadLocalResourceManager;
//import com.bdcc.waf.resource.TransactionManagerFactory;



public class ConnectionFactory 
{
	private static Map dataSourceList = new HashMap();
	private static String controllerType;
	private static String dataSource;
//	public static DataSourceBuilder builder = null;
//    static{
//        builder = DataSourceBuilder.getInstance();
//    }
	
	
	
	
	
	private static synchronized Object getSynchronizedDataSource(String jndiName)
			throws NamingException {
		Object ds = dataSourceList.get(jndiName);
		if (ds == null) {
//			ds = JndiUtils.lookup(jndiName);
			ds=DataSourceBuilder.getInstance().getDataSourceCluster().getReadableDataSource();
			
//			if (!(ds instanceof DataSource) && !(ds instanceof XADataSource)) {
//				throw new SystemException(
//						"Resource should be DataSource or XADataSource type ("
//								+ jndiName + ")");
//			}

			dataSourceList.put(jndiName, ds);
		}

		return ds;
	}
	
	public static Connection getConnection() throws SQLException {
//		if ("ejb".equalsIgnoreCase(getControllerTypeFromConfig())
//				&& !isTestMode()) {
//			Transaction txnKey = TransactionManagerFactory.getTransaction();
//			if (txnKey != null) {
//				if (log.isDebugEnabled()) {
//					log.debug("[getConnection with transaction]" + txnKey);
//				}
//
//				return wrappedJtaConnection(txnKey, getDataSourceFromConfig());
//			}
//		}

		Object txnKey1 = ThreadLocalResourceManager.getThreadId();
		return wrappedConnection(txnKey1, getDataSourceFromConfig());
//		return	jdbc.getConnection();
	}
	
	public static Connection getConnection(Object threadId) {
		return wrappedConnection(threadId, getDataSourceFromConfig());
	}
	
	
	private static String getDataSourceFromConfig() {
//		if (!isInit) {
//			init();
//		}
		if (dataSource == null) {
			dataSource = "jdbc/datasource";
		}
		return dataSource;
	}
	
	private static Connection wrappedConnection(Object threadId, String jndiName) {
//		if (jndiName != null && jndiName.length() != 0) {
			WrappedConnection connObj = (WrappedConnection) ThreadLocalResourceManager
					.getResource(threadId, jndiName);

			try {
				if (connObj == null || connObj.isClosed()) {
					ThreadLocalResourceManager.unbindResource(threadId,
							jndiName);
					Connection e = null;
//					if (isTestMode()) {
//						e = ConnectionBroker.getInstance().getConnection();
//					} else {
						e = getInnerConnection(jndiName);
//					}
//					e=jdbc.getConnection();

					connObj = new WrappedConnection(e);
					connObj.setJta(false);
					ThreadLocalResourceManager.bindResource(threadId, jndiName,
							connObj);
				}

				return connObj;
			} catch (SQLException arg3) {
//				arg3.printStackTrace();
//				throw new SystemException(
//						"Cannot create the connection by DataSource ("
//								+ jndiName + ")", arg3);
				return null;
			}
//		} else {
//			throw new IllegalArgumentException("JNDI name is required");
//		}
	}
	
	private static Connection getInnerConnection(String jndiName) {
		if (jndiName == null || jndiName.length() == 0) {
			jndiName = getDataSourceFromConfig();
		}

		Connection conn = null;
		Object ds = getDataSource(jndiName);
		if (ds == null) {
			throw new IllegalArgumentException(
					"Don\'t found DataSource JNDI name (" + jndiName + ")");
		} else {
			try {
				if (ds instanceof DataSource) {
					conn = ((DataSource) ds).getConnection();
				} else if (ds instanceof XADataSource) {
					conn = ((XADataSource) ds).getXAConnection()
							.getConnection();
				}
//				conn=jdbc.getConnection();
				if (conn == null) {
					throw new IllegalArgumentException(
							"Cannot create the connection by DataSource ("
									+ jndiName + ")");
				} else {
					return conn;
				}
			} catch (SQLException arg3) {
//				throw new SystemException(
//						"Cannot create the connection by DataSource ("
//								+ jndiName + ")", arg3);
			}
		}
		return conn;
	}
	public static Object getDataSource(String jndiName) {
//		if (jndiName != null && jndiName.length() != 0) {
			Object ds = null;
			try {
				ds = dataSourceList.get(jndiName);
				if (ds == null) {
					ds = getSynchronizedDataSource(jndiName);
				}
				return ds;
			} catch (SystemException arg2) {
				throw arg2;
			} catch (Exception arg3) {
				arg3.printStackTrace();
//				throw new SystemException("Don\'t found DataSource JNDI name ("
//						+ jndiName + ")", arg3);
				arg3.printStackTrace();
				return null;
			}
//		} else {
//			throw new SystemException("JNDI name is required");
//		}
	}
	
	
	
}
