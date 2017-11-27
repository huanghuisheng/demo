package com.demo.dao;

import gnu.trove.THashMap;

public class FastThreadLocal {
	private THashMap keyMap = new THashMap(200);

	public Object get() {
		Object obj = this.keyMap.get(getThreadId());
		return obj != null ? obj : null;
	}

	public Object get(Object threadId) {
		Object obj = this.keyMap.get(threadId);
		return obj != null ? obj : null;
	}

	public boolean containKey(Object threadId) {
		return this.keyMap.containsKey(threadId);
	}

	public void set(Object value) {
		Object key = getThreadId();
		if (!this.keyMap.containsKey(key)) {
			this.keyMap.put(key, value);
		}

	}

	public void set(Object threadId, Object value) {
		if (!this.keyMap.containsKey(threadId)) {
			this.keyMap.put(threadId, value);
		}

	}

	public void remove() {
		this.keyMap.remove(getThreadId());
	}

	public void remove(Object threadId) {
		this.keyMap.remove(threadId);
	}

	public static Object getThreadId() {
		return Thread.currentThread();
	}
}