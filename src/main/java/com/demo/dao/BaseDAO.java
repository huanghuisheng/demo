package com.demo.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bdcc.waf.db.SqlSyntaxAdapter;
import com.bdcc.waf.db.SqlSyntaxAdapterFactory;


public class BaseDAO {
	private static Map cloneTable = new HashMap();
	private static BaseDAO singleInstance = new BaseDAO();
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	public static String sql_type = "mysql";


	public static BaseDAO getInstance() {
		return singleInstance;
	}

	protected Connection getConnection() throws Exception {
		return ConnectionFactory.getConnection();
	}

//	protected Session getSession() {
//		return HibernateFactory.getSession();
//	}
	protected SqlSession getSession() {
	return MybatisFactory.getSqlSession();
}
		
//	增删改查

	public Object create(Object entity) {
//		this.log.enterMethod();
		Serializable retu = null;
		try {
			String nameSpaceId=entity.getClass().getName()+"Mapper."+"insert";
			SqlSession session=	this.getSession();
			retu=session.insert(nameSpaceId,entity);
			
//			Object threadId = ThreadLocalResourceManager.getThreadId();
//			Connection e = ConnectionFactory.getConnection(threadId);
//			e.commit();
			
			session.close();
		} catch (Exception arg3) {
//			DaoExceptionHandler.exceptionHandler(arg3);
//			throw new SystemException("Create Fail.", arg3);
			arg3.printStackTrace();
		}
//		this.log.exitMethod();
		return retu;
	}
	
	
	public static void main(String[] args) throws Exception {
		BaseDAO a=new BaseDAO();
		SmsMember b =new SmsMember();
		b.setId(Long.valueOf("111"));
		b.setUserId(Long.valueOf("1112222"));
		a.create(b);
		
//		Connection con=	a.getConnection();
//		System.out.println(con.getClientInfo().toString());
	}
	
	
}
	
