package com.tone.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DBResultSetProcessor {
	Object processResultSetRow(ResultSet arg0) throws SQLException;
}
