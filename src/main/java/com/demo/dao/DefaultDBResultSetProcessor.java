package com.demo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DefaultDBResultSetProcessor implements DBResultSetProcessor {
	public Object processResultSetRow(ResultSet rs) throws SQLException {
		int numcols = rs.getMetaData().getColumnCount();
		Object[] record = new Object[numcols];

		for (int i = 0; i < numcols; ++i) {
			record[i] = rs.getObject(i + 1);
		}

		return record;
	}
}
