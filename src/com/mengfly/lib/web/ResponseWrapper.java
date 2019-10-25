package com.mengfly.lib.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mengfly.lib.CollectionUtil;
import com.mengfly.lib.bean.ClassUtil;

/**
 * 服务器返回对象包装类，可以自定义返回对象应该包含的属性，而不必须将所有的信息都进行返回 如果想要忽略某些属性，不进行返回那么可以调用
 * {@link #ignoreAdd(String...)} 对要忽略的属性进行添加
 * 
 * 同时可以指定接受那些属性值进行返回，需要注意的是，接受值的优先级高于忽略值的优先级，一旦设置了接受值，那么忽略值的设置将会在接受值列表的基础上进行设置
 * 
 * 并且支持将两个Wapper对象的数据进行整合使用，在调用{@link #and(ResponseWrapper)} 之后即可实现
 * 整合使用的Wapper仅对整合规则有效 ，不对整合数据有效
 * 
 * 另外也支持为我们即将返回的对象添加额外的数据，这时候我们只需调用{@link #addExtra(Object, Map)} 即可实现
 * 
 * 
 * 
 * Create By wangp At 2018/11/15 10:47
 */
public class ResponseWrapper<T> {
	private T bean;

	private Map<String, Object> extra;
	private List<String> ignoreBeanFields;
	private List<String> accessBeanFields;

	public ResponseWrapper(T bean) {
		this();
		this.bean = bean;
		this.addExtra(bean, extra);
		this.ignoreBeanFields.addAll(Arrays.asList(ignoreBeanFields()));
		this.accessBeanFields.addAll(Arrays.asList(accessBeanFields()));
	}

	public ResponseWrapper() {
		this.extra = new LinkedHashMap<>();
		ignoreBeanFields = new ArrayList<>();
		accessBeanFields = new ArrayList<>();
	}

	public void addExtra(T bean, Map<String, Object> map) {
	}

	public String[] ignoreBeanFields() {
		return new String[0];
	}

	public String[] accessBeanFields() {
		return new String[0];
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getResult() {
		if (bean == null) {
			return null;
		}
		Map<String, Object> fieldValueMap;
		String[] propertys;
		if (accessBeanFields.isEmpty()) {
			propertys = ClassUtil.getProperties(bean.getClass(), ignoreBeanFields).toArray(new String[0]);
		} else {
			propertys = accessBeanFields.stream().filter(s -> !ignoreBeanFields.contains(s))
					.collect(Collectors.toList()).toArray(new String[0]);
		}
		if (bean instanceof Map) {
			fieldValueMap = (Map<String, Object>) CollectionUtil.subMapByKeys((Map<String, ?>) bean, propertys);
			
		} else {
			fieldValueMap = ClassUtil.getPropertyMap(bean, propertys);
		}
		fieldValueMap.putAll(extra);
		return fieldValueMap;
	}

	public ResponseWrapper<T> and(ResponseWrapper<T> other) {
		this.extra.putAll(other.extra);
		this.ignoreAdd(other.ignoreBeanFields.toArray(new String[0]));
		this.accessAdd(other.accessBeanFields.toArray(new String[0]));
		return this;
	}

	public void ignoreAdd(String... add) {
		this.ignoreBeanFields.addAll(Arrays.asList(add));
	}

	public void accessAdd(String... add) {
		this.accessBeanFields.addAll(Arrays.asList(add));
	}

}
