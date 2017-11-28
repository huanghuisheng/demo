package com.demo.dao;

import gnu.trove.THashMap;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BusinessHelper {
	private static Logger log = LoggerFactory.getLogger(BusinessHelper.class);
	private static Map threadLocalVar = new THashMap(200);
	private static Map serviceMap = new THashMap(200);
	private static Map serviceProxyMap = new THashMap(200);
	private static final String CONTEXT_METHOD = "getContext";
	private static ActionResult sResult = null;
	private static ThreadLocal eventVariable = new ThreadLocal() {
		protected synchronized Object initialValue() {
			return new BaseEvent();
		}
	};

	private static Object getServiceProxy(Class className) {
		Object service = serviceMap.get(className);
		if (service == null) {
			service = getSynServiceProxy(className);
		}

		return service;
	}

	private static synchronized Object getSynServiceProxy(Class className) {
		Object service = serviceMap.get(className);
		if (service == null) {
			service = Proxy.newProxyInstance(className.getClassLoader(), new Class[] { className }, new BusinessHelper.WrapperHandlerNoRequest(className));
			serviceMap.put(className, service);
		}

		return service;
	}

	private static boolean isUniqueMethodInProxy(Class classProxy, String methodName) {
		boolean isExist = false;
		Map objectMap = (Map) ((Map) serviceProxyMap.get(classProxy));
		if (objectMap == null) {
			isExist = getSynServicProxy(classProxy, methodName);
		} else {
			isExist = objectMap.containsKey(methodName);
		}

		return isExist;
	}

	private static synchronized boolean getSynServicProxy(Class classProxy, String methodName) {
		Object objectMap = (Map) ((Map) serviceProxyMap.get(classProxy));
		if (objectMap == null) {
			objectMap = new HashMap();
			putAllMethods(classProxy, (Map) objectMap);
			serviceProxyMap.put(classProxy, objectMap);
		}

		return ((Map) objectMap).containsKey(methodName);
	}

	private static void putAllMethods(Class clazz, Map methodMap) {
		HashMap duplicatedMethod = new HashMap();
		Method[] methods = clazz.getMethods();

		for (int i = 0; i < methods.length; ++i) {
			Method methodObj = methods[i];
			String name = methodObj.getName();
			if (!"notify".equals(name) && !"toString".equals(name) && !"equals".equals(name) && !"wait".equals(name) && !"hashCode".equals(name) && !"getClass".equals(name)
					&& !"notifyAll".equals(name) && !duplicatedMethod.containsKey(name)) {
				if (methodMap.containsKey(name)) {
					duplicatedMethod.put(name, name);
					methodMap.remove(name);
				} else {
					methodMap.put(name, name);
				}
			}
		}

	}

	private static ActionResult executeCommand(Event event) {
		if (event == null) {
			throw new SystemException("No business action need to be executed.");
		} else {
			Thread currentThread = Thread.currentThread();
			HttpServletRequest request = (HttpServletRequest) ((HttpServletRequest) threadLocalVar.get(currentThread));
			threadLocalVar.remove(currentThread);
			Object result = null;

			ErrorObject errorObj;
			try {
				BusinessActionController errorList = BusinessActionControllerFactory.getBusinessActionController();
				ParameterInjectorHelper.getInstance().preprocess(request, event);
				result = errorList.executeCommand(event);
			} catch (SystemException arg7) {
				if (log.isDebugEnabled()) {
					log.debug("executeCommand error", arg7);
				}

				result = new BaseActionResult();
				ErrorObject iter1 = arg7.export();
				((ActionResult) result).addErrorObject(iter1);
			} catch (Exception arg8) {
				if (log.isDebugEnabled()) {
					log.debug("executeCommand error", arg8);
				}

				SystemException iter = new SystemException("Execute command(" + event.getService() + ") error", arg8);
				result = new BaseActionResult();
				errorObj = iter.export();
				((ActionResult) result).addErrorObject(errorObj);
			}

			if (result != null) {
				sResult = (ActionResult) result;
				List errorList1 = ((ActionResult) result).getAllError();
				if (((ActionResult) result).getStatus() == -1 || errorList1 != null && errorList1.size() > 0) {
					if (errorList1 != null && errorList1.size() > 0) {
						Iterator iter2 = errorList1.iterator();

						while (iter2.hasNext()) {
							errorObj = (ErrorObject) ((ErrorObject) iter2.next());
							String errorKey = null;
							if (errorObj.getType() == 1) {
								errorKey = "waf.warnings";
							} else if (errorObj.getType() == 2) {
								errorKey = "waf.messages";
							}

							if (request != null) {
								StrutsUtils.addError(request, errorObj.getKey(), errorObj.getParameters(), errorKey);
							}

							if (log.isDebugEnabled()) {
								log.debug("[" + errorObj.getKey() + "]" + errorObj.getOriginalMessage());
							}
						}
					}

					throw new SystemNotificationException(event.getService());
				}
			}

			return (ActionResult) result;
		}
	}

	private static ActionResult executeCommand(HttpServletRequest request, Event event) {
		if (event == null) {
			throw new SystemException("No business action need to be executed.");
		} else {
			Object result = null;

			ErrorObject errorObj;
			try {
				BusinessActionController errorList = BusinessActionControllerFactory.getBusinessActionController();
				ParameterInjectorHelper.getInstance().preprocess(request, event);
				result = errorList.executeCommand(event);
			} catch (SystemException arg6) {
				if (log.isDebugEnabled()) {
					log.debug("executeCommand error", arg6);
				}

				result = new BaseActionResult();
				ErrorObject iter1 = arg6.export();
				((ActionResult) result).addErrorObject(iter1);
			} catch (Exception arg7) {
				if (log.isDebugEnabled()) {
					log.debug("executeCommand error", arg7);
				}

				SystemException iter = new SystemException("Execute command(" + event.getService() + ") error", arg7);
				result = new BaseActionResult();
				errorObj = iter.export();
				((ActionResult) result).addErrorObject(errorObj);
			}

			if (result != null) {
				List errorList1 = ((ActionResult) result).getAllError();
				if (((ActionResult) result).getStatus() == -1 || errorList1 != null && errorList1.size() > 0) {
					if (errorList1 != null && errorList1.size() > 0) {
						Iterator iter2 = errorList1.iterator();

						while (iter2.hasNext()) {
							errorObj = (ErrorObject) ((ErrorObject) iter2.next());
							String errorKey = null;
							if (errorObj.getType() == 1) {
								errorKey = "waf.warnings";
							} else if (errorObj.getType() == 2) {
								errorKey = "waf.messages";
							}

							if (request != null) {
								StrutsUtils.addError(request, errorObj.getKey(), errorObj.getParameters(), errorKey);
							}

							if (log.isDebugEnabled()) {
								log.debug("[" + errorObj.getKey() + "]" + errorObj.getOriginalMessage());
							}
						}
					}

					throw new SystemNotificationException(event.getService());
				}
			}

			return (ActionResult) result;
		}
	}

	private static ActionResult executeCommandForUT(HttpServletRequest request, Event event) {
		if (event == null) {
			throw new SystemException("No business action need to be executed.");
		} else {
			Object result = null;

			try {
				BusinessActionController e = BusinessActionControllerFactory.getBusinessActionController();
				ParameterInjectorHelper.getInstance().preprocess(request, event);
				result = e.executeCommand(event);
				ThreadLocalResourceManager.closeAllResource();
			} catch (SystemException arg5) {
				if (log.isDebugEnabled()) {
					log.debug("executeCommand error", arg5);
				}

				result = new BaseActionResult();
				ErrorObject se1 = arg5.export();
				((ActionResult) result).addErrorObject(se1);
			} catch (Exception arg6) {
				if (log.isDebugEnabled()) {
					log.debug("executeCommand error", arg6);
				}

				SystemException se = new SystemException("Execute command(" + event.getService() + ") error", arg6);
				result = new BaseActionResult();
				ErrorObject eo = se.export();
				((ActionResult) result).addErrorObject(eo);
			}

			if (result != null && ((ActionResult) result).getStatus() == -1) {
				ThreadLocalResourceManager.rollbackAllResource();
			}

			return (ActionResult) result;
		}
	}

	public static Object getService2(HttpServletRequest request, Class interfaceName) {
		if (interfaceName == null) {
			throw new IllegalArgumentException("Invaild the object.");
		} else {
			return Proxy.newProxyInstance(interfaceName.getClassLoader(), new Class[] { interfaceName }, new BusinessHelper.WrapperHandler(request, interfaceName));
		}
	}

	public static Object getService(HttpServletRequest request, Class interfaceName) {
		if (interfaceName == null) {
			throw new IllegalArgumentException("Invaild the object.");
		} else {
			Object service = getServiceProxy(interfaceName);
			threadLocalVar.put(Thread.currentThread(), request);
			return service;
		}
	}

	public static Object getServiceForUT(HttpServletRequest request, Class interfaceName) {
		if (interfaceName == null) {
			throw new IllegalArgumentException("Invaild the object.");
		} else {
			return Proxy.newProxyInstance(interfaceName.getClassLoader(), new Class[] { interfaceName }, new BusinessHelper.UTWrapperHandler(request, interfaceName));
		}
	}

	public static Object getService(Object originalObject) {
		if (originalObject == null) {
			throw new IllegalArgumentException("Invaild the object.");
		} else {
			return Proxy.newProxyInstance(originalObject.getClass().getClassLoader(), originalObject.getClass().getInterfaces(), new BusinessHelper.TestWrapperHandler(originalObject));
		}
	}

	public static ActionResult getSResult() {
		return sResult;
	}

	static final class WrapperHandler implements InvocationHandler {
		private Class originalInterface;
		private HttpServletRequest request;

		public WrapperHandler(HttpServletRequest request, Class instance) {
			this.request = request;
			this.originalInterface = instance;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (method.getName().equals("getContext")) {
				return null;
			} else {
				BaseEvent event = new BaseEvent();
				event.put("waf.service.class", this.originalInterface);
				event.put("waf.service.method", method.getName());
				event.put("waf.service.parameter.type", method.getParameterTypes());
				event.put("waf.service.parameter", args);
				ActionResult actionResult = BusinessHelper.executeCommand(this.request, event);
				return actionResult.getResult("waf.service.result");
			}
		}
	}

	static final class WrapperHandlerNoRequest implements InvocationHandler {
		private Class originalInterface;

		public WrapperHandlerNoRequest(Class instance) {
			this.originalInterface = instance;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (method.getName().equals("getContext")) {
				return null;
			} else {
				BaseEvent event = (BaseEvent) ((BaseEvent) BusinessHelper.eventVariable.get());
				
				String methodName = method.getName();
				event.put("waf.service.class", this.originalInterface);
				event.put("waf.service.method", methodName);
				if (!BusinessHelper.isUniqueMethodInProxy(proxy.getClass(), methodName)) {
					event.put("waf.service.parameter.type", method.getParameterTypes());
				}

				event.put("waf.service.parameter", args);
				
				ActionResult actionResult = BusinessHelper.executeCommand(event);
				
				event.init();
				return actionResult.getResult("waf.service.result");
			}
		}
	}

	static final class QuickWrapperHandlerNoRequest implements InvocationHandler {
		private Class originalInterface;

		public QuickWrapperHandlerNoRequest(Class instance) {
			this.originalInterface = instance;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (method.getName().equals("getContext")) {
				return null;
			} else {
				BaseEvent event = new BaseEvent();
				event.put("waf.service.class", this.originalInterface);
				event.put("waf.service.method", method.getName());
				event.put("waf.service.parameter.type", method.getParameterTypes());
				event.put("waf.service.parameter", args);
				ActionResult actionResult = BusinessHelper.executeCommand(event);
				return actionResult.getResult("waf.service.result");
			}
		}
	}

	static final class UTWrapperHandler implements InvocationHandler {
		private Class originalInterface;
		private List errors;
		private HttpServletRequest request;

		public UTWrapperHandler(HttpServletRequest request, Class instance) {
			this.request = request;
			this.originalInterface = instance;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (method.getName().equals("getContext")) {
				if (this.errors == null) {
					return null;
				} else {
					ServiceContextImpl event1 = new ServiceContextImpl();
					event1.setAllError(this.errors);
					return event1;
				}
			} else {
				BaseEvent event = new BaseEvent();
				event.put("waf.service.class", this.originalInterface);
				event.put("waf.service.method", method.getName());
				event.put("waf.service.parameter.type", method.getParameterTypes());
				event.put("waf.service.parameter", args);
				ActionResult actionResult = BusinessHelper.executeCommandForUT(this.request, event);
				this.errors = actionResult.getAllError();
				return actionResult.getResult("waf.service.result");
			}
		}
	}

	static final class TestWrapperHandler implements InvocationHandler {
		private Object instance;

		public TestWrapperHandler(Object originalObject) {
			this.instance = originalObject;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Throwable error = null;
			Object result = null;
			try {
				result = method.invoke(this.instance, args);
			} catch (InvocationTargetException arg6) {
				error = arg6.getTargetException();
			} catch (Throwable arg7) {
				error = arg7;
			}

			if (error != null) {
				error.printStackTrace();
				throw error;
			} else {
				return result;
			}
		}
	}
}
