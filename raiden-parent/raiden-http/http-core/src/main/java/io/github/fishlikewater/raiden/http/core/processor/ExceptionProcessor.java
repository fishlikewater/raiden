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
package io.github.fishlikewater.raiden.http.core.processor;

import io.github.fishlikewater.raiden.http.core.RequestWrap;

import java.io.IOException;
import java.net.http.HttpResponse;

/**
 * {@code ExceptionProcessor}
 * 异常处理器
 *
 * @author zhangxiang
 * @version 1.0.2
 * @since 2024/06/12
 */
public interface ExceptionProcessor {

    default <T> void exceptionHandle(RequestWrap requestWrap, HttpResponse<T> response, Throwable cause) {
        if (cause instanceof IOException ioException) {
            this.ioExceptionHandle(requestWrap, response, ioException);
        } else {
            this.otherExceptionHandle(requestWrap, response, cause);
        }
    }

    /**
     * 处理无效响应
     *
     * @param requestWrap 请求
     * @param response    响应
     */
    <T> void invalidRespHandle(RequestWrap requestWrap, HttpResponse<T> response);

    /**
     * 处理IO异常
     *
     * @param requestWrap 请求
     * @param response    响应
     * @param cause       异常
     */
    <T> void ioExceptionHandle(RequestWrap requestWrap, HttpResponse<T> response, IOException cause);

    /**
     * 处理异常 (除IO异常之外的其他异常)
     *
     * @param requestWrap 请求
     * @param response    响应
     * @param cause       异常
     */
    <T> void otherExceptionHandle(RequestWrap requestWrap, HttpResponse<T> response, Throwable cause);
}
