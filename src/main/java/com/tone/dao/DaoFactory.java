package com.tone.dao;

import java.util.HashMap;
import java.util.Map;



public class DaoFactory {
	private static Map daoList = new HashMap(200);
	private static final String JAVABEAN_SUFFIX = "Impl";
	private static final String ORACL_JAVABEAN_SUFFIX = "QracDAOImpl";
	private static final String DB2_JAVABEAN_SUFFIX = "DB2DAOImpl";
	private static final String SQL_JAVABEAN_SUFFIX = "SQLDAOImpl";
	private static final String DAO = "DAO";
	private static final String ORACLE_TYPE = "Oracle";
	private static final String SQLSERVER_TYPE = "mssql";
	private static final String DB2_TYPE = "DB2";
	private static final String COMMONDAO = "com.bdcc.waf.dao.CommonDAO";
	private static final int ORACLE_IMPL = 1521;
	private static final int SQLSERVER_IMPL = 1433;
	private static final int DB2_IMPL = 50000;

	public static Object getDao(Class daoClass) {
		if (daoClass == null) {
			return null;
		} else {
			Object dao = daoList.get(daoClass);
			if (dao == null) {
				dao = getSynchronizedDao(daoClass);
			}

			return dao;
		}
	}

	private static synchronized Object getSynchronizedDao(Class daoClass) {
		Object dao = daoList.get(daoClass);
		if (dao == null) {
			dao = getJavaDao(daoClass);
			daoList.put(daoClass, dao);
		}

		return dao;
	}

	private static Object getJavaDao(Class dao){
//		return ClassUtils.classInstance(getImplementClassName(dao.getName()));
	    try {
			return Class.forName(getImplementClassName(dao.getName())).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static String getImplementClassName(String daoInterface) {
		if ("com.bdcc.waf.dao.CommonDAO".equalsIgnoreCase(daoInterface)) {
			return daoInterface + "Impl";
		} else {
			int implFlag = getWhatImpl();
			String daoInterfaceName = getNameExcludeDao(daoInterface);
			return 1521 == implFlag
					? daoInterfaceName + "QracDAOImpl"
					: ('썐' == implFlag
							? daoInterfaceName + "DB2DAOImpl"
							: (1433 == implFlag ? daoInterfaceName
									+ "SQLDAOImpl" : daoInterfaceName + "Impl"));
		}
	}

	private static String getNameExcludeDao(String name) {
		String result = "";
		int length = name.length() - "DAO".length();
		result = name.substring(0, length);
		return result;
	}

	private static int getWhatImpl() {
//		String dbType = ConfigFactory.getInstance().getConfig()
//				.getString("framework", "dbtype");
		String dbType="mysql";
		return "Oracle".equalsIgnoreCase(dbType) ? 1521 : ("DB2"
				.equalsIgnoreCase(dbType) ? '썐' : ("mssql"
				.equalsIgnoreCase(dbType) ? 1433 : 1521));
	}
}
