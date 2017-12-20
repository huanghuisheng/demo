package com.demo.constant;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

public interface Config extends Serializable {
	void init();

	String getEARPath();

	boolean getBoolean(String arg0, String arg1, String arg2);

	float getFloat(String arg0, String arg1, String arg2);

	int getInteger(String arg0, String arg1);

	String getString(String arg0, String arg1);

	String getString(String arg0, String arg1, String arg2);

	Map getSection(String arg0);

	Properties getSectionAsProperties(String arg0);

	String[] getStringArray(String arg0, String arg1);
}