package com.mengfly.lib;

import java.util.*;
import java.util.function.Function;

public class StringUtil {

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean equals(String str1, String str2) {
        return Objects.equals(str1, str2);
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

    public static String join(List<String> ss, String separator) {
        StringBuilder builder = new StringBuilder();
        for (Iterator<String> iterator = ss.iterator(); iterator.hasNext(); ) {
            String next = iterator.next();
            builder.append(next);
            if (iterator.hasNext()) {
                builder.append(separator);
            }
        }
        return builder.toString();
    }

    public static String join(String[] ss, String separator) {
        return join(Arrays.asList(ss), separator);
    }

    public static List<Double> splitToDoubleList(String str, String separator) {
        return splitToAnyList(str, separator, Double::valueOf);
    }

    public static String newRepeat(String repeatRes, int count) {
        StringBuilder result = new StringBuilder(repeatRes.length() * count);
        for (int i = 0; i < count; i++) {
            result.append(repeatRes);
        }
        return result.toString();

    }

}
