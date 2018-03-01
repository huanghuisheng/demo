package com.tone.dao;

import com.tone.exception.SystemException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class MybatisFactory {
	private static Map sessionFactorys = new HashMap();
	protected static Logger log = LoggerFactory.getLogger(MybatisFactory.class);
	public static SqlSession getSqlSession() {
		// TODO Auto-generated method stub
		return getThreadSession("framework");
	}

	@SuppressWarnings("unused")
	private static SqlSession getThreadSession(String p_sessionName) {
		Object threadId = ThreadLocalResourceManager.getThreadId();
		SqlSession connObj = (SqlSession) ThreadLocalResourceManager.getResource(threadId, "Mybatis" + p_sessionName);

		if (connObj == null) {
			ThreadLocalResourceManager.unbindResource(threadId, "Mybatis" + p_sessionName);
			if (connObj != null) {
				connObj.close();
			}
			SqlSessionFactory sqlSessionFactory = getSessionFactory(p_sessionName);
			Connection e = ConnectionFactory.getConnection(threadId);
			connObj = sqlSessionFactory.openSession(e);
			ThreadLocalResourceManager.bindResource(threadId, "Mybatis" + p_sessionName, connObj);
		}
		return connObj;

	}

	public static SqlSessionFactory getSessionFactory(String p_hibernateName) {
		SqlSessionFactory sf = (SqlSessionFactory) ((SqlSessionFactory) sessionFactorys.get(p_hibernateName));
		if (sf == null) {
			sf = buildSessionFactory(p_hibernateName);
			sessionFactorys.put(p_hibernateName, sf);
		}
		return sf;
	}

	private static SqlSessionFactory buildSessionFactory(String p_hibernateName) {
		SqlSessionFactory sessionFactory = null;
		String hibernateConfig = null;
		// hibernateConfig = ConfigFactory.getInstance().getConfig()
		// .getString(configPathSection, "file");
		// if (hibernateConfig == null) {
		// hibernateConfig =
		// "com/achievo/waf/hibernate/hibernate-mappings.cfg.xml";
		// }

		// configFile = Thread.currentThread().getContextClassLoader()
		// .getResource(hibernateConfig);
		// if (configFile == null) {
		// throw new SystemException("Don\'t found hibernate-mappings.cfg.xml");
		// } else {
		try {
			// 配置文件（SqlMapConfig.xml）
			String resource = "SqlMapConfig.xml";
			// 加载配置文件到输入 流
			InputStream inputStream = Resources.getResourceAsStream(resource);
			// 创建会话工厂
			sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		} catch (Exception arg7) {
			log.error(arg7.getMessage());
			throw new SystemException("could not load SqlMapConfig.xml",arg7);
		}
		return sessionFactory;
	}

}
