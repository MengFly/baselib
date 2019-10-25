package com.mengfly.lib.web;

import org.junit.jupiter.api.Test;

class ResponseWrapperTest {
	
	static class testBean {
		@SuppressWarnings("unused")
		private int f1 = 1;
		@SuppressWarnings("unused")
		private int f2 = 2;
		@SuppressWarnings("unused")
		private String f3 = "f3";
		
	}

	@Test
	void testResponseWrapper() {
		
		ResponseWrapper<testBean> wapper = new ResponseWrapper<ResponseWrapperTest.testBean>(new testBean());
		wapper.ignoreAdd("f1");
		wapper.accessAdd("f2", "f1", "f3");
		System.out.println(wapper.getResult());
		
	}

}
