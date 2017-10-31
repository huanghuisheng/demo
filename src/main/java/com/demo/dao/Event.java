package com.demo.dao;

import java.io.Serializable;
import java.util.Map;

public interface Event extends Serializable {
	void setActionName(String arg0);

	String getActionName();

	void setActionType(int arg0);

	int getActionType();

	String getEventName();

	void setEventName(String arg0);

	Map getParameters();

	void put(Object arg0, Object arg1);

	Object get(Object arg0);
}