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

import io.github.fishlikewater.raiden.core.enums.StatusEnum;
import io.github.fishlikewater.raiden.core.exception.AbstractException;

import java.io.Serial;

/**
 * {@code RaidenException}
 *
 * @author zhangxiang
 * @version 1.0.2
 * @since 2024/06/06
 */
public class RaidenHttpException extends AbstractException {

    @Serial
    private static final long serialVersionUID = -6722279357552749937L;

    public RaidenHttpException() {
        super();
    }

    public RaidenHttpException(Throwable e) {
        super(e);
    }

    public RaidenHttpException(StatusEnum status) {
        super(status);
    }

    public RaidenHttpException(StatusEnum status, String message, Object... args) {
        super(status, message, args);
    }

    public RaidenHttpException(String message, Object... args) {
        super(message, args);
    }

    public RaidenHttpException(Throwable e, String message, Object... args) {
        super(e, message, args);
    }
}
