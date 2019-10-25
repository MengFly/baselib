package com.mengfly.lib;


import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class CollectionUtilTest {

	@Test
	void testSplit() {
		List<String> list = new ArrayList<>();
		for(int i = 0; i < 100; i++) {
			list.add(String.valueOf(i));
		}
		System.out.println(CollectionUtil.split(list, 900));
	}

}
