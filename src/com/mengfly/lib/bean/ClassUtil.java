package com.mengfly.lib.bean;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassUtil {


    /**
     * TODO 添加是否有私有变量和静态变量属性获取的选项
     *
     * @param <E>
     * @param eClass
     * @param ignore
     * @return
     */
    public static <E> List<String> getProperties(Class<E> eClass, List<String> ignore) {
        return Arrays.stream(eClass.getDeclaredFields()).filter(field -> !ignore.contains(field.getName()))
                .map(Field::getName).collect(Collectors.toList());
    }

    /**
     * 获取对象指定属性的名称和值的Map
     *
     * @param entity 要获取Map的对象
     * @param fields 要获取的属性名称列表
     * @return map
     */
    public static <T> Map<String, Object> getPropertyMap(T entity, String[] fields) {
        Map<String, Object> m = new LinkedHashMap<>(fields.length);
        Class<?> cls = entity.getClass();
        for (String field : fields) {
            Object propertyValue = null;
            try {
                Field f = cls.getDeclaredField(field);
                f.setAccessible(true);
                propertyValue = f.get(entity);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            m.put(field, propertyValue);
        }
        return m;
    }

    public static void main(String[] args) {
        System.out.println(getPropertyMap(new ClassUtil(), getProperties(ClassUtil.class, Collections.emptyList()).toArray(new String[0])));
    }

}
