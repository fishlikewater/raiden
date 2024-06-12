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
package io.github.fishlikewater.raiden.core.exception;

import io.github.fishlikewater.raiden.core.ObjectUtils;

/**
 * <p>
 * {@code ExceptionCheck}
 * </p>
 * 检查与抛出异常
 *
 * @author fishlikewater@126.com
 * @version 1.0.2
 * @since 2024年06月11日 21:05
 **/
@SuppressWarnings("all")
public abstract class AbstractExceptionCheck {

    /**
     * 创建异常
     *
     * @param message 异常信息
     * @param args    参数
     * @return {@code AbstractException}
     */
    protected AbstractException createException(String message, Object... args) {
        return this.createException(null, message, args);
    }

    protected AbstractException createException(Throwable e) {
        return this.createException(e, null);
    }

    /**
     * 创建异常
     *
     * @param e       异常
     * @param message 异常信息
     * @param args    参数
     * @return {@code AbstractException}
     */
    protected abstract AbstractException createException(Throwable e, String message, Object... args);

    /**
     * 检查是否为空
     *
     * @param object  对象
     * @param message 异常信息
     * @param args    参数
     * @param <T>     泛型
     */
    public <T> void isNull(T object, String message, Object... args) {
        if (ObjectUtils.isNotNullOrEmpty(object)) {
            throw createException(message, args);
        }
    }

    public <T> void isNotNull(T object, String message, Object... args) {
        if (ObjectUtils.isNullOrEmpty(object)) {
            throw createException(message, args);
        }
    }

    public <T> void notEquals(T t1, T t2, String message, Object... args) {
        if (ObjectUtils.equals(t1, t2)) {
            throw createException(message, args);
        }
    }

    public <T> void equals(T t1, T t2, String message, Object... args) {
        if (ObjectUtils.notEquals(t1, t2)) {
            throw createException(message, args);
        }
    }

    public void isFalse(boolean expression, String message, Object... args) {
        if (expression) {
            throw createException(message, args);
        }
    }

    public void isTrue(boolean expression, String message, Object... args) {
        if (!expression) {
            throw createException(message, args);
        }
    }

    public <T> T throwUnchecked(Throwable e) {
        throw createException(e);
    }

    public <T> T throwUnchecked(String message, Object... args) {
        throw createException(message, args);
    }

    public <T> T throwUnchecked(Throwable e, String message, Object... args) {
        throw createException(e, message, args);
    }
}
