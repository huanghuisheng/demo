package com.demo.util;



public class SystemException extends UnrecoverableException {
	private static final long serialVersionUID = -4369604296692379840L;

	public SystemException(String messageKey) {
		super(messageKey);
	}

	public SystemException(String messageKey, Object[] parameterList) {
		super(messageKey, parameterList);
	}

	public SystemException(String messageKey, Throwable throwable) {
		super(messageKey, throwable);
	}

	public SystemException(String messageKey, Object[] parameterList,
			Throwable throwable) {
		super(messageKey, parameterList, throwable);
	}
}