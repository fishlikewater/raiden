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
package io.github.fishlikewater.raiden.http.core.exception;

import io.github.fishlikewater.raiden.core.exception.AbstractException;
import io.github.fishlikewater.raiden.core.exception.AbstractExceptionCheck;

/**
 * <p>
 * {@code HttpExceptionCheck}
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.2
 * @since 2024年06月11日 22:30
 **/
public final class HttpExceptionCheck extends AbstractExceptionCheck {
    public static final HttpExceptionCheck INSTANCE = new HttpExceptionCheck();

    private HttpExceptionCheck() {

    }

    @Override
    protected AbstractException createException(Throwable e, String message, Object... args) {
        return new RaidenHttpException(e, message, args);
    }
}
