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

import java.io.Serial;

/**
 * {@code Tuple3}
 *
 * @author zhangxiang
 * @version 1.0.8
 * @since 2024/11/29
 */
public class Tuple3<T0, T1, T2> extends Tuple {

    @Serial
    private static final long serialVersionUID = 2026145762850335475L;

    public T0 f0;
    public T1 f1;
    public T2 f2;

    public Tuple3() {
    }

    public Tuple3(T0 f0, T1 f1, T2 f2) {
        this.f0 = f0;
        this.f1 = f1;
        this.f2 = f2;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getField(int pos) {
        return switch (pos) {
            case 0 -> (T) this.f0;
            case 1 -> (T) this.f1;
            case 2 -> (T) this.f2;
            default -> throw new IndexOutOfBoundsException(String.valueOf(pos));
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void setField(T t1, int pos) {
        switch (pos) {
            case 0 -> this.f0 = (T0) t1;
            case 1 -> this.f1 = (T1) t1;
            case 2 -> this.f2 = (T2) t1;
            default -> throw new IndexOutOfBoundsException(String.valueOf(pos));
        }
    }

    @Override
    public int getArity() {
        return 3;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Tuple3<T0, T1, T2> copy() {
        return new Tuple3<>(this.f0, this.f1, this.f2);
    }


    public void setFields(T0 f0, T1 f1, T2 f2) {
        this.f0 = f0;
        this.f1 = f1;
        this.f2 = f2;
    }

    public static <T0, T1, T2> Tuple3<T0, T1, T2> of(T0 f0, T1 f1, T2 f2) {
        return new Tuple3<>(f0, f1, f2);
    }
}
