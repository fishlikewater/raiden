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
package com.github.fishlikewater.raiden.core;

import java.util.*;

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
     * 判断对象是否不为空
     *
     * @param target 对象
     * @return true:不为空 false:为空
     */
    public static <T> boolean isNotNullOrEmpty(T target) {
        return !isNullOrEmpty(target);
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
     * 要求对象不能为null
     *
     * @param obj 对象
     */
    public static <T> void requireNonNull(T obj) {
        Objects.requireNonNull(obj);
    }

    private static boolean isCollectionsSupportType(Object target) {
        boolean isCollectionOrMap = target instanceof Collection || target instanceof Map;
        boolean isEnumerationOrIterator = target instanceof Enumeration || target instanceof Iterator;
        return isCollectionOrMap || isEnumerationOrIterator || target.getClass().isArray();
    }
}
