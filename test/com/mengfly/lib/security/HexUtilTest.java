package com.mengfly.lib.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HexUtilTest {

	/*
	 * Test Case:
	 * 
	 * YL001：59 4C 30 30 31
	 * MG255：4D 47 32 35 35 
	 * LC001：4C 43 30 30 31
	 */
	@Test
	void testStr2ASCII2Hex() {
		assertEquals(HexBinUtil.str2Ascii2Hex("YL001"), "59 4C 30 30 31");
		assertEquals(HexBinUtil.str2Ascii2Hex("MG255"), "4D 47 32 35 35");
		assertEquals(HexBinUtil.str2Ascii2Hex("LC001"), "4C 43 30 30 31");

	}

}
