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
package io.github.fishlikewater.raiden.core.exception;

import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.StringUtils;
import io.github.fishlikewater.raiden.core.enums.StatusEnum;

import java.io.Serial;

/**
 * {@code AbstractException}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/04/30
 */
@SuppressWarnings("unused")
public abstract class AbstractException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "system.error";

    @Serial
    private static final long serialVersionUID = 5881311899429280508L;

    protected Integer status;

    protected String code;

    protected String message;

    public AbstractException() {
        super(DEFAULT_MESSAGE);
        this.status = StatusEnum.BAD_REQUEST.status();
        this.code = StatusEnum.BAD_REQUEST.code();
        this.message = DEFAULT_MESSAGE;
    }

    public AbstractException(Throwable e) {
        super(DEFAULT_MESSAGE, e);
        this.status = StatusEnum.BAD_REQUEST.status();
        this.code = StatusEnum.BAD_REQUEST.code();
        this.message = DEFAULT_MESSAGE;
    }

    public AbstractException(StatusEnum status) {
        super(status.message());
        this.status = status.status();
        this.code = status.code();
        this.message = status.message();
    }

    public AbstractException(StatusEnum status, String message, Object... args) {
        super(StringUtils.format(ObjectUtils.isNotNullOrEmpty(message) ? message : DEFAULT_MESSAGE, args));
        this.message = StringUtils.format(ObjectUtils.isNotNullOrEmpty(message) ? message : DEFAULT_MESSAGE, args);
        this.status = status.status();
        this.code = status.code();
    }

    public AbstractException(String message, Object... args) {
        super(StringUtils.format(ObjectUtils.isNotNullOrEmpty(message) ? message : DEFAULT_MESSAGE, args));
        this.message = StringUtils.format(ObjectUtils.isNotNullOrEmpty(message) ? message : DEFAULT_MESSAGE, args);
        this.status = StatusEnum.BAD_REQUEST.status();
        this.code = StatusEnum.BAD_REQUEST.code();
    }

    public AbstractException(Throwable e, String message, Object... args) {
        super(StringUtils.format(ObjectUtils.isNotNullOrEmpty(message) ? message : DEFAULT_MESSAGE, args), e);
        this.message = StringUtils.format(ObjectUtils.isNotNullOrEmpty(message) ? message : DEFAULT_MESSAGE, args);
        this.status = StatusEnum.BAD_REQUEST.status();
        this.code = StatusEnum.BAD_REQUEST.code();
    }
}
