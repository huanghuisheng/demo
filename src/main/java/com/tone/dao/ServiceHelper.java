package com.tone.dao;

import com.tone.exception.SystemException;
import gnu.trove.THashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public final class ServiceHelper {
    private static Logger log = LoggerFactory.getLogger(ServiceHelper.class);
    private static Map serviceMap = new THashMap(200);

    private static Object getServiceProxy(Class className) {
        Object service = serviceMap.get(className);
        if (service == null) {
            service = getSynServiceProxy(className);
        }
        return service;
    }

    @SuppressWarnings("unchecked")
    private static synchronized Object getSynServiceProxy(Class className) {
        Object service = serviceMap.get(className);
        if (service == null) {
            service = Proxy.newProxyInstance(className.getClassLoader(), new Class[]{className}, new ServiceHelper.WrapperHandlerNoRequest(className));
            serviceMap.put(className, service);
        }
        return service;
    }

    public static Object getService(Class interfaceName) {
        if (interfaceName == null) {
            throw new IllegalArgumentException("Invaild the object.");
        } else {
            Object service = getServiceProxy(interfaceName);
            return service;
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
                Object errorObj = null;
                Object result = null;
                boolean isRollback = false;
                try {
                    Object service = ServiceFactory.getService(this.originalInterface);
                    result = method.invoke(service, args);
                    ThreadLocalResourceManager.closeAllResource();
                } catch (SystemException arg4) {
                    log.error(" SystemException invoke is fail ", arg4);
                    isRollback = true;
                    throw arg4;
                } catch (Exception arg7) {
                    log.error(" Exception invoke  is fail", arg7);
                    isRollback = true;
                    throw arg7;
                }
                try {
                    if (errorObj != null || isRollback) {
                        ThreadLocalResourceManager.rollbackAllResource();
                    }
                } catch (Exception arg8) {
                    log.error("rollbackAll  is fail", arg8);
                }
                return result;
            }
        }
    }
}
	
