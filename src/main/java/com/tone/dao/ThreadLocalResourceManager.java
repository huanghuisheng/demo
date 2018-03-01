package com.tone.dao;

import com.tone.exception.SystemException;
import gnu.trove.THashMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tone.interfaces.Closeable;
import com.tone.interfaces.Rollbackable;
import com.tone.util.FastThreadLocal;


public final class ThreadLocalResourceManager {
	private static Logger log = LoggerFactory.getLogger(ThreadLocalResourceManager.class);
	
	private static final FastThreadLocal TO_BE_CLOSED_RESOURCE = new FastThreadLocal();
	private static final Map TO_BE_CLOSED_RESOURCE_BY_ORDER = new THashMap(200);
	

	public static Object getResource(Object key) {
		Map map = (Map) TO_BE_CLOSED_RESOURCE.get();
		return map == null ? null : map.get(key);
	}

	public static Object getResource(Object threadId, Object key) {
		if (threadId == null) {
			return getResource(key);
		} else {
			Map map = (Map) TO_BE_CLOSED_RESOURCE.get(threadId);
			return map == null ? null : map.get(key);
		}
	}

	public static void bindResource(Object key, Object value) {
		bindResource((Object) null, key, value);
	}

	public static void bindResource(Object threadId, Object key, Object value) {
		if (threadId == null) {
			threadId = FastThreadLocal.getThreadId();
		}

		Object map = (Map) TO_BE_CLOSED_RESOURCE.get(threadId);
		Stack resourceStack = (Stack) ((Stack) TO_BE_CLOSED_RESOURCE_BY_ORDER
				.get(threadId));
		if (resourceStack == null) {
			resourceStack = new Stack();
			TO_BE_CLOSED_RESOURCE_BY_ORDER.put(threadId, resourceStack);
		}

		if (map == null) {
			map = new HashMap();
			TO_BE_CLOSED_RESOURCE.set(threadId, map);
		}

		if (((Map) map).containsKey(key)) {
			throw new SystemException("Already exist value  for key [" + key
					+ "] ");
		} else {
			resourceStack.push(value);
			((Map) map).put(key, value);
		}
	}

	public static Object unbindResource(Object key) {
		return unbindResource((Object) null, key);
	}

	public static Object unbindResource(Object threadId, Object key) {
		if (threadId == null) {
			threadId = FastThreadLocal.getThreadId();
		}

		Map map = (Map) TO_BE_CLOSED_RESOURCE.get(threadId);
		if (map == null) {
			return null;
		} else {
			closeResource(map.get(key));
			Object value = map.remove(key);
			if (map.isEmpty()) {
				TO_BE_CLOSED_RESOURCE.remove(threadId);
			}

			return value;
		}
	}

	public static Object getThreadId() {
		return FastThreadLocal.getThreadId();
	}

	public static void setRollbackOnly() {
		setValue(getThreadId(), "waf.isRollbackOnly", "waf.isRollbackOnly");
	}

	public static boolean getRollbackOnly() {
		return getValueOnly(getThreadId(), "waf.isRollbackOnly") != null;
	}

	public static boolean getRollbackOnly(Object threadId) {
		return getValueOnly(threadId, "waf.isRollbackOnly") != null;
	}

	private static void setValue(Object threadId, Object key, Object value) {
		Object map = (Map) TO_BE_CLOSED_RESOURCE.get(threadId);
		if (map == null) {
			map = new HashMap();
			TO_BE_CLOSED_RESOURCE.set(threadId, map);
		}

		((Map) map).put(key, value);
	}

	private static Object getValueOnly(Object threadId, Object key) {
		Map map = (Map) TO_BE_CLOSED_RESOURCE.get(threadId);
		return map == null ? null : map.get(key);
	}

	public static void closeAllResource() {
		closeAllResource(getThreadId());
	}

	public static void closeAllResource(Object threadId) {
		if (threadId == null) {
			threadId = getThreadId();
		}

		Map map = (Map) TO_BE_CLOSED_RESOURCE.get(threadId);
		if (map != null && map.size() != 0) {
			Stack resourceStack = (Stack) ((Stack) TO_BE_CLOSED_RESOURCE_BY_ORDER
					.get(threadId));
			if (resourceStack != null && resourceStack.size() != 0) {
				while (resourceStack.size() > 0) {
					Object obj = resourceStack.pop();

					try {
						closeResource(obj);
					} catch (Exception arg8) {
						if (log.isErrorEnabled()) {
							log.error("commit fail.", arg8);
						}

						try {
							rollbackAllResource(threadId);
						} finally {
							TO_BE_CLOSED_RESOURCE_BY_ORDER.remove(threadId);
							TO_BE_CLOSED_RESOURCE.remove(threadId);
							map.clear();
						}
						throw new SystemException(
								"The transaction commits fail.", arg8);
					}
				}

				TO_BE_CLOSED_RESOURCE_BY_ORDER.remove(threadId);
				TO_BE_CLOSED_RESOURCE.remove(threadId);
				map.clear();
			} else {
				TO_BE_CLOSED_RESOURCE.remove(threadId);
				TO_BE_CLOSED_RESOURCE_BY_ORDER.remove(threadId);
			}
		} else {
			TO_BE_CLOSED_RESOURCE.remove(threadId);
			TO_BE_CLOSED_RESOURCE_BY_ORDER.remove(threadId);
		}
	}

	private static void closeResource(Object resource) {
		if (resource != null) {
			if (resource instanceof Closeable) {
				((Closeable) resource).objectClose();
			}

		}
	}

	public static void rollbackAllResource() {
		rollbackAllResource(getThreadId());
	}

	public static void rollbackAllResource(String threadId) {
		rollbackAllResource(threadId);
	}

	public static void rollbackAllResource(Object threadId) {
		if (threadId == null) {
			threadId = getThreadId();
		}

		Map map = (Map) TO_BE_CLOSED_RESOURCE.get(threadId);
		if (map != null && map.size() != 0) {
			Stack resourceStack = (Stack) ((Stack) TO_BE_CLOSED_RESOURCE_BY_ORDER
					.get(threadId));
			TO_BE_CLOSED_RESOURCE.remove(threadId);
			TO_BE_CLOSED_RESOURCE_BY_ORDER.remove(threadId);
			if (resourceStack != null && resourceStack.size() != 0) {
				boolean isSuccess = true;

				while (resourceStack.size() > 0) {
					Object obj = resourceStack.pop();
					if (log.isDebugEnabled()) {
						log.debug("[rollbackResource][" + threadId + "]" + obj);
					}
					if (!rollbackResource(obj)) {
						isSuccess = false;
					}
				}
				map.clear();
				if (!isSuccess) {
					throw new SystemException("The transaction rollbacks fail");
				}
			}
		} else {
			TO_BE_CLOSED_RESOURCE.remove(threadId);
			TO_BE_CLOSED_RESOURCE_BY_ORDER.remove(threadId);
		}
	}

	private static boolean rollbackResource(Object resource) {
		if (resource == null) {
			return true;
		} else {
			try {
				if (resource instanceof Rollbackable) {
					((Rollbackable) resource).transactionRollback();
				}
				return true;
			} catch (Exception arg1) {
				if (log.isErrorEnabled()) {
					log.error("rollback fail.", arg1);
				}
				return false;
			}
		}
	}
}
