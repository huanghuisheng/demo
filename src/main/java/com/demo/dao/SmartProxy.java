package com.demo.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

//import com.bdcc.waf.exception.SystemException;
//import com.bdcc.waf.utils.ClassUtils;
//import com.bdcc.waf.utils.ProxyStub;
//import com.bdcc.waf.utils.SmartProxy;

public final class SmartProxy {
	private Map staticProxys = new HashMap(200);
	private static final String POSTFIX = "1Proxy";
	private static SmartProxy singleInstance = new SmartProxy();

	public static SmartProxy getInstance() {
		return singleInstance;
	}

	public Object invoke(Object originalObject, String methodName, Class[] parameterTypes, Object[] args) throws Exception {
		if (originalObject != null && methodName != null) {
			Object result = null;
			Class className = originalObject.getClass();
			ProxyStub staticProxy = (ProxyStub) ((ProxyStub) this.staticProxys.get(className));
			if (staticProxy == null) {
				staticProxy = this.getSynchronizedProxy(originalObject, className);
				if (staticProxy == null) {
					try {
						Method e = originalObject.getClass().getMethod(methodName, parameterTypes);
						result = e.invoke(originalObject, args);
						return result;
					} catch (InvocationTargetException arg9) {
						Throwable throwVal = arg9.getTargetException();
						if (throwVal instanceof Exception) {
							throw (Exception) throwVal;
						}
//						throw new SystemException("System Fatal Exception", throwVal);
					}
				}
			}

			result = staticProxy.invoke(originalObject, methodName, parameterTypes, args);
			return result;
		} else {
			throw new IllegalArgumentException("Invalid object or method:" + originalObject + "." + methodName);
		}
	}

	public Object dynamicInvoke(Object originalObject, String methodName, Class[] parameterTypes, Object[] args) throws Throwable {
		if (originalObject != null && methodName != null) {
			Object result = null;
			String className = originalObject.getClass().getName();
			Method methodObject = null;
			Map objectMap = (Map) ((Map) this.staticProxys.get(className));
			if (objectMap == null) {
				methodObject = this.getSynchronizedDynProxy(originalObject, (Map) null, className, methodName, parameterTypes);
			} else {
				Object e = objectMap.get(methodName);
				if (e instanceof Method) {
					methodObject = (Method) e;
				} else {
					String throwVal = ClassUtils.getParameterNameOfMethod(parameterTypes);
					methodObject = (Method) ((Method) ((Map) e).get(throwVal));
				}
			}

			try {
				result = methodObject.invoke(originalObject, args);
				return result;
			} catch (InvocationTargetException arg10) {
				Throwable throwVal1 = arg10.getTargetException();
				throw throwVal1;
			}
		} else {
			throw new IllegalArgumentException("Invalid object or method:" + originalObject + "." + methodName);
		}
	}

	private synchronized ProxyStub getSynchronizedProxy(Object originalObject, Class className) {
		ProxyStub staticProxy = (ProxyStub) ((ProxyStub) this.staticProxys.get(className));
		if (staticProxy == null) {
			try {
				String e = className.getName() + "1Proxy";
				staticProxy = (ProxyStub) ClassUtils.classInstance(e);
				this.staticProxys.put(className, staticProxy);
			} catch (Exception arg4) {
				staticProxy = null;
			}
		}

		return staticProxy;
	}

	private synchronized Method getSynchronizedDynProxy(Object originalObject, Map objectMap, String className, String methodName, Class[] parameterTypes) throws Throwable {
		if (objectMap == null) {
			objectMap = new HashMap();
			this.staticProxys.put(className, objectMap);
		}

		if (!((Map) objectMap).containsKey(methodName)) {
			putAllMethods(originalObject.getClass(), (Map) objectMap);
		}

		Object obj = ((Map) objectMap).get(methodName);
		if (obj instanceof Method) {
			return (Method) obj;
		} else {
			String uniqueParameter = ClassUtils.getParameterNameOfMethod(parameterTypes);
			return (Method) ((Method) ((Map) obj).get(uniqueParameter));
		}
	}

	private static void putAllMethods(Class clazz, Map methodMap) {
		Method[] methods = clazz.getMethods();

		for (int i = 0; i < methods.length; ++i) {
			Method methodObj = methods[i];
			String name = methodObj.getName();
			if (!"notify".equals(name) && !"toString".equals(name) && !"equals".equals(name) && !"wait".equals(name) && !"hashCode".equals(name) && !"getClass".equals(name)
					&& !"notifyAll".equals(name)) {
				Object orgObj = methodMap.get(name);
				if (orgObj != null) {
					Object methodSubMap = null;
					if (orgObj instanceof Method) {
						methodSubMap = new HashMap();
						Method uniqueParameter = (Method) orgObj;
						String uniqueParameter1 = ClassUtils.getParameterNameOfMethod(uniqueParameter.getParameterTypes());
						((Map) methodSubMap).put(uniqueParameter1, uniqueParameter);
					} else {
						methodSubMap = (Map) orgObj;
					}

					String arg9 = ClassUtils.getParameterNameOfMethod(methodObj.getParameterTypes());
					((Map) methodSubMap).put(arg9, methodObj);
					methodMap.put(name, methodSubMap);
				} else {
					methodMap.put(name, methodObj);
				}
			}
		}

	}
}