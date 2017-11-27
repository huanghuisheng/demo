package com.demo.dao;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
//import org.hibernate.HibernateException;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.cfg.Configuration;


import org.apache.ibatis.session.SqlSessionFactoryBuilder;





public class MybatisFactory {
	private static Map sessionFactorys = new HashMap();
	
	
	
	public static SqlSession getSqlSession() {
		// TODO Auto-generated method stub
		return getThreadSession("framework");
	}
	
	
	private static SqlSession getThreadSession(String p_sessionName) {
			Object threadId = ThreadLocalResourceManager.getThreadId();
			SqlSession connObj = (SqlSession) ThreadLocalResourceManager.getResource(threadId, "Mybatis" + p_sessionName);
			
			if (connObj == null) {
				ThreadLocalResourceManager.unbindResource(threadId, "Mybatis"+ p_sessionName);
//				if (connObj != null) {
//					connObj.objectClose();
//				}
				SqlSessionFactory sqlSessionFactory = getSessionFactory(p_sessionName);
				Connection e = ConnectionFactory.getConnection(threadId);
//				org.hibernate.classic.Session session1 = session2
//						.openSession(e);
//				connObj = new HibernateSessionWrapper3();
//				connObj.setOriginalSession(session1);
				connObj=sqlSessionFactory.openSession(e);
				ThreadLocalResourceManager.bindResource(threadId, "Mybatis"
						+ p_sessionName, connObj);
			}
//			} else if (!connObj.getWrappedSession().isConnected()) {
//				SqlSession session = connObj;
//
//				try {
//					session      (ConnectionFactory.getConnection(threadId));
//				} catch (Exception arg5) {
////					throw new SystemException(
////							"Hebernate session reconnect fail", arg5);
//				}
//			}
			return connObj;
		
	}
	
	public static SqlSessionFactory getSessionFactory(String p_hibernateName) {
		SqlSessionFactory sf = (SqlSessionFactory) ((SqlSessionFactory) sessionFactorys
				.get(p_hibernateName));
		if (sf == null) {
			sf = buildSessionFactory(p_hibernateName);
			sessionFactorys.put(p_hibernateName, sf);
		}
		return sf;
	}

	private static SqlSessionFactory buildSessionFactory(String p_hibernateName) {
//		String configSection = "ormapping.hibernate." + p_hibernateName;
//		String configPathSection = "ormapping.hibernate.config."
//				+ p_hibernateName;
//		URL configFile = null;
		SqlSessionFactory sessionFactory = null;
		String hibernateConfig = null;
//		hibernateConfig = ConfigFactory.getInstance().getConfig()
//				.getString(configPathSection, "file");
//		if (hibernateConfig == null) {
//			hibernateConfig = "com/achievo/waf/hibernate/hibernate-mappings.cfg.xml";
//		}

//		configFile = Thread.currentThread().getContextClassLoader()
//				.getResource(hibernateConfig);
//		if (configFile == null) {
//			throw new SystemException("Don\'t found hibernate-mappings.cfg.xml");
//		} else {
			try {
//				Configuration e = new Configuration();
//				Properties prop = ConfigFactory.getInstance().getConfig()
//						.getSectionAsProperties(configSection);
//				e.addProperties(prop);
//				e.configure(configFile);
//				sessionFactory = e.buildSessionFactory();
//				return sessionFactory;
				
				
				// 配置文件（SqlMapConfig.xml）
				String resource = "SqlMapConfig.xml";
				// 加载配置文件到输入 流
				InputStream inputStream = Resources.getResourceAsStream(resource);
				// 创建会话工厂
				sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
				return sessionFactory;
				
			} catch (Exception arg7) {
//				throw new SystemException("Could not build SessionFactory",
//						arg7);
			}
//		}
			return sessionFactory;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
