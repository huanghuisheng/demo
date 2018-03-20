package com.tone.dao;

import gnu.trove.THashMap;

import java.util.Map;

import com.tone.util.ClassUtils;


public class ServiceFactory {
	private static Map delegateList = new THashMap(100);
	private static final String JAVABEAN_SUFFIX = "Impl";
	private static final String JAVAPACKAGE_SUFFIX = ".impl.";
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
				String serviceUrl = serviceInterface.getPackage().getName() + JAVAPACKAGE_SUFFIX +
						serviceInterface.getSimpleName() + JAVABEAN_SUFFIX;
				svc = ClassUtils.classInstance(serviceUrl);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			delegateList.put(serviceInterface, svc);
		}
		return svc;
	}
}
