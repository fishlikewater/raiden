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

/**
 * {@code RaidenExceptionCheck}
 * 异常检测器
 *
 * @author zhangxiang
 * @version 1.0.2
 * @since 2024/06/12
 */
public final class RaidenExceptionCheck extends AbstractExceptionCheck {

    public static final RaidenExceptionCheck INSTANCE = RaidenExceptionCheckHolder.check;

    private RaidenExceptionCheck() {

    }

    @Override
    protected AbstractException createException(Throwable e, String message, Object... args) {
        return new RaidenException(e, message, args);
    }

    public static class RaidenExceptionCheckHolder {

        public static RaidenExceptionCheck check = new RaidenExceptionCheck();
    }
}
