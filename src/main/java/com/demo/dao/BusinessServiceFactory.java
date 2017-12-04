package com.demo.dao;

import gnu.trove.THashMap;

import java.util.Map;

import com.bdcc.waf.exception.SystemException;
import com.bdcc.waf.service.BaseService;
import com.bdcc.waf.utils.ClassUtils;

public class BusinessServiceFactory {
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

	public static Object getService(Class serviceInterface, Class serviceImpl) {
		if (serviceInterface != null && serviceImpl != null) {
			Object svc = delegateList.get(serviceImpl);
			if (svc == null) {
				svc = getSynchronizedService(serviceInterface, serviceImpl);
			}

			return svc;
		} else {
			return null;
		}
	}

	private static synchronized Object getSynchronizedService(
			Class serviceInterface) {
		Object svc = delegateList.get(serviceInterface);
		if (svc == null) {
			svc = ClassUtils.classInstance(serviceInterface.getName() + "Impl");
			if (!(svc instanceof BaseService)) {
				throw new SystemException(serviceInterface.getName() + "Impl"
						+ " class should extend from BaseServiceImpl");
			}

			delegateList.put(serviceInterface, svc);
		}

		return svc;
	}

	private static synchronized Object getSynchronizedService(
			Class serviceInterface, Class serviceImpl) {
		Object svc = delegateList.get(serviceImpl);
		if (svc == null) {
			svc = ClassUtils.classInstance(serviceImpl);
			if (!(svc instanceof BaseService)) {
				throw new SystemException(serviceImpl.getName()
						+ " class should extend from BaseServiceImpl");
			}

			delegateList.put(serviceImpl, svc);
		}

		return svc;
	}
