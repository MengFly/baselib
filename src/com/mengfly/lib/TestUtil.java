package com.mengfly.lib;
import java.util.ArrayList;
import java.util.List;

public class TestUtil {

	
	public static List<String> getArangeList(int num) {
		List<String> list = new ArrayList<>();
		for(int i = 0; i < num; i++) {
			list.add(String.valueOf(i));
		}
		return list;
	}
}
