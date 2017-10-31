package com.demo.dao;

//import com.bdcc.waf.exception.ErrorObject;
//import com.bdcc.waf.exception.ExceptionUtil;
//import com.bdcc.waf.exception.OriginalExceptionHolder;

public class UnrecoverableException extends RuntimeException implements OriginalExceptionHolder {
	private static final long serialVersionUID = 7881003034798351398L;
	public static final String LP = System.getProperty("line.separator");
	protected String userFriendlyErrorMessage = null;
	protected Object[] arguments = null;
	protected String uniqueId = ExceptionUtil.getExceptionID();
	protected Throwable originalThrowable = null;

	public UnrecoverableException() {
	}

	public UnrecoverableException(String messageKey) {
		super(messageKey);
		this.userFriendlyErrorMessage = messageKey;
	}

	public UnrecoverableException(String messageKey, Throwable throwable) {
		super(messageKey);
		this.userFriendlyErrorMessage = messageKey;
		if (throwable instanceof OriginalExceptionHolder) {
			this.originalThrowable = ((OriginalExceptionHolder) throwable).getOriginalThrowable();
		} else {
			this.originalThrowable = throwable;
		}

	}

	public UnrecoverableException(String messageKey, Object[] parameterList) {
		super(messageKey);
		this.userFriendlyErrorMessage = messageKey;
		this.arguments = parameterList;
	}

	public UnrecoverableException(String messageKey, Object[] parameterList, Throwable throwable) {
		super(messageKey);
		this.userFriendlyErrorMessage = messageKey;
		this.arguments = parameterList;
		if (throwable instanceof OriginalExceptionHolder) {
			this.originalThrowable = ((OriginalExceptionHolder) throwable).getOriginalThrowable();
		} else {
			this.originalThrowable = throwable;
		}

	}

	public Throwable getOriginalThrowable() {
		return (Throwable) (this.originalThrowable != null ? (this.originalThrowable instanceof OriginalExceptionHolder ? this
				.extractOriginalThrowable((OriginalExceptionHolder) this.originalThrowable) : this.originalThrowable) : this);
	}

	private Throwable extractOriginalThrowable(OriginalExceptionHolder exception) {
		Throwable original;
		for (original = exception.getOriginalThrowable(); original != exception; original = exception.getOriginalThrowable()) {
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

	public String getMessage() {
		StringBuffer sb = new StringBuffer(this.userFriendlyErrorMessage);
		sb.append(LP);
		if (this.originalThrowable != null) {
			sb.append(this.originalThrowable.getMessage());
		} else {
			sb.append(super.getMessage());
		}

		return sb.toString();
	}

	public String getLocalizedMessage() {
		return this.getMessage();
	}

	public String getOriginalStackTrace() {
		return this.originalThrowable == null ? ExceptionUtil.getStackTrace(this) : (this.originalThrowable instanceof OriginalExceptionHolder ? ((OriginalExceptionHolder) this.originalThrowable)
				.getOriginalStackTrace() : ExceptionUtil.getStackTrace(this.originalThrowable));
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

	public String getUserFriendlyErrorMessage() {
		return this.userFriendlyErrorMessage;
	}
}
