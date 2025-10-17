/*
 * Copyright (c) 2023-2025 zhangxiang (fishlikewater@126.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.fishlikewater.raiden.core;

import io.github.fishlikewater.raiden.core.exception.RaidenExceptionCheck;
import io.github.fishlikewater.raiden.core.func.LambdaFunction;
import io.github.fishlikewater.raiden.core.model.SmartMap;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;

/**
 * {@code ObjectUtils}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/04/30
 */
@SuppressWarnings("unused")
public final class ObjectUtils {

    /**
     * 判断对象是否为空
     *
     * @param target 对象
     * @return true:为空 false:不为空
     */
    public static <T> boolean isNullOrEmpty(T target) {
        if (null == target) {
            return true;
        } else if (target instanceof CharSequence) {
            return StringUtils.isBlank((CharSequence) target);
        } else {
            return isCollectionsSupportType(target) && CollectionUtils.isEmpty(target);
        }
    }

    /**
     * 判断对象或函数返回是否为空
     *
     * @param target 对象
     * @param fx     函数
     * @return true:不为空 false:为空
     */
    public static <T, R> boolean isNullOrEmpty(T target, Function<T, R> fx) {
        return isNullOrEmpty(target) || isNullOrEmpty(fx.apply(target));
    }

    /**
     * 如果对象为空，则返回默认值
     *
     * @param target       对象
     * @param defaultValue 默认值
     * @return 对象
     */
    public static <T> T defaultIfNullOrEmpty(T target, T defaultValue) {
        return isNullOrEmpty(target) ? defaultValue : target;
    }

    /**
     * 判断对象是否不为空
     *
     * @param target 对象
     * @return true:不为空 false:为空
     */
    public static <T> boolean isNotNullOrEmpty(T target) {
        return !isNullOrEmpty(target);
    }

    /**
     * 判断对象是否不为空
     *
     * @param target 对象
     * @param fx     函数
     * @return true:不为空 false:为空
     */
    public static <T, R> boolean isNotNullOrEmpty(T target, Function<T, R> fx) {
        if (!isNullOrEmpty(target)) {
            return !isNullOrEmpty(fx.apply(target));
        }

        return false;
    }

    /**
     * 判断两个对象是否相等
     *
     * @param t1 对象1
     * @param t2 对象2
     * @return true:相等 false:不相等
     */
    public static <T> boolean equals(T t1, T t2) {
        return Objects.equals(t1, t2);
    }

    /**
     * 判断两个对象是否不相等
     *
     * @param t1 对象1
     * @param t2 对象2
     * @return true:相等 false:不相等
     */
    public static <T> boolean notEquals(T t1, T t2) {
        return !Objects.equals(t1, t2);
    }

    /**
     * 要求对象不能为null
     *
     * @param obj 对象
     */
    public static <T> void requireNonNull(T obj) {
        Objects.requireNonNull(obj);
    }

    /**
     * 对象转Map
     *
     * @param object object
     * @return return
     */
    public static Map<String, Object> beanToMap(Object object, boolean ignoreNull) {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                field.setAccessible(true);
                if (ignoreNull && isNullOrEmpty(field.get(object))) {
                    continue;
                }
                map.put(field.getName(), field.get(object));
            }
        } catch (IllegalAccessException e) {
            RaidenExceptionCheck.INSTANCE.throwUnchecked(e);
        }

        return map;
    }

    /**
     * 对象转Map
     *
     * @param object object
     * @return {@code SmartMap}
     */
    public static SmartMap<String, Object> beanToSmartMap(Object object, boolean ignoreNull) {
        return new SmartMap<>(beanToMap(object, ignoreNull));
    }

    /**
     * Map转对象
     *
     * @param map       待转换的map
     * @param beanClass 目标对象类型
     * @return return
     */
    public static <T> T mapToBean(Map<?, ?> map, Class<T> beanClass) {
        try {
            T object = beanClass.getConstructor().newInstance();
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                if (map.containsKey(field.getName())) {
                    field.set(object, map.get(field.getName()));
                }
            }
            return object;
        } catch (Exception e) {
            return RaidenExceptionCheck.INSTANCE.throwUnchecked(e);
        }
    }

    /**
     * 对象转换
     *
     * @param target 待转换对象
     * @param clazz  目标类型
     * @param <T>    目标类型
     * @return return
     */
    @SuppressWarnings("all")
    public static <T> T convert(Object target, Class<T> clazz) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return RaidenExceptionCheck.INSTANCE.throwUnchecked("target is null");
        }

        if (clazz.isInstance(target)) {
            return clazz.cast(target);
        }

        if (String.class.isAssignableFrom(clazz)) {
            return clazz.cast(target.toString());
        }

        if (clazz.isPrimitive() || Number.class.isAssignableFrom(clazz)) {
            return convertPrimitive(target, clazz);
        }

        if (Boolean.class.isAssignableFrom(clazz)) {
            return clazz.cast(Boolean.valueOf(target.toString()));
        }

        if (Character.class.isAssignableFrom(clazz)) {
            return clazz.cast(target.toString().charAt(0));
        }

        if (target instanceof Map<?, ?> map) {
            return mapToBean(map, clazz);
        }

        return (T) target;
    }

    /**
     * 获取对象属性
     *
     * @param target 待获取对象
     * @param lambda 函数
     * @param <T>    目标对象类型
     * @return return
     */
    public static <T, R> T notNullGetter(R target, LambdaFunction<R, T> lambda) {
        return notNullGetter(target, lambda, null);
    }

    /**
     * 获取对象属性
     *
     * @param target 待获取对象
     * @param lambda 函数
     * @param <T>    目标对象类型
     * @param <R>    目标属性类型
     * @return return
     */
    public static <T, R> T notNullGetter(R target, LambdaFunction<R, T> lambda, T defaultValue) {
        return ObjectUtils.isNullOrEmpty(target) ? defaultValue : lambda.apply(target);
    }

    // ----------------------------------------------------------------

    /**
     * 转换基本类型
     *
     * @param target 待转换对象
     * @param clazz  目标类型
     * @param <T>    目标类型
     * @return return
     */
    @SuppressWarnings("all")
    private static <T> T convertPrimitive(Object target, Class<T> clazz) {
        if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
            return (T) (Integer.valueOf(target.toString()));
        }
        if (clazz.equals(long.class) || clazz.equals(Long.class)) {
            return (T) (Long.valueOf(target.toString()));
        }
        if (clazz.equals(float.class) || clazz.equals(Float.class)) {
            return (T) (Float.valueOf(target.toString()));
        }
        if (clazz.equals(double.class) || clazz.equals(Double.class)) {
            return (T) (Double.valueOf(target.toString()));
        }
        if (clazz.equals(byte.class) || clazz.equals(Byte.class)) {
            return (T) (Byte.valueOf(target.toString()));
        }
        if (clazz.equals(short.class) || clazz.equals(Short.class)) {
            return (T) (Short.valueOf(target.toString()));
        }
        return RaidenExceptionCheck.INSTANCE.throwUnchecked("Unsupported primitive type: {}", clazz);
    }

    private static boolean isCollectionsSupportType(Object target) {
        boolean isCollectionOrMap = target instanceof Collection || target instanceof Map;
        boolean isEnumerationOrIterator = target instanceof Enumeration || target instanceof Iterator;
        return isCollectionOrMap || isEnumerationOrIterator || target.getClass().isArray();
    }
}
