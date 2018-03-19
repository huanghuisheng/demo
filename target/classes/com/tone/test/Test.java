package com.tone.test;

import com.tone.constant.ConfigFactory;



public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	 String ADDR=ConfigFactory.getInstance().getConfig().getString("dsList", "getMaster");
	 System.out.println("--------"+ADDR);
	}

}
