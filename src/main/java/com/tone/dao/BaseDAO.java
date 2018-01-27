package com.tone.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tone.interfaces.DBResultSetProcessor;
import com.tone.util.Pagin;
import com.tone.util.StringUtils;
import com.tone.util.SystemException;

public class BaseDAO <E> {
	private static BaseDAO singleInstance = new BaseDAO();
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	public static String sql_type = "mysql";
	private static String dbType = "MySQL";

	public static BaseDAO getInstance() {
		return singleInstance;
	}

	protected Connection getConnection(String dbMaster) throws Exception {
		if(dbMaster==null||("").equals(dbMaster))
		{
			return ConnectionFactory.getConnection();	
		}else{
			return ConnectionFactory.getConnection(dbMaster);
		}
	}

	protected SqlSession getSession() {
		return MybatisFactory.getSqlSession();
	}

	// 增删改查
	public Object create(Object entity) {
		// this.log.enterMethod();
		Serializable retu = null;
		try {
			String nameSpaceId = entity.getClass().getName() + "Mapper." + "insert";
			SqlSession session = this.getSession();
			retu = session.insert(nameSpaceId, entity);
//			Object threadId = ThreadLocalResourceManager.getThreadId();
//			Connection e = ConnectionFactory.getConnection(threadId);
//			e.commit();
		} catch (Exception arg3) {
			 throw new SystemException("Create Fail.", arg3);
		}
		// this.log.exitMethod();
		return retu;
	}

	//批量插入
	public Object createByBatch(List<E> entity) {
		// this.log.enterMethod();
		Serializable retu = null;
		try {
			if(entity!=null&&entity.size()>0)
			{
				Object object=entity.get(0);
				String nameSpaceId = object.getClass().getName() + "Mapper." + "insertByBatch";
				SqlSession session = this.getSession();
				retu = session.insert(nameSpaceId, entity);	
			}
		} catch (Exception arg3) {
			 throw new SystemException("Create Fail.", arg3);
		}
		// this.log.exitMethod();
		return retu;
	}
	
	
	public void update(Object entity) {
		try {
			if (entity == null) {
				if (this.log.isDebugEnabled()) {
					this.log.debug("Object is empty during updating !");
				}
				return;
			}
			String nameSpaceId = entity.getClass().getName() + "Mapper." + "updateByPrimaryKey";
			SqlSession session = this.getSession();
			session.update(nameSpaceId, entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(Object entity) {
		try {
			String nameSpaceId = entity.getClass().getName() + "Mapper." + "deleteByPrimaryKey";
			SqlSession session = this.getSession();
			session.delete(nameSpaceId, entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List select(String sqlString, String filterCondition, Object[] parameters, DBResultSetProcessor processor) {
		return this.select(sqlString, filterCondition, parameters, processor, false,null);
	}
	public List selectds(String sqlString, String filterCondition, Object[] parameters, DBResultSetProcessor processor,String dbMaster) {
		return this.select(sqlString, filterCondition, parameters, processor, false,dbMaster);
	}
	
	private List select(String sqlString, String filterCondition, Object[] parameters, DBResultSetProcessor processor, boolean isAll,String dbMaster) {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		ArrayList records = new ArrayList(50);
		if (processor == null) {
			processor = new DefaultDBResultSetProcessor();
		}
		Object e1;
		try {
			pstmt = this.getConnection(dbMaster).prepareStatement(sqlString, 1003, 1007);
			if (this.log.isInfoEnabled()) {
				this.log.info("sqlString");
			}

			this.setParameters(pstmt, parameters);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				records.add(((DBResultSetProcessor) processor).processResultSetRow(rs));
			}

			if (this.log.isInfoEnabled()) {
				this.log.info(records.size() + " records returned.");
			}

			ArrayList e = records;
			return e;
		} catch (Exception arg19) {
			this.log.info(this.getClass().getName() + " select " + arg19.getMessage());
			e1 = null;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}

				if (pstmt != null) {
					pstmt.close();
				}
			} catch (Exception arg18) {
				if (this.log.isInfoEnabled()) {
					this.log.info(this.getClass().getName() + " select " + arg18.getMessage());
				}
			}

		}
		return (List) e1;
	}

	private void setParameters(PreparedStatement pstmt, Object[] parameters) throws SQLException {
		if (parameters != null) {
			for (int i = 0; i < parameters.length; ++i) {
				if (this.log.isInfoEnabled()) {
					this.log.info("parameter[" + i + "]=" + parameters[i]);
				}

				if (parameters[i] != null && !"".equals(parameters[i])) {
					if (parameters[i] instanceof Date) {
						pstmt.setTimestamp(i + 1, new Timestamp(((Date) parameters[i]).getTime()));
					} else if (!(parameters[i] instanceof Float) && !(parameters[i] instanceof Double)) {
						pstmt.setObject(i + 1, parameters[i]);
					} else {
						pstmt.setObject(i + 1, parameters[i].toString());
					}
				} else {
					pstmt.setString(i + 1, (String) null);
				}
			}
		}

	}

	public void paginSelect(Pagin pg, String sql, Object args1, List args2, DBResultSetProcessor pro) {
		StringBuffer cSql = new StringBuffer("");
		if ("MySQL".equals(dbType)) {
			cSql.append("SELECT COUNT(0) COUNT FROM ( ").append(sql).append(" ) tt ");
		} else {
			cSql.append("SELECT COUNT(0) COUNT FROM ( ").append(sql).append(" )");
		}

		List clist = this.select(cSql.toString(), "", args2.toArray(), new DBResultSetProcessor() {
			public Object processResultSetRow(ResultSet rs) throws SQLException {
				return Integer.valueOf(rs.getInt("COUNT"));
			}
		});
		if (clist != null && clist.size() > 0) {
			StringBuffer sqlb = new StringBuffer();
			pg.setTotal((Integer) clist.get(0));
			Object list = new ArrayList(20);
			if ("Oracle".equals(dbType)) {
				sqlb.append("select * from ( SELECT rownum PAGIN_ROWNUM,ana.* FROM ( ").append(sql).append(") ana where rownum < ? ) where PAGIN_ROWNUM > ? ");
				args2.add(Long.valueOf(Pagin.getPageNumber().longValue() * Pagin.getPageSize().longValue() + 1L));
				args2.add(Long.valueOf((Pagin.getPageNumber().longValue() - 1L) * Pagin.getPageSize().longValue()));
				list = this.selectAll(sqlb.toString(), "", args2.toArray(), pro);
			} else if ("MySQL".equals(dbType)) {
				sqlb.append("select * from ( ").append(sql).append(") as tnt limit ?,?");
				args2.add(Long.valueOf((Pagin.getPageNumber().longValue() - 1L) * Pagin.getPageSize().longValue()));
				args2.add(Pagin.getPageSize());
				list = this.select(sqlb.toString(), "", args2.toArray(), pro);
			}
			pg.setRows((Collection) list);
		}
	}

	public List selectAll(String sqlString, String filterCondition, Object[] parameters, DBResultSetProcessor processor) {
		return this.select(sqlString, filterCondition, parameters, processor, true,null);
	}

}
