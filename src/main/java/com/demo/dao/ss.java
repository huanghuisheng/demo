package com.demo.dao;

import com.bdcc.waf.service.ServiceContext;

public interface BaseService {
	ServiceContext getContext();

	void setContext(ServiceContext arg0);
}
