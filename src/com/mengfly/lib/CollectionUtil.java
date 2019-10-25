package com.mengfly.lib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CollectionUtil {

	/**
	 * 判断集合是否为空
	 * @return 集合为 NULL 或 集合元素为个数 0
	 */
	public static <E> boolean isEmpty(Collection<E> col) {
		return col == null || col.isEmpty();
	}

	public static <E> boolean isNotEmpty(Collection<E> col) {
		return !isEmpty(col);
	}

	/**
	 * 判断Map对象是否为空
	 * @return Map对象是NULL 或Map对象元素个数为 0
	 */
	public static <K, V> boolean isEmpty(Map<K, V> map) {
		return map == null || map.isEmpty();
	}

	public static <K, V> boolean isNotEmpty(Map<K, V> map) {
		return !isEmpty(map);
	}

	
	/**
	 * 分割list为多个子list
	 * @param list 要进行分割的list
	 * @param childNum 每个list的中item的数量大小
	 * @return 分割后的所有list组合
	 */
	public static <E> List<List<E>> split(List<E> list, int childNum) {
		if (childNum <= 0) {
			throw new IllegalArgumentException("Param childNum can't less than zero!");
		}
		if (isNotEmpty(list)) {
			int initialCapacity = (list.size() % childNum == 0) ? list.size() / childNum : list.size() / childNum + 1;
			List<List<E>> result = new ArrayList<List<E>>();
			for (int i = 0; i < initialCapacity; i++) {
				result.add(list.subList(i * childNum, Math.min((i + 1) * childNum, list.size())));
			}
			return result;
		}
		return Collections.emptyList();
	}

	/**
	 * 根据 Key 列表获取Map的子Map
	 */
	public static <K, V> Map<K, V> subMapByKeys(Map<K, V> map, K[] keys) {
		Map<K, V> subMap = new HashMap<K, V>();
		for (K k : keys) {
			subMap.put(k, map.getOrDefault(k, null));
		}
		return subMap;

	}

	public static <T> boolean addIfNotContains(Collection<T> collection, T item) {
		if (!collection.contains(item)) {
			collection.add(item);
			return true;
		}
		return false;
	}

	public static <E> E getIndexOfElse(List<E> list, int index, E defaultValue) {
		Objects.requireNonNull(list);
		if (index < list.size()) {
			return list.get(index);
		} else {
			return defaultValue;
		}
	}

}
