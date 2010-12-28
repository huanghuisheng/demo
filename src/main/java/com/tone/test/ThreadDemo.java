package com.tone.test;

import java.util.List;

import com.tone.dao.BaseDAO;
import com.tone.dao.SmsMember;
import com.tone.dao.ThreadLocalResourceManager;
import com.tone.util.BaseProcessor;

public class ThreadDemo implements Runnable {
	public BaseDAO dao;
	
	public ThreadDemo(BaseDAO dao){
		this.dao=dao;
	}
	
	public void run() {
		Object txnKey1 = ThreadLocalResourceManager.getThreadId();
		System.out.println("---------------------------线程---------------------------------------"+txnKey1.toString());
		for (int i = 0; i < 1; i++) {
			SmsMember b = new SmsMember();
			b.setId(Long.valueOf(txnKey1.hashCode()+i));
			b.setUserId(Long.valueOf(i));
			dao.create(b);
		}
	}

}
