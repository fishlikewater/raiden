/*
 * Copyright (c) 2024 zhangxiang (fishlikewater@126.com)
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

import cn.hutool.core.util.StrUtil;
import io.github.fishlikewater.raiden.core.constant.CommonConstants;
import io.github.fishlikewater.raiden.core.func.LambdaFunction;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@code LambdaUtils}
 * lambda工具栏
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/07/09
 */
public class LambdaUtils {

    /**
     * 将方法名转换为属性名
     *
     * @param fx 方法
     * @return 属性名
     */

    public static <T, R> String resolve(LambdaFunction<T, R> fx) {
        try {
            Method method = fx.getClass().getDeclaredMethod(CommonConstants.LAMBDA_FUNCTION_NAME);
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(fx);
            String methodName = serializedLambda.getImplMethodName();
            return methodToProperty(methodName);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 过滤
     *
     * @param collection 待过滤集合
     * @param predicate  过滤条件
     * @return 集合
     */
    public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        return collection
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * 将集合中的元素映射到新的集合中
     *
     * @param collection 集合
     * @param mapper     映射函数
     * @param <T>        新集合元素类型
     * @param <E>        旧集合元素类型
     * @return 新集合
     */
    public static <T, E> List<T> toList(Collection<E> collection, Function<E, T> mapper) {
        return collection
                .stream()
                .map(mapper)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 将集合中的元素映射到新的集合中，并去重
     *
     * @param collection 集合
     * @param mapper     映射函数
     * @param <T>        新集合元素类型
     * @param <E>        旧集合元素类型
     * @return 新集合
     */
    public static <T, E> List<T> toList(Collection<E> collection, Function<E, T> mapper, boolean distinct) {
        Stream<T> tStream = collection
                .stream()
                .map(mapper)
                .filter(Objects::nonNull);
        if (distinct) {
            return tStream.distinct().collect(Collectors.toList());
        }
        return tStream.collect(Collectors.toList());
    }

    /**
     * 将集合中的元素映射到新的集合中
     *
     * @param collection 集合
     * @param mapper     映射函数
     * @param <T>        新集合元素类型
     * @param <E>        旧集合元素类型
     * @return 新集合
     */
    public static <T, E> Set<T> toSet(Collection<E> collection, Function<E, T> mapper) {
        return collection
                .stream()
                .map(mapper)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * 找到集合中第一个元素
     *
     * @param collection 集合
     * @param <T>        集合元素类型
     * @return 找到的元素
     */
    public static <T> T findFirst(Collection<T> collection) {
        return collection
                .stream()
                .findFirst()
                .orElse(null);
    }


    /**
     * 找到集合中第一个满足条件的元素
     *
     * @param collection 集合
     * @param predicate  条件
     * @param <T>        集合元素类型
     * @return 找到的元素
     */
    public static <T> T findFirst(Collection<T> collection, Predicate<T> predicate) {
        return collection
                .stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    /**
     * 找到集合中第一个满足条件的元素，如果没有找到则返回默认值
     *
     * @param collection   集合
     * @param predicate    条件
     * @param defaultValue 默认值
     * @param <T>          集合元素类型
     * @return 找到的元素
     */
    public static <T> T findFirst(Collection<T> collection, Predicate<T> predicate, T defaultValue) {
        return collection
                .stream()
                .filter(predicate)
                .findFirst()
                .orElse(defaultValue);
    }

    /**
     * 找到集合中任意一个满足条件的元素
     *
     * @param collection 集合
     * @param predicate  条件
     * @param <T>        集合元素类型
     * @return 找到的元素
     */
    public static <T> T findAny(Collection<T> collection, Predicate<T> predicate) {
        return collection
                .stream()
                .parallel()
                .filter(predicate)
                .findAny()
                .orElse(null);
    }

    /**
     * 找到集合中任意一个满足条件的元素，如果没有找到则返回默认值
     *
     * @param collection   集合
     * @param predicate    条件
     * @param defaultValue 默认值
     * @param <T>          集合元素类型
     * @return 找到的元素
     */
    public static <T> T findAny(Collection<T> collection, Predicate<T> predicate, T defaultValue) {
        return collection
                .stream()
                .parallel()
                .filter(predicate)
                .findAny()
                .orElse(defaultValue);
    }

    /**
     * @param collection 集合
     * @param predicate  条件
     * @return 是否匹配到
     */
    public static <T> boolean anyMatch(Collection<T> collection, Predicate<T> predicate) {
        return collection
                .stream()
                .anyMatch(predicate);
    }

    /**
     * @param collection 集合
     * @param predicate  条件
     * @return 是否匹配到
     */
    public static <T> boolean allMatch(Collection<T> collection, Predicate<T> predicate) {
        return collection
                .stream()
                .allMatch(predicate);
    }

    /**
     * 对集合进行排序
     *
     * @param collection 集合
     * @param comparator 比较器
     * @param <T>        集合元素类型
     * @return 排序后的集合
     */
    public static <T> List<T> sort(Collection<T> collection, Comparator<T> comparator) {
        return CollectionUtils.sort(collection, comparator);
    }

    /**
     * 对集合进行分组
     *
     * @param collection 集合
     * @param groupFunc  分组函数
     * @param <T>        集合元素类型
     * @param <K>        分组键类型
     * @return 分组后的Map
     */
    public static <T, K> Map<K, List<T>> groupBy(Collection<T> collection, Function<T, K> groupFunc) {
        return collection
                .stream()
                .collect(Collectors.groupingBy(groupFunc));
    }

    /**
     * 合并多个集合
     *
     * @param collections 将要合并的集合
     * @param <T>         集合元素类型
     * @return 合并后的集合
     */
    @SafeVarargs
    public static <T> List<T> combined(Collection<T>... collections) {
        if (collections == null || collections.length == 0) {
            return CollectionUtils.newList();
        }
        return Stream.of(collections)
                .flatMap(Collection<T>::stream)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 求交集
     *
     * @param collections 将要求交集的集合
     * @param <T>         集合元素类型
     * @return 求交集后的集合
     */
    @SafeVarargs
    public static <T> List<T> intersection(Collection<T>... collections) {
        return Stream.of(collections)
                .skip(1)
                .distinct()
                .toList()
                .stream()
                .flatMap(Collection<T>::stream)
                .filter(collections[0]::contains)
                .collect(Collectors.toList());
    }

    /**
     * 统计集合中的元素
     *
     * @param collection 集合
     * @return 统计结果
     */
    public static IntSummaryStatistics statisticsInt(Collection<Integer> collection) {
        return collection.stream()
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .summaryStatistics();
    }

    /**
     * 统计集合中的元素
     *
     * @param collection 集合
     * @return 统计结果
     */
    public static LongSummaryStatistics statisticsLong(Collection<Long> collection) {
        return collection.stream()
                .filter(Objects::nonNull)
                .mapToLong(Long::longValue)
                .summaryStatistics();
    }

    /**
     * 求和
     *
     * @param collection 集合
     * @return 求和结果
     */
    @SuppressWarnings("all")
    public static <T extends Number> T sum(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException("Collection must not be null or empty");
        }

        T firstElement = collection.iterator().next();

        return switch (firstElement) {
            case Integer ignored -> (T) (Integer) collection.stream()
                    .mapToInt(Number::intValue)
                    .sum();
            case Long ignored -> (T) (Long) collection.stream()
                    .mapToLong(Number::longValue)
                    .sum();
            case Double ignored -> (T) (Double) collection.stream()
                    .mapToDouble(Number::doubleValue)
                    .sum();
            case BigDecimal ignored -> (T) collection.stream()
                    .map(BigDecimal.class::cast)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            default -> throw new UnsupportedOperationException("Type not supported");
        };
    }

    /**
     * 求最小值
     *
     * @param collection 集合
     * @return 最小值
     */
    @SuppressWarnings("all")
    public static <T extends Number> T min(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException("Collection must not be null or empty");
        }
        T firstElement = collection.iterator().next();
        return switch (firstElement) {
            case Integer ignored -> (T) (Integer) collection.stream()
                    .mapToInt(Number::intValue)
                    .reduce((int) firstElement, Math::min);
            case Long ignored -> (T) (Long) collection.stream()
                    .mapToLong(Number::longValue)
                    .reduce((long) firstElement, Math::min);
            case Double ignored -> (T) (Double) collection.stream()
                    .mapToDouble(Number::doubleValue)
                    .reduce((double) firstElement, Math::min);
            case BigDecimal ignored -> (T) collection.stream()
                    .map(BigDecimal.class::cast)
                    .reduce((BigDecimal) firstElement, BigDecimal::min);
            default -> throw new UnsupportedOperationException("Type not supported");
        };
    }

    // ---------------------------------------------------------------- private

    /**
     * 把方法名转换成字段名
     *
     * @param name 方法名
     * @return 字段名
     */
    private static String methodToProperty(String name) {
        if (name.startsWith(CommonConstants.BOOLEAN_FIELD_START_WITH)) {
            name = name.substring(2);
        } else if (name.startsWith(CommonConstants.GET_METHOD_START_WITH) || name.startsWith(CommonConstants.SET_METHOD_START_WITH)) {
            name = name.substring(3);
        }

        if (StrUtil.isNotBlank(name) && !Character.isUpperCase(name.charAt(1))) {
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }

        return name;
    }
}
