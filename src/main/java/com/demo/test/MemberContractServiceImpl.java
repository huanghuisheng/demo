package com.demo.test;

import java.util.List;

import com.demo.dao.BaseDAO;
import com.demo.dao.SmsMember;
import com.demo.util.BaseProcessor;


public class MemberContractServiceImpl  implements MemberContractService{

	public BaseDAO dao=new BaseDAO();
	public Object addMemberContract() {
		BaseDAO a = new BaseDAO();
		for (int i = 11000; i < 12000; i++) {
			SmsMember b = new SmsMember();
			b.setId(Long.valueOf(i));
			b.setUserId(Long.valueOf(i));
			a.create(b);
		}
		return null;
	}

	public Object selectMemberContract() {
		// TODO Auto-generated method stub
	
		StringBuffer sql=new StringBuffer();
		
		sql.append(" SELECT * from sms_member");
		
		List<SmsMember>	 list = dao.select(sql.toString(), null, null, new BaseProcessor(SmsMember.class));
		
		for(SmsMember sms:list)
		{
			System.out.println(sms.getId());
		}
		
		
		return sql;
		
		
		
		
	}
	
}
