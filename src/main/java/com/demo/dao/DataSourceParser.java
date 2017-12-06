package com.demo.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


import org.apache.commons.beanutils.BeanUtils;
//import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.tonetime.commons.database.Configuration;
//import com.tonetime.commons.database.DataSourceParser;
//import com.tonetime.commons.database.DataSourceParser.HolderSingletonHolder;

/**
 * 数据源构建器.通过配置构建数据源
 * 
 * @author ybbk
 * @date 2010.07.21
 */
public class DataSourceParser {

	private static Logger logger = LoggerFactory.getLogger(DataSourceParser.class);

	/** DataSource列表在配置中的KEY */
	public static final String KEY_DATASOURCE_NAME = "dsList";

	/** 公用属性在配置中的Key */
	public static final String KEY_COMMON = "COMMON";
	public static final String DEFAULT_DATASOURCE = "org.apache.commons.dbcp.BasicDataSource";

	private static Map<String, DataSource> dsmaps = new LinkedHashMap<String, DataSource>();
	
	private Configuration configure = null;

	/**
	 * <p>重构数据源</p>
	 */
	public void reBuildDataSources(){
		dsmaps.clear();
		buildDataSources(configure, dsmaps);
	}

	private DataSourceParser() {
	}

	/**
	 * 根据配置构建数据源
	 */
	protected void buildDataSources(Configuration configure, Map<String, DataSource> dataSourceMap) {
		if(configure==null){
			logger.error("configure is null!");
			return;
		}
		this.configure = configure;

		Map<String, String> commonProperties = configure.getConfigMap(KEY_COMMON);
		String dataSources = configure.getConfigProperty(KEY_DATASOURCE_NAME);
		dataSources = dataSources.replaceAll(" ", ",");
		String[] dss = dataSources.split(",");
		for (String dataSourceName : dss) {
			dataSourceName = dataSourceName.trim();
			if (dataSourceName.length() == 0) {
				continue;
			}
			if (dataSourceName.equalsIgnoreCase(KEY_COMMON)) {
				logger.info("DataSource:[{}] have same name with COMMON CONFIG. ignore!", dataSourceName);
				continue;
			}
			Map<String, String> dataSourceProperties = configure.getConfigMap(dataSourceName);
			dataSourceProperties = configure.mergeConfig(dataSourceProperties, commonProperties);
			DataSource obj = buildDataSource(dataSourceName, dataSourceProperties);
			if(obj!=null){
				dataSourceMap.put(dataSourceName, obj);
			}
		}
	}

	/**
	 * 构建单个数据源
	 * 
	 * @param prop
	 * @param dataSource
	 * @return
	 */
	private DataSource buildDataSource(String dataSourceName, Map<String, String> dataSourceProperties) {
		String dataSourceType = dataSourceProperties.get("Type");
		if (dataSourceType == null || dataSourceType.trim().length() == 0) {
			dataSourceType = DEFAULT_DATASOURCE;
		}
		if (dataSourceType.equalsIgnoreCase("jndi")) {
			String jndiName = dataSourceProperties.get("JndiName");
			return buildJNDIDataSource(dataSourceName, jndiName);
		}
		try {
			@SuppressWarnings("rawtypes")
			Class clazz = Class.forName(dataSourceType);
			Object obj = clazz.newInstance();
			if (!(obj instanceof javax.sql.DataSource)) {
				return null;
			}

			for (Entry<String, String> entry : dataSourceProperties.entrySet()) {
				if (entry.getKey().equals("Type")) {
					continue;
				}
				try {
					BeanUtils.setProperty(obj, entry.getKey(), entry.getValue());
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			return testDataSource(dataSourceName, dataSourceProperties.get("url"), (DataSource)obj);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 构建JNDI数据源
	 * 
	 * @param prop
	 * @param dataSource
	 * @return
	 */
	private DataSource buildJNDIDataSource(String dataSourceName, String jndi) {
		try {
			Context ctx = new InitialContext();
			Object obj = ctx.lookup(jndi);
			if (obj instanceof DataSource) {
				return testDataSource(dataSourceName, jndi, (DataSource)obj);
			}
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * <p>获取数据源</p>
	 * 
	 * @param name
	 * @return
	 */
	public DataSource getDataSource(String name) {
		return dsmaps.get(name);
	}

	public DataSource getDataSource() {
		Set<Entry<String, DataSource>> entrySet = dsmaps.entrySet();
		for (Entry<String, DataSource> entry : entrySet) {
			return entry.getValue();
		}
		return null;
	}

	public static void main(String[] args) {
		String dataSource = "dbMaster";
		String dbMaster = "dbMasterdriverClassName";
		String key = dbMaster.substring(dataSource.length());
		System.out.println(key);
	}

	////////////////////////////////////////////////////////////
	private static class HolderSingletonHolder {
		static DataSourceParser instance = new DataSourceParser();
	}
	public static DataSourceParser getInstance() {
		return HolderSingletonHolder.instance;
	}

	public static DataSourceParser builder(Configuration configure){
		DataSourceParser instance = getInstance();
		synchronized(instance){
			instance.buildDataSources(configure, dsmaps);
		}
		return instance;
	}
	/**
	 * 数据源测试
	 * @param dataSource
	 */
	private DataSource testDataSource(String dataSourceName, String url, DataSource dataSource) {
		if( dataSource != null ){
			try{
				logger.info("DataSource Test... Name:[{}] URL:[{}] Result:[{}]", new String[]{dataSourceName, dataSource.getConnection().getMetaData().getURL(), "OK"});
			}catch(SQLException e){
				logger.error("DataSource Test... Name:[{}] URL:[{}]  Result:[{}]", new String[]{dataSourceName, url, formatMsg(e.getMessage())});
			}
		}else{
			logger.error("DataSource Test... Name:[{}] URL:[{}]  Result:[NULL]", dataSourceName, url);
		}
		return dataSource;
	}
	
	private String formatMsg(String msg){
		if(msg!=null){
			msg = msg.replace('\r', ' ').replace('\n', ' ');
		}
		return msg;
	}
}
