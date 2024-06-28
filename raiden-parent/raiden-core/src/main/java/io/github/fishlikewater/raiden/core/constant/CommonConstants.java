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
package io.github.fishlikewater.raiden.core.constant;

/**
 * {@code CommonConstants}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/04/30
 */
public interface CommonConstants {

    // ---------------------------------------------------------------- STRING

    String LAMBDA_FUNCTION_NAME = "writeReplace";
    String BOOLEAN_FIELD_START_WITH = "is";
    String GET_METHOD_START_WITH = "get";
    String SET_METHOD_START_WITH = "set";
    String SYMBOL_AND = "&";
    String SYMBOL_EXPRESSION = "#";
    String SYMBOL_OR = "|";
    String SYMBOL_PATH = "/";
    String HEX_PREFIX = "0x";
    String HEX_PREFIX_UPPERCASE = "0X";

    // ---------------------------------------------------------------- NUMBER

    /**
     * 默认缓存大小 8192
     */
    int DEFAULT_BUFFER_SIZE = 2 << 12;
    long MILLIS_UNIT = 1000L;
    int TIME_STAMP_LENGTH = 13;
    int INT_ZERO = 0;
    int INT_ONE = 1;
}
