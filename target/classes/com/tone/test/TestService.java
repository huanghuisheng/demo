package com.tone.test;
import com.tone.dao.ServiceHelper;


public class TestService {
	
	public static void main(String[] args) {
		MemberContractService service = (MemberContractService) ServiceHelper.getService(MemberContractService.class);
//		service.addMemberContract();
//		MemberContractService service1 = (MemberContractService) BusinessServiceFactory.getService(MemberContractService.class);
//		service1.addMemberContract();
//		service.selectMemberContract();
		System.out.println("----------------------开始-------------------------");
		service.selectMemberContract();
		service.selectMemberContract();
		System.out.println("----------------------开始-------------------------");
//		service.selectMemberContract();

	}





}
