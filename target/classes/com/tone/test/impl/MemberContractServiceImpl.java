package com.tone.test.impl;

import java.util.ArrayList;
import java.util.List;

import com.tone.dao.BaseDAO;
import com.tone.test.MemberContractService;
import com.tone.test.People;
import com.tone.test.SmsMember;
import com.tone.test.ThreadDemo;
import com.tone.util.BaseProcessor;


public class MemberContractServiceImpl  implements MemberContractService {

	public BaseDAO dao=new BaseDAO();
	public Object addMemberContract() {
		BaseDAO a = new BaseDAO();
		List<People> list=new ArrayList<People>();
		for (int i = 0; i < 10; i++) {
			People peo=new People();
			peo.setName("小明"+i);
//			peo.setId(i);
			list.add(peo);
		}
		a.createByBatch(list);
		return null;
	}

	public Object selectMemberContract() {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer();
		sql.append(" SELECT * from1 people ");
		List<People>	 list = dao.select(sql.toString(), null, null, new BaseProcessor(People.class));
		System.out.println("数字为-------"+list.size());
		for(People sms:list)
		{
			System.out.println(sms.getId());
		}
		return sql;
	}
	
	public Object selectMemberContract1() {
		// TODO Auto-generated method stub
//		Object txnKey1 = ThreadLocalResourceManager.getThreadId();
//		System.out.println("---------------------------线程---------------------------------------"+txnKey1.toString());
               Thread  thread=Thread.currentThread();
      System.out.println("---------------------------线程---------------------------------------"+thread.toString());
	  System.out.println("---------------------------线程---------------------------------------"+thread.getId()+thread.getName()+thread.getPriority());	
		
	    ThreadDemo threadDemo=new ThreadDemo(dao);
		
		new Thread(threadDemo).start();
		
		new Thread(threadDemo).start();
		
		new Thread(threadDemo).start();
		
		ThreadGroup group=thread.getThreadGroup();
		
//		ThreadGroup group = Thread.currentThread().getThreadGroup();  
		ThreadGroup topGroup = group;  
		// 遍历线程组树，获取根线程组  
		while (group != null) {  
		    topGroup = group;  
		    group = group.getParent();  
		}  
		// 激活的线程数加倍  
		int estimatedSize = topGroup.activeCount() * 2;  
		Thread[] slackList = new Thread[estimatedSize];  
		// 获取根线程组的所有线程  
		int actualSize = topGroup.enumerate(slackList);  
		// copy into a list that is the exact size  
		Thread[] list = new Thread[actualSize];  
		System.arraycopy(slackList, 0, list, 0, actualSize);  
		System.out.println("Thread list size == " + list.length);  
		for (Thread thread1 : list) {  
		    System.out.println(thread1.getName());  
		}
		
		
		 System.out.println("---------------------------线程---------------------------------------"+thread.getThreadGroup());		
		
		return null;
	}
	
}
