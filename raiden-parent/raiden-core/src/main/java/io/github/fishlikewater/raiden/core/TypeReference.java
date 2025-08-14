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

import lombok.Getter;
import lombok.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * {@code TypeReference}
 * 泛型传递
 *
 * @author zhangxiang
 * @since 2025/6/26
 */
@Getter
public abstract class TypeReference<T> implements Comparable<TypeReference<T>> {

    protected final Type type;

    protected TypeReference() {
        Type superClass = this.getClass().getGenericSuperclass();
        if (superClass instanceof Class) {
            throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
        } else {
            this.type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        }
    }

    @Override
    public int compareTo(@NonNull TypeReference<T> o) {return 0;}

    public Class<?> getRawType() {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        }
        throw new IllegalArgumentException("not support：" + type);
    }
}
