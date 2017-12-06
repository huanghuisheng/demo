package com.demo.util;

//import com.achievo.waf.utils.SystemException;

public class ClassUtils {
	public static Object classInstance(String p_className) {
		try {
			Class e = loadClass(p_className);
			return e.newInstance();
		} catch (IllegalAccessException arg1) {
			throw new SystemException(p_className
					+ " class instantiation error", arg1);
		} catch (InstantiationException arg2) {
			throw new SystemException(p_className
					+ " class instantiation error", arg2);
		}
	}

	public static Object classInstance(Class p_class) {
		try {
			return p_class.newInstance();
		} catch (IllegalAccessException arg1) {
			throw new SystemException(p_class.getName()
					+ " class instantiation error", arg1);
		} catch (InstantiationException arg2) {
			throw new SystemException(p_class.getName()
					+ " class instantiation error", arg2);
		}
	}

	public static Class loadClass(String p_className) {
		try {
			ClassLoader e = Thread.currentThread().getContextClassLoader();
			if (e == null) {
				e=ClassUtils.class.getClassLoader();
			}
			Class classz = e.loadClass(p_className);
			return classz;
		} catch (ClassNotFoundException arg2) {
			throw new SystemException(p_className + " class not found", arg2);
		}
	}

	public static boolean isAssignableFrom(String p_class, Class p_interface) {
		return p_interface.isAssignableFrom(loadClass(p_class));
	}
	
	public static String getParameterNameOfMethod(Class[] parameterTypes) {
		if (parameterTypes != null && parameterTypes.length != 0) {
			StringBuffer sb = new StringBuffer(40);
			sb.append(parameterTypes.length);

			for (int i = 0; i < parameterTypes.length; ++i) {
				if (parameterTypes[i].isArray()) {
					Class clazz = parameterTypes[i].getComponentType();
					sb.append("_").append(clazz.getName()).append("[]");
				} else {
					sb.append("_").append(parameterTypes[i].getName());
				}
			}
			return sb.toString();
		} else {
			return "0";
		}
	}
	
	
}