/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
package com.tone.exception;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class DaoExceptionHandler {
	private static Map dbMessage = new HashMap();

	static {
		dbMessage.put("ORA-00018", "maximum.number.of.sessions.exceeded");
		dbMessage.put("ORA-00020", "maximum.number.of.processes.exceeded");
		dbMessage.put("No row with the given identifier exists",
				"no.row.given.identifier.exists");
		dbMessage.put("ORA-00001", "unique.constraint.violated");
		dbMessage.put("ORA-02292",
				"integrity.constraint.violated.child.record.found");
		dbMessage.put("ORA-01400",
				"integrity.constraint.violated.field.not.null");
		dbMessage.put("ORA-01407",
				"integrity.constraint.violated.field.not.null");
		dbMessage.put("[SQLServer]Violation of PRIMARY KEY constraint",
				"unique.constraint.violated");
		dbMessage
				.put("[SQLServer]DELETE statement conflicted with COLUMN REFERENCE constraint",
						"integrity.constraint.violated.child.record.found");
		dbMessage.put("Duplicate entry", "unique.constraint.violated");
		dbMessage
				.put("Cannot delete or update a parent row: a foreign key constraint fails",
						"integrity.constraint.violated.child.record.found");
	}

	private static String parseException(String message) {
		if (message != null && message.length() != 0) {
			Iterator iter = dbMessage.entrySet().iterator();

			while (iter.hasNext()) {
				Entry entry = (Entry) ((Entry) iter.next());
				if (message.indexOf((String) ((String) entry.getKey())) > -1) {
					return (String) ((String) entry.getValue());
				}
			}

			return null;
		} else {
			return null;
		}
	}

	public static void exceptionHandler(Throwable throwable) {
		if (throwable != null && throwable.getCause() != null) {
			String newKey = parseException(throwable.getCause().getMessage());
			if (newKey != null && newKey.length() > 0) {
				throw new SystemException(newKey, throwable);
			}
		}

	}
}