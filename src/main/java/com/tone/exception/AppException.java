/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
package com.tone.exception;

public class AppException extends Exception implements OriginalExceptionHolder {
	private static final long serialVersionUID = -7257279691798348919L;
	public static final String LP = System.getProperty("line.separator");
	protected String userFriendlyErrorMessage = null;
	protected Object[] arguments = null;
	protected String uniqueId = ExceptionUtil.getExceptionID();
	protected Throwable originalThrowable = null;

	public AppException() {
	}

	public AppException(String messageKey) {
		super(messageKey);
		this.userFriendlyErrorMessage = messageKey;
	}

	public AppException(String messageKey, Throwable throwable) {
		super(messageKey);
		this.userFriendlyErrorMessage = messageKey;
		if (throwable instanceof OriginalExceptionHolder) {
			this.originalThrowable = ((OriginalExceptionHolder) throwable)
					.getOriginalThrowable();
		} else {
			this.originalThrowable = throwable;
		}

	}

	public AppException(String messageKey, Object[] parameterList) {
		super(messageKey);
		this.userFriendlyErrorMessage = messageKey;
		this.arguments = parameterList;
	}

	public AppException(String messageKey, Object[] parameterList,
			Throwable throwable) {
		super(messageKey);
		this.userFriendlyErrorMessage = messageKey;
		this.arguments = parameterList;
		if (throwable instanceof OriginalExceptionHolder) {
			this.originalThrowable = ((OriginalExceptionHolder) throwable)
					.getOriginalThrowable();
		} else {
			this.originalThrowable = throwable;
		}

	}

	public Throwable getOriginalThrowable() {
		return (Throwable) (this.originalThrowable != null
				? (this.originalThrowable instanceof OriginalExceptionHolder
						? this.extractOriginalThrowable((OriginalExceptionHolder) this.originalThrowable)
						: this.originalThrowable)
				: this);
	}

	private Throwable extractOriginalThrowable(OriginalExceptionHolder exception) {
		Throwable original;
		for (original = exception.getOriginalThrowable(); original != exception; original = exception
				.getOriginalThrowable()) {
			if (original == null) {
				original = (Throwable) exception;
				break;
			}

			if (!(original instanceof OriginalExceptionHolder)) {
				break;
			}

			exception = (OriginalExceptionHolder) original;
		}

		return original;
	}

	public String getErrorMessage() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append(this.uniqueId);
		sb.append("] ");
		sb.append(this.userFriendlyErrorMessage);
		return sb.toString();
	}

	public String getMessage() {
		return super.getMessage();
	}

	public String getLocalizedMessage() {
		return this.getMessage();
	}

	public String getOriginalStackTrace() {
		return this.originalThrowable == null
				? ExceptionUtil.getStackTrace(this)
				: (this.originalThrowable instanceof OriginalExceptionHolder
						? ((OriginalExceptionHolder) this.originalThrowable)
								.getOriginalStackTrace() : ExceptionUtil
								.getStackTrace(this.originalThrowable));
	}

	public String getAllErrorMessage() {
		StringBuffer sb = new StringBuffer(this.getErrorMessage());
		sb.append(LP);
		sb.append(this.getOriginalStackTrace());
		return sb.toString();
	}

	public String getMessageId() {
		return this.uniqueId;
	}

	public ErrorObject export() {
		ErrorObject eo = new ErrorObject();
		eo.setMessageId(this.getMessageId());
		eo.setKey(this.userFriendlyErrorMessage);
		eo.setParameters(this.arguments);
		eo.setOriginalMessage(this.getOriginalStackTrace());
		return eo;
	}
}