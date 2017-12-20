package com.demo.test;

import com.demo.constant.ConfigFactory;



public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	 String ADDR=ConfigFactory.getInstance().getConfig().getString("logging", "configFile");
	 System.out.println("--------"+ADDR);
	}

}
