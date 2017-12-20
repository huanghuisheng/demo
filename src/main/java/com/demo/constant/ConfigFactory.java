package com.demo.constant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.demo.util.SystemException;

public final class ConfigFactory implements Serializable, Initializable {
	private static final long serialVersionUID = 1L;
	private static Config configImpl;
	private static ConfigFactory factoryInstance = new ConfigFactory();
	private static Map registeredConfigClient = new HashMap(30);

	private ConfigFactory() {
		this.init();
	}

	public static ConfigFactory getInstance() {
		return factoryInstance;
	}

	public Config getConfig() {
		while (!SynchronizedLock.getInstance().isInitializedConfigFactory()) {
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException arg1) {
				throw new SystemException("Thread interrupted.", arg1);
//				arg1.printStackTrace();
			}
		}

		return configImpl;
	}

	public synchronized void init() {
		SynchronizedLock.getInstance().setInitializedConfigFactory(false);
		configImpl = new JFigConfig();
		configImpl.init();

		try {
			Thread.sleep(1000L);
		} catch (Exception arg1) {
			;
		}

		SynchronizedLock.getInstance().setInitializedConfigFactory(true);
	}

	public void register(String clientName, Object registeredObject) {
		if (clientName != null && registeredObject != null) {
			registeredConfigClient.put(clientName, registeredObject);
		}
	}

	public boolean isRegistered(String clientName) {
		return registeredConfigClient.containsKey(clientName);
	}
}