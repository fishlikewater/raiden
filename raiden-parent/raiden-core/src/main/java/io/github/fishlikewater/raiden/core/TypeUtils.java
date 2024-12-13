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

import java.lang.reflect.*;

/**
 * TypeUtils
 *
 * @author zhangxiang
 * @version 1.1.0
 * @since 2024/12/13
 **/
public final class TypeUtils {

    /**
     * 获取方法返回类型
     *
     * @param method 方法
     * @return 返回类型
     */
    public static Type getReturnType(Method method) {
        return method.getGenericReturnType();
    }

    /**
     * 获取类型Type 的 泛型类型
     *
     * @param type 类型
     * @return 泛型类型
     */
    public static Class<?> getGenericType(Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (ObjectUtils.isNotNullOrEmpty(actualTypeArguments)) {
                return getClass(actualTypeArguments[0]);
            }
        }
        return null;
    }

    /**
     * 获取类型Type 的 泛型类型
     *
     * @param type 类型
     * @return 泛型类型
     */
    public static Class<?> getGenericType(Type type, int index) {
        if (type instanceof ParameterizedType parameterizedType) {
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (ObjectUtils.isNotNullOrEmpty(actualTypeArguments) && index < actualTypeArguments.length) {
                return getClass(actualTypeArguments[index]);
            }
        }
        return null;
    }

    /**
     * 获取类型
     *
     * @param type 类型
     * @return 类型
     */
    public static Class<?> getClass(Type type) {
        if (null != type) {
            switch (type) {
                case Class<?> aClass -> {
                    return aClass;
                }
                case ParameterizedType parameterizedType -> {
                    return (Class<?>) parameterizedType.getRawType();
                }
                case TypeVariable<?> typeVariable -> {
                    Type[] bounds = typeVariable.getBounds();
                    if (bounds.length == 1) {
                        return getClass(bounds[0]);
                    }
                }
                case WildcardType wildcardType -> {
                    final Type[] upperBounds = wildcardType.getUpperBounds();
                    if (upperBounds.length == 1) {
                        return getClass(upperBounds[0]);
                    }
                }
                default -> {
                }
            }
        }
        return null;
    }
}
