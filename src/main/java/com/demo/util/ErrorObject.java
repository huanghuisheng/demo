package com.demo.util;

import java.io.Serializable;

public class ErrorObject implements Serializable {
	private static final long serialVersionUID = -445978956372602941L;
	public static final int ERROR = -1;
	public static final int WARN = 1;
	public static final int MESSAGE = 2;
	protected int type;
	protected String uniqueId;
	protected String key;
	protected Object[] parameters;
	protected String originalMessage;

	public ErrorObject() {
		this.type = -1;
	}

	public ErrorObject(int type, String key, Object[] parameters) {
		this.setType(type);
		this.setKey(key);
		this.setParameters(parameters);
	}

	public int getType() {
		return this.type;
	}

	public void setType(int pType) {
		this.type = pType;
	}

	public void setKey(String pKey) {
		this.key = pKey;
	}

	public String getKey() {
		return this.key;
	}

	public void setParameters(Object[] pParameters) {
		this.parameters = pParameters;
	}

	public Object[] getParameters() {
		return this.parameters;
	}

	public void setMessageId(String messageId) {
		this.uniqueId = messageId;
	}

	public String getMessageId() {
		return this.uniqueId;
	}

	public void setOriginalMessage(String message) {
		this.originalMessage = message;
	}

	public String getOriginalMessage() {
		return this.originalMessage;
	}
}
