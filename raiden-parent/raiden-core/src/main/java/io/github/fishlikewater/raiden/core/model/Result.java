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
package io.github.fishlikewater.raiden.core.model;

import io.github.fishlikewater.raiden.core.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * {@code Result}
 * 统一返回数据格式
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.2
 * @since 2024年06月04日 22:15
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 5347148098024079057L;

    /**
     * 返回状态码
     */
    protected String code;

    /**
     * 返回提示消息
     */
    protected String message;

    /**
     * 返回数据
     */
    private T result;

    /**
     * 请求唯一编号
     */
    private String requestId;

    // ----------------------------------------------------------------

    public static <T> Result<T> of(String code, String message) {
        return Result.<T>builder()
                .code(code)
                .message(message)
                .build();
    }

    public static <T> Result<T> of(String code, T result) {
        return Result.<T>builder()
                .code(code)
                .result(result)
                .build();
    }

    public static <T> Result<T> of(String code, T result, String message) {
        return Result.<T>builder()
                .code(code)
                .message(message)
                .result(result)
                .build();
    }

    // ----------------------------------------------------------------

    public static <T> Result<T> of(T result) {
        return Result.<T>builder()
                .code(StatusEnum.OK.code())
                .message(StatusEnum.OK.message())
                .result(result)
                .build();
    }

    public static <T> Result<T> of(StatusEnum statusEnum, T result) {
        return Result.<T>builder()
                .code(statusEnum.code())
                .message(statusEnum.message())
                .result(result)
                .build();
    }
}
