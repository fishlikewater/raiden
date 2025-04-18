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
package io.github.fishlikewater.raiden.core.enums;

import io.github.fishlikewater.raiden.core.StringUtils;
import io.github.fishlikewater.raiden.core.exception.RaidenExceptionCheck;

/**
 * {@code SortEnum}
 * 排序方式枚举
 *
 * @author zhangxiang
 * @since 1.1.5
 */
public enum SortEnum {

    ASC("asc", "升序"),
    DESC("desc", "降序");

    private final String alias;
    private final String message;

    SortEnum(String alias, String message) {
        this.alias = alias;
        this.message = message;
    }

    public String alias() {
        return alias;
    }

    public String message() {
        return message;
    }

    public static SortEnum resolve(String alias) {
        for (SortEnum sortEnum : values()) {
            if (StringUtils.equals(alias, sortEnum.alias)) {
                return sortEnum;
            }
        }
        return RaidenExceptionCheck.INSTANCE.throwUnchecked("this.alias.don't.support");
    }
}
