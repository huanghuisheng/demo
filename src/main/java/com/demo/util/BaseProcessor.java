package com.demo.util;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.demo.interfaces.DBResultSetProcessor;




public class BaseProcessor implements DBResultSetProcessor {
	private Class tg;

	public BaseProcessor(Class tg2) {
		tg = tg2;
	}
	public Object processResultSetRow(ResultSet rs) throws SQLException {
		try {
			Object obj = tg.newInstance();
			Field[] fields = tg.getDeclaredFields();
			Map<String, Field> map = fieldConvertMap(fields);
			ResultSetMetaData rsmd = rs.getMetaData();
			if (rsmd != null && rsmd.getColumnCount() > 0) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String fname = rsmd.getColumnName(i);
					if (!"PAGIN_ROWNUM".equals(fname)) {
						if (fname != null) {
							String fieldName = fieldConvert(fname);
							if (map.containsKey(fieldName)) {
								Field field = map.get(fieldName);
								if (field != null) {
									field.setAccessible(true);
									Class cl = field.getType();
									if (cl.equals(Integer.class)) {
										//field.set(obj, rs.getInt(fname));
										field.set(obj, rs.getString(fname)==null?null:rs.getInt(fname));
									}
									if (cl.equals(Long.class) || cl.equals(long.class)) {
										//field.set(obj, rs.getLong(fname));
										field.set(obj, rs.getString(fname)==null?null:rs.getLong(fname));
									}
									if (cl.equals(String.class)) {
										field.set(obj, rs.getString(fname));
									}
									if (cl.equals(Double.class)) {
										//field.set(obj, rs.getDouble(fname));
										field.set(obj, rs.getString(fname)==null?null:rs.getDouble(fname));
									}
									if (cl.equals(Date.class)) {
										field.set(obj, rs.getTimestamp(fname));
									}
									if (cl.equals(Float.class)) {
										//field.set(obj, rs.getFloat(fname));
										field.set(obj, rs.getString(fname)==null?null:rs.getFloat(fname));
									}
								}
							}
						}
					}
				}
			}
			return obj;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String fieldConvert(String field) {
		StringBuffer sb = new StringBuffer();
		if (field.indexOf("_") != -1) {
			String[] arrStr = field.split("_");
			for (int j = 0; j < arrStr.length; j++) {
				if (j == 0) {
					sb.append(arrStr[j].toLowerCase());
				} else {
					if (arrStr[j].length() >= 1) {
						sb.append(arrStr[j].substring(0, 1).toUpperCase());
						if (arrStr[j].length() >= 2)
							sb.append(arrStr[j].substring(1).toLowerCase());
					}
				}
			}
		} else {
			if (field.length() > 0) {
				sb.append(field.toLowerCase());
			}
		}
		return sb.toString();
	}

	/**
	 * Des: 把类对应属性注入Map 2012-4-26
	 * 
	 * @author mopy
	 * @param fields
	 * @return String
	 */
	private static Map<String, Field> fieldConvertMap(Field[] fields) {
		Map<String, Field> map = new HashMap<String, Field>(fields.length);
		for (Field field : fields) {
			map.put(field.getName(), field);
		}
		return map;
	}

}