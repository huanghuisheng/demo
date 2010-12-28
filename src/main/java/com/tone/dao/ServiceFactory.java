package com.tone.dao;

import gnu.trove.THashMap;

import java.util.Map;

import com.tone.util.ClassUtils;


public class ServiceFactory {
	private static Map delegateList = new THashMap(100);
	private static final String JAVABEAN_SUFFIX = "Impl";

	public static Object getService(Class serviceInterface) {
		if (serviceInterface == null) {
			return null;
		} else {
			Object svc = delegateList.get(serviceInterface);
			if (svc == null) {
				svc = getSynchronizedService(serviceInterface);
			}

			return svc;
		}
	}
	private static synchronized Object getSynchronizedService(Class serviceInterface) {
		Object svc = delegateList.get(serviceInterface);
		if (svc == null) {
			Class a;
			try {
//				a = Class.forName(serviceInterface.getName() + "Impl");
//				svc = a.newInstance();
				svc=ClassUtils.classInstance(serviceInterface.getName()+JAVABEAN_SUFFIX);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			delegateList.put(serviceInterface, svc);
		}
		return svc;
	}
}
