package com.demo.dao;

import java.util.Map;

import com.bdcc.waf.controller.business.action.BusinessAction;
import com.bdcc.waf.event.ActionResult;
import com.bdcc.waf.event.Event;
import com.bdcc.waf.service.BaseService;
import com.bdcc.waf.service.BusinessServiceFactory;
import com.bdcc.waf.service.ServiceContextImpl;
import com.bdcc.waf.utils.SmartProxy;

public class ServiceWrapper{

	private static void executePrivate(Event event, ActionResult actionResult) throws Throwable {
		Class serviceInterface = (Class) event.get("waf.service.class");
		String methodName = (String) event.get("waf.service.method");
		Object[] args = (Object[]) event.get("waf.service.parameter");
		Class[] parameterTypes = (Class[]) event.get("waf.service.parameter.type");
		BaseService service = (BaseService) BusinessServiceFactory.getService(serviceInterface);
		Object result = null;
		Map para = event.getParameters();
		if (para.size() > 4) {
			para.remove("waf.service.class");
			para.remove("waf.service.method");
			para.remove("waf.service.parameter");
			para.remove("waf.service.parameter.type");
			((ServiceContextImpl) service.getContext()).setParameters(para);
		}

		SmartProxy proxy = SmartProxy.getInstance();
		result = proxy.dynamicInvoke(service, methodName, parameterTypes, args);
		actionResult.setResult("waf.service.result", result);
	}
}
