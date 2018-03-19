package com.tone.constant;

import java.util.Map;
import java.util.Properties;

import com.tone.exception.SystemException;
import org.igfay.jfig.JFig;
import org.igfay.jfig.JFigException;



public class JFigConfig implements Config {
	private static final long serialVersionUID = 507247809361656182L;
	private ConfigLocator configLocator;

	public synchronized void init() {
		try {
			String ex = System.getProperty("config.filename");
			if (ex == null || ex.length() == 0) {
				ex = "config.xml";
			}

			this.configLocator = new ConfigLocator(ex);
			JFig.initialize(this.configLocator);
		} catch (JFigException arg1) {

			throw new SystemException("Load config fail.", arg1);
	
		}
	}

	public String getEARPath() {
		if (this.configLocator == null) {
			this.init();
		}

		return this.configLocator.getEARPath();
	}

	public boolean getBoolean(String section, String entry, String notFoundValue) {
		try {
			return JFig.getInstance().getBooleanValue(section, entry,
					notFoundValue);
		} catch (Exception arg4) {
			return false;
		}
	}

	public float getFloat(String section, String entry, String notFoundValue) {
		try {
			float value = JFig.getInstance().getFloatValue(section, entry,
					notFoundValue);
			return value;
		} catch (JFigException arg5) {
			throw new SystemException(
					"Confige Service don\'t found appropriate value. getFloat( "
							+ section + "," + entry + "," + notFoundValue, arg5);
			
			
			
		}
	}

	public int getInteger(String section, String entry) {
		try {
			int value = JFig.getInstance().getIntegerValue(section, entry);
			return value;
		} catch (JFigException arg4) {
			throw new SystemException(
					"Confige Service don\'t found appropriate value. getInteger( "
							+ section + "," + entry, arg4);
		}
	}

	public String getString(String section, String entry) {
		String value = null;

		try {
			value = JFig.getInstance().getValue(section, entry);
			return value;
		} catch (JFigException arg4) {
			return null;
		}
	}

	public String getString(String section, String entry, String notFoundValue) {
		String value = null;

		try {
			value = JFig.getInstance().getValue(section, entry, notFoundValue);
		} catch (Exception arg5) {
			;
		}

		return value;
	}

	public synchronized Map getSection(String section) {
		try {
			return JFig.getInstance().getSection(section);
		} catch (Exception arg2) {
			return null;
		}
	}

	public synchronized Properties getSectionAsProperties(String section) {
		try {
			return JFig.getInstance().getSectionAsProperties(section);
		} catch (Exception arg2) {
			return null;
		}
	}

	public String[] getStringArray(String section, String entry) {
		String[] value = (String[]) null;

		try {
			value = JFig.getInstance().getArrayValue(section, entry);
			return value;
		} catch (JFigException arg4) {
			return null;
		}
	}
}