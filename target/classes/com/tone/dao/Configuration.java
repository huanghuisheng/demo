package com.tone.dao;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tone.util.ClassLoaderUtils;


/**
 * <p>数据源配置读取器</p>
 * 
 * @author ybbk
 * @date 2010-11-10 上午10:26:39
 * @since 1.3.0
 */
public class Configuration {
	private static final Logger logger = LoggerFactory.getLogger(Configuration.class);
	private static final String DATABASE_CONFIG_FILE = "solar_database.properties";

	private Properties properties = null;

	public Configuration(){
		this(DATABASE_CONFIG_FILE);
	}
	public Configuration(String configureFile){
		URL configureUrl  = ClassLoaderUtils.getResource(configureFile, Configuration.class);
		loadProperties(configureUrl);
	}
	public Configuration(URL configureUrl){
		loadProperties(configureUrl);
	}

	protected void loadProperties(URL configureUrl){
		try {
			if(configureUrl==null){
				throw new IllegalArgumentException("请指定文件!");
			}
			Properties prop = new Properties();
			prop.load(configureUrl.openStream());
			this.properties = prop;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	public Properties getProperties() {
		return properties;
	}
	
	
	protected String getConfigProperty(String key) {
		String result = properties.getProperty(key);
		if (result == null)
			result = "";
		return result;
	}
	protected Map<String, String> getConfigMap(String prefix) {
		Map<String, String> tmp = new HashMap<String, String>();
		Set<Entry<Object, Object>> entrySet = properties.entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			String key = entry.getKey().toString().trim();
			if (!key.startsWith(prefix.concat("."))) {
				continue;
			}
			key = key.substring(prefix.length() + 1);
			tmp.put(key, (String) entry.getValue());
		}
		return tmp;
	}

	protected Map<String, String> mergeConfig(Map<String, String> destMap, Map<String, String> srcMap) {
		for (Entry<String, String> entry : srcMap.entrySet()) {
			if (!destMap.containsKey(entry.getKey())) {
				destMap.put(entry.getKey(), entry.getValue());
			}
		}
		return destMap;
	}
}