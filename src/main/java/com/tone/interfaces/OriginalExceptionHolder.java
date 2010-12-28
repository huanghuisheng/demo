package com.tone.interfaces;

public interface OriginalExceptionHolder {
	Throwable getOriginalThrowable();

	String getOriginalStackTrace();
}
