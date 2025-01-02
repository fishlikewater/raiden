/*
 * Copyright (c) 2025 zhangxiang (fishlikewater@126.com)
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

import java.util.ArrayList;
import java.util.List;

/**
 * {@code EnumUtils}
 * 枚举工具类
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/28
 */
@SuppressWarnings("unused")
public class EnumUtils {

    /**
     * 判断是否是枚举
     *
     * @param clazz 类
     * @return 是否是枚举
     */
    public static boolean isEnum(Class<?> clazz) {
        Assert.notNull(clazz, "is null");
        return clazz.isEnum();
    }

    /**
     * 指定类是否为Enum类
     *
     * @param obj 类
     * @return 是否为Enum类
     */
    public static boolean isEnum(Object obj) {
        Assert.notNull(obj, "is null");
        return obj.getClass().isEnum();
    }

    /**
     * 字符串转枚举，调用{@link Enum#valueOf(Class, String)}
     *
     * @param <E>       枚举类型泛型
     * @param enumClass 枚举类
     * @param value     值
     * @return 枚举值
     */
    public static <E extends Enum<E>> E fromString(Class<E> enumClass, String value) {
        return Enum.valueOf(enumClass, value);
    }

    /**
     * 字符串转枚举，调用{@link Enum#valueOf(Class, String)}<br>
     * 如果无枚举值，返回默认值
     *
     * @param <E>          枚举类型泛型
     * @param enumClass    枚举类
     * @param value        值
     * @param defaultValue 无对应枚举值返回的默认值
     * @return 枚举值
     */
    public static <E extends Enum<E>> E fromString(Class<E> enumClass, String value, E defaultValue) {
        return ObjectUtils.defaultIfNullOrEmpty(fromStringQuietly(enumClass, value), defaultValue);
    }

    /**
     * 字符串转枚举，调用{@link Enum#valueOf(Class, String)}，转换失败返回{@code null} 而非报错
     *
     * @param <E>       枚举类型泛型
     * @param enumClass 枚举类
     * @param value     值
     * @return 枚举值
     */
    public static <E extends Enum<E>> E fromStringQuietly(Class<E> enumClass, String value) {
        if (null == enumClass || StringUtils.isBlank(value)) {
            return null;
        }

        try {
            return fromString(enumClass, value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * 枚举类中所有枚举对象的name列表
     *
     * @param clazz 枚举类
     * @return name列表
     */
    public static List<String> getNames(Class<? extends Enum<?>> clazz) {
        final Enum<?>[] enums = clazz.getEnumConstants();
        if (null == enums) {
            return null;
        }
        final List<String> list = new ArrayList<>(enums.length);
        for (Enum<?> e : enums) {
            list.add(e.name());
        }
        return list;
    }
}
