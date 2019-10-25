package com.mengfly.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class StringUtil {

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static boolean equals(String str1, String str2) {
		return str1 == null ? str2 == null : str1.equals(str2);
	}

	public static <T> List<T> splitToAnyList(String str, String separator, Function<String, T> fun) {
		Objects.requireNonNull(separator, "Param separator can't be null!");
		List<T> list = new ArrayList<>();
		if (isEmpty(str)) {
			return list;
		}
		for (String s : str.split(separator)) {
			list.add(fun.apply(s));
		}
		return list;
	}

	public static List<Double> splitToDoubleList(String str, String separator) {
		return splitToAnyList(str, separator, Double::valueOf);
	}

}
