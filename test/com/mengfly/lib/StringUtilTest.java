package com.mengfly.lib;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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

}
