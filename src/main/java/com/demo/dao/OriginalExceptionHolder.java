package com.demo.dao;

public interface OriginalExceptionHolder {
	Throwable getOriginalThrowable();

	String getOriginalStackTrace();
}
