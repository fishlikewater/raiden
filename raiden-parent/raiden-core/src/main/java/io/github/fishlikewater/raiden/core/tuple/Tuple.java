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
package io.github.fishlikewater.raiden.core.tuple;

import io.github.fishlikewater.raiden.core.exception.RaidenExceptionCheck;

import java.io.Serial;
import java.io.Serializable;

/**
 * {@code Tuple}
 * 元组
 *
 * @author zhangxiang
 * @version 1.0.8
 * @since 2024/11/29
 */
public abstract class Tuple implements Serializable {

    @Serial
    private final static long serialVersionUID = 1L;

    public abstract <T> T getField(int pos);

    public abstract <T> void setField(T t1, int pos);

    public abstract int getArity();

    public abstract <T extends Tuple> T copy();

    public <T> T getFieldNotNull(int pos) {
        T field = this.getField(pos);
        if (field != null) {
            return field;
        } else {
            return RaidenExceptionCheck.INSTANCE.throwUnchecked("not found filed");
        }
    }

    public static <T extends Tuple> Tuple newInstance(int arity) {
        return switch (arity) {
            case 2 -> new Tuple2<>();
            case 3 -> new Tuple3<>();
            default -> throw new IllegalArgumentException("The tuple arity must be in [2, 3].");
        };
    }
}