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
    private T data;

    /**
     * 请求唯一编号
     */
    private String requestId;

    // ----------------------------------------------------------------

    public static <T> Result<T> of(String code, String message) {
        return of(code, null, message);
    }

    public static <T> Result<T> of(String code, T data) {
        return of(code, data, null);
    }


    public static <T> Result<T> of(T data) {
        return of(StatusEnum.OK, data);
    }

    // ----------------------------------------------------------------

    public static Result<Void> ok() {
        return ok(null);
    }

    public static <T> Result<T> ok(T data) {
        return of(data);
    }

    public static Result<Void> fail() {
        return fail(null);
    }

    public static <T> Result<T> fail(T data) {
        return of(StatusEnum.INNER_ERROR, data);
    }

    // ----------------------------------------------------------------

    public static <T> Result<T> of(String code, T data, String message) {
        return Result.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> Result<T> of(StatusEnum statusEnum, T data) {
        return Result.<T>builder()
                .code(statusEnum.code())
                .message(statusEnum.message())
                .data(data)
                .build();
    }
}
