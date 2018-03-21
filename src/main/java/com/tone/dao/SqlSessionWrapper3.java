package com.tone.dao;

import com.tone.exception.SystemException;
import com.tone.interfaces.Closeable;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class SqlSessionWrapper3 implements Closeable ,SqlSession{
    protected SqlSession originalSession;
    protected boolean isClosed = false;

    public void setOriginalSession(SqlSession session) {
        try {
            this.originalSession = session;
        } catch (Exception arg2) {
            this.originalSession = null;
            throw new SystemException("mybatis begin transaction error", arg2);
        }
    }

    public SqlSession getWrappedSession() {
        return this.originalSession;
    }

    public void objectClose() {
        if (!this.isClosed) {
            try {
                this.originalSession.commit();
                this.originalSession.close();
            } catch (Exception arg4) {
                throw new SystemException("mybatisSession commit error", arg4);
            } finally {
                this.originalSession = null;
                this.isClosed = true;
            }

        }
    }

    @Override
    public <T> T selectOne(String statement) {
        return originalSession.selectOne(statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        return originalSession.selectOne(statement,parameter);
    }

    @Override
    public <E> List<E> selectList(String statement) {
        return originalSession.selectList(statement);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter) {
        return originalSession.selectList(statement,parameter);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
        return originalSession.selectList(statement,parameter,rowBounds);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
        return originalSession.selectMap(statement,mapKey);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
        return originalSession.selectMap(statement,parameter,mapKey);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
        return originalSession.selectMap(statement,parameter,mapKey,rowBounds);
    }

    @Override
    public void select(String statement, Object parameter, ResultHandler handler) {
        originalSession.select(statement,parameter,handler);
    }

    @Override
    public void select(String statement, ResultHandler handler) {
        originalSession.select(statement,handler);
    }

    @Override
    public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
        originalSession.select(statement,parameter,rowBounds,handler);
    }

    @Override
    public int insert(String statement) {
       return  originalSession.insert(statement);

    }

    @Override
    public int insert(String statement, Object parameter) {
        return originalSession.insert(statement,parameter);
    }

    @Override
    public int update(String statement) {
        return originalSession.update(statement);
    }

    @Override
    public int update(String statement, Object parameter) {
        return originalSession.update(statement,parameter);
    }

    @Override
    public int delete(String statement) {
        return originalSession.delete(statement);
    }

    @Override
    public int delete(String statement, Object parameter) {
        return originalSession.delete(statement,parameter);
    }

    @Override
    public void commit() {
         originalSession.commit();
    }

    @Override
    public void commit(boolean force) {
        originalSession.commit(force);
    }

    @Override
    public void rollback() {
        originalSession.rollback();
    }

    @Override
    public void rollback(boolean force) {
        originalSession.rollback(force);
    }

    @Override
    public List<BatchResult> flushStatements() {
        return originalSession.flushStatements();
    }

    @Override
    public void close() {
        originalSession.close();
    }

    @Override
    public void clearCache() {
        originalSession.clearCache();
    }

    @Override
    public Configuration getConfiguration() {
        return originalSession.getConfiguration();
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return originalSession.getMapper(type);
    }

    @Override
    public Connection getConnection() {
        return originalSession.getConnection();
    }
}
