package com.tone.dao;

import com.tone.exception.SystemException;
import com.tone.interfaces.Closeable;
import org.apache.ibatis.session.SqlSession;

public class SqlSessionWrapper3 implements Closeable{
    protected SqlSession originalSession;
    protected boolean isClosed = false;

    public void setOriginalSession(SqlSession session) {
        try {
            this.originalSession = session;
        } catch (Exception arg2) {
            this.originalSession = null;
            throw new SystemException("Hibernate begin transaction error", arg2);
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
                throw new SystemException("HibernateSession commit error", arg4);
            } finally {
                this.originalSession = null;
                this.isClosed = true;
            }

        }
    }
}
