/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
package com.tone.exception;

public interface OriginalExceptionHolder {
	Throwable getOriginalThrowable();

	String getOriginalStackTrace();
}