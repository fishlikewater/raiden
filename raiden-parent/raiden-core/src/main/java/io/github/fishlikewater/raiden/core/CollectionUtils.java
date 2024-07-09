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

import io.github.fishlikewater.raiden.core.exception.RaidenExceptionCheck;

import java.lang.reflect.Array;
import java.util.*;

/**
 * {@code CollectionUtils}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/04/30
 */
public final class CollectionUtils {

    /**
     * 构建集合
     */
    @SafeVarargs
    public static <T> List<T> ofList(T... t) {
        return new ArrayList<>(Arrays.asList(t));
    }

    /**
     * 获取空的集合
     */
    public static <T> List<T> emptyList() {
        return Collections.emptyList();
    }

    /**
     * 构建集合
     */
    public static <T> List<T> newList() {
        return new ArrayList<T>();
    }

    /**
     * 获取空的集合
     */
    public static <T> Set<T> emptySet() {
        return Collections.emptySet();
    }

    /**
     * 获取空的MAP
     */
    public static <K, T> Map<K, T> emptyMap() {
        return Collections.emptyMap();
    }

    public static <T> List<T> sort(Collection<T> collection, Comparator<T> comparator) {
        if (collection instanceof List<T> list) {
            list.sort(comparator);
            return list;
        }
        if (collection instanceof SortedSet<T> sortedSet) {
            return sortedSet
                    .stream()
                    .sorted(comparator)
                    .toList();
        }
        return RaidenExceptionCheck.INSTANCE.throwUnchecked("not support!!!");
    }

    /**
     * 获取集合的第一个元素
     *
     * @param collection 集合
     * @param <T>        泛型
     * @return 第一个元素
     */
    public static <T> T getFirst(Collection<T> collection) {
        return isNotEmpty(collection) ? collection.iterator().next() : null;
    }

    /**
     * 判断对象是否为空
     *
     * @param object 对象
     * @return 是否为空
     */
    public static boolean isEmpty(Object object) {
        switch (object) {
            case null -> {
                return true;
            }
            case Collection<?> coll -> {
                return coll.isEmpty();
            }
            case Iterable<?> iterable -> {
                return !iterable.iterator().hasNext();
            }
            case Map<?, ?> map -> {
                return map.isEmpty();
            }
            case Object[] objects -> {
                return objects.length == 0;
            }
            case Iterator<?> iterator -> {
                return !iterator.hasNext();
            }
            case Enumeration<?> enumeration -> {
                return !enumeration.hasMoreElements();
            }
            default -> {
                try {
                    return Array.getLength(object) == 0;
                } catch (IllegalArgumentException var2) {
                    throw new IllegalArgumentException(StringUtils.format("Unsupported object type: {}", object.getClass().getName()));
                }
            }
        }
    }

    /**
     * 判断对象是否不为空
     *
     * @param object 对象
     * @return 是否不为空
     */
    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }

    /**
     * 改变数组大小
     *
     * @param bytes   原数组
     * @param newSize 新大小
     * @return 新数组
     */
    public static byte[] resize(byte[] bytes, int newSize) {
        if (newSize < 0) {
            return bytes;
        }
        final byte[] newArray = new byte[newSize];
        if (newSize > 0 && isNotEmpty(bytes)) {
            System.arraycopy(bytes, 0, newArray, 0, Math.min(bytes.length, newSize));
        }
        return newArray;
    }
}
