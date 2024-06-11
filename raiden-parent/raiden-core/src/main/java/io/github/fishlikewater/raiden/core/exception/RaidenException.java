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

import java.io.Serial;

/**
 * {@code RaidenException}
 *
 * @author zhangxiang
 * @version 1.0.2
 * @since 2024/06/06
 */
public class RaidenException extends AbstractException {

    @Serial
    private static final long serialVersionUID = -6722279357552749937L;

    public RaidenException() {
        super();
    }

    public RaidenException(Throwable e) {
        super(e);
    }

    public RaidenException(ExceptionStatusEnum status) {
        super(status);
    }

    public RaidenException(ExceptionStatusEnum status, String message, Object... args) {
        super(status, message, args);
    }

    public RaidenException(String message, Object... args) {
        super(message, args);
    }

    public RaidenException(Throwable e, String message, Object... args) {
        super(e, message, args);
    }

    @Override
    public AbstractException creat(String message, Object... args) {
        return new RaidenException(message, args);
    }
}
