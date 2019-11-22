package com.mengfly.lib;

/**
 * 这个类用于各种类型转换 其目的是为了屏蔽掉各种的转换失败后的异常，从而提供一个默认值，因此其只适用于某些可以忽略异常获取默认值的情况
 * 
 * @author wangp
 *
 */
public class ParseUtil {

	public static Integer intAdd(String... elements) {
		Integer result = 0;
		for (String element : elements) {
			result += parseIntOrDefault(element, 0);
		}
		return result;
	}

	public static Float floatAdd(String... elements) {
		Float result = 0.F;
		for (String element : elements) {
			result += parseFloatOrDefault(element, 0.F);
		}
		return result;
	}

	public static Integer parseIntOrDefault(String str, Integer defaultInt) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return defaultInt;
		}
	}

	public static Double parseDoubleOrDefault(String str, Double defaultDouble) {
		try {
			return Double.parseDouble(str);
		} catch (NumberFormatException e) {
			return defaultDouble;
		}
	}

	public static Integer parseObjectToIntOrDefault(Object obj, Integer defaultInt) {
		if (obj == null)
			return defaultInt;
		try {
			return Integer.parseInt(obj.toString());
		} catch (NumberFormatException e) {
			return defaultInt;
		}
	}

	public static Double parseObjectToDoubleOrDefault(Object obj, Double defaultDouble) {
		if (obj == null)
			return defaultDouble;
		try {
			return Double.parseDouble(obj.toString());
		} catch (NumberFormatException e) {
			return defaultDouble;
		}
	}

	public static Float parseFloatOrDefault(String str, Float defaultFloat) {
		try {
			return Float.parseFloat(str);
		} catch (NumberFormatException ex) {
			return defaultFloat;
		}
	}

	public static Boolean parseBoolean(String str) {
		Integer integer = parseIntOrDefault(str, null);
		if (integer == null) {
			return Boolean.parseBoolean(str);
		} else {
			return integer == 1;
		}
	}

	public static Integer addAllInt(Integer... integers) {
		Integer sum = 0;
		for (Integer integer : integers) {
			sum += integer;
		}
		return sum;
	}

	public static Double addAllDouble(Double... integers) {
		Double sum = 0.;
		for (Double integer : integers) {
			sum += integer;
		}
		return sum;
	}

	public static <T> T getOrDefault(T integer, T defaultInt) {
		if (integer == null) {
			return defaultInt;
		} else {
			return integer;
		}
	}

	public static String getStringOrDefault(Object obj, String s) {
		if (obj == null) {
			return s;
		}
		return obj.toString();
	}

}
