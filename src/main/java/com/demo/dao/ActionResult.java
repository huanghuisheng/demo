package com.demo.dao;

import java.io.Serializable;
import java.util.List;

public interface ActionResult extends Serializable {
	int FAIL = -1;
	int SUCCESS = 0;

	void setStatus(int arg0);

	int getStatus();

	Object getResult(Object arg0);

	void setResult(Object arg0, Object arg1);

	void addError(String arg0);

	void addError(String arg0, Object[] arg1);

	void removeAllWarning();

	void addWarning(String arg0);

	void addWarning(String arg0, Object[] arg1);

	void addMessage(String arg0);

	void addMessage(String arg0, Object[] arg1);

	void addErrorObject(ErrorObject arg0);

	List getAllError();
}