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
 * {@code DegradeException}
 * 熔断异常
 *
 * @author zhangxiang
 * @version 1.1.0
 * @since 2024/12/10
 */
public class DegradeException extends AbstractException {

    @Serial
    private static final long serialVersionUID = -8502072746385972181L;

    public DegradeException() {
        super();
    }

    public DegradeException(Throwable e) {
        super(e);
    }

    public DegradeException(StatusEnum status) {
        super(status);
    }

    public DegradeException(StatusEnum status, String message, Object... args) {
        super(status, message, args);
    }

    public DegradeException(String message, Object... args) {
        super(message, args);
    }

    public DegradeException(Throwable e, String message, Object... args) {
        super(e, message, args);
    }
}
