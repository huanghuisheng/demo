package com.demo.constant;

public final class SynchronizedLock {
	private static SynchronizedLock singleInstance = new SynchronizedLock();
	private boolean isInitializedConfigFactory = false;

	public static SynchronizedLock getInstance() {
		return singleInstance;
	}

	public boolean isInitializedConfigFactory() {
		return this.isInitializedConfigFactory;
	}

	public void setInitializedConfigFactory(boolean isInitialized) {
		this.isInitializedConfigFactory = isInitialized;
	}
}
