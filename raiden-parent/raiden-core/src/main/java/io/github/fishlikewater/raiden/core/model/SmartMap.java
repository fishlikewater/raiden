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
package io.github.fishlikewater.raiden.core.model;

import io.github.fishlikewater.raiden.core.LambdaUtils;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.exception.RaidenExceptionCheck;

import java.io.Serial;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * {@code SmartMap}
 *
 * @author zhangxiang
 * @version 1.1.3
 * @since 2025/01/24
 */
@SuppressWarnings("all")
public class SmartMap<K, V> extends HashMap<K, V> {

    @Serial
    private static final long serialVersionUID = 1752898875996629839L;

    public SmartMap() {
        super();
    }

    public SmartMap(Map<? extends K, ? extends V> m) {
        super(m);
    }

    /**
     * 获取SmartMap
     *
     * @param k   key
     * @param <R> key
     * @param <T> value
     * @return SmartMap
     */
    public <R, T> SmartMap<R, T> getMap(K k) {
        Object value = super.get(k);
        if (value == null) {
            return null;
        }

        if (value instanceof Map map) {
            return new SmartMap<>(map);
        }
        return RaidenExceptionCheck.INSTANCE.throwUnchecked("value for key {} is not a Map", k);
    }

    /**
     * 获取ListMap
     *
     * @param k   key
     * @param <T> value
     * @return ListMap
     */
    public <R, T> List<SmartMap<R, T>> getListMap(K k) {
        Object value = super.get(k);
        if (value == null) {
            return null;
        }
        if (value instanceof List list) {
            List convert = ObjectUtils.convert(value, List.class);
            return LambdaUtils.toList(convert, item -> new SmartMap<>((Map<R, T>) item));
        }

        return RaidenExceptionCheck.INSTANCE.throwUnchecked("value for key {} is not a List", k);
    }

    /**
     * 获取值
     *
     * @param k        key
     * @param function 函数
     * @param <T>      value
     * @return value
     */
    public <T> T getObject(K k, Function<Object, T> function) {
        return function.apply(get(k));
    }

    /**
     * 获取Integer
     *
     * @param k key
     * @return Integer
     */
    public Integer getInteger(K k) {
        return ObjectUtils.convert(get(k), Integer.class);
    }

    /**
     * 获取Integer
     *
     * @param k            key
     * @param defaultValue 默认值
     * @return Integer
     */
    public Integer getInteger(K k, Integer defaultValue) {
        return ObjectUtils.defaultIfNullOrEmpty(getInteger(k), defaultValue);
    }

    /**
     * 获取Long
     *
     * @param k key
     * @return Long
     */
    public Long getLong(K k) {
        return ObjectUtils.convert(get(k), Long.class);
    }

    /**
     * 获取Long
     *
     * @param k            key
     * @param defaultValue 默认值
     * @return Long
     */
    public Long getLong(K k, Long defaultValue) {
        return ObjectUtils.defaultIfNullOrEmpty(getLong(k), defaultValue);
    }

    /**
     * 获取String
     *
     * @param k key
     * @return String
     */
    public String getString(K k) {
        return ObjectUtils.convert(get(k), String.class);
    }

    /**
     * 获取String
     *
     * @param k            key
     * @param defaultValue 默认值
     * @return String
     */
    public String getString(K k, String defaultValue) {
        return ObjectUtils.defaultIfNullOrEmpty(getString(k), defaultValue);
    }

    /**
     * 获取Boolean
     *
     * @param k key
     * @return Boolean
     */
    public Boolean getBoolean(K k) {
        return ObjectUtils.convert(get(k), Boolean.class);
    }

    /**
     * 获取Double
     *
     * @param k            key
     * @param defaultValue 默认值
     * @return Boolean
     */
    public Double getDouble(K k) {
        return ObjectUtils.convert(get(k), Double.class);
    }

    /**
     * 获取Double
     *
     * @param k            key
     * @param defaultValue 默认值
     * @return Boolean
     */
    public Float getFloat(K k) {
        return ObjectUtils.convert(get(k), Float.class);
    }
}
