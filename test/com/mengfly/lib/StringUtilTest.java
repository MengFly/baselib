package com.mengfly.lib;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilTest {

	@Test
	void testIsEmpty() {
		assertFalse(StringUtil.isEmpty("1"));
		assertTrue(StringUtil.isEmpty(""));
		assertTrue(StringUtil.isEmpty(null));
	}

	@Test
	void testEqualsStringString() {
		
	}

	@Test
	void testSplitToDoubleList() {
		assertArrayEquals(StringUtil.splitToDoubleList("123,345,345.6", ",").toArray(), new Double[] {123., 345., 345.6});
		assertArrayEquals(StringUtil.splitToDoubleList("123", ",").toArray(), new Double[] {123.});
	}

	@Test
	void testJoin() {
		System.out.println(StringUtil.join(new String[]{"1","2","1","2","1","2","1","2","1","2","1","2"}, "#"));
	}

}
