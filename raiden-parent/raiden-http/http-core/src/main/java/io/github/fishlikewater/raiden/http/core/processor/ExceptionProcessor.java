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

import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.http.core.constant.HttpConstants;
import io.github.fishlikewater.raiden.http.core.exception.HttpExceptionCheck;

import java.io.IOException;
import java.net.http.HttpRequest;
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

    /**
     * 处理无效响应
     *
     * @param request  请求
     * @param response 响应
     * @return 异常
     */
    default <T> RuntimeException invalidRespHandle(HttpRequest request, HttpResponse<T> response) {
        if (ObjectUtils.notEquals(response.statusCode(), HttpConstants.HTTP_OK)) {
            return HttpExceptionCheck.INSTANCE.throwUnchecked("请求失败, 响应码: {}", response.statusCode());
        }
        return null;
    }

    /**
     * 处理IO异常
     *
     * @param request 请求
     * @param cause   异常
     * @return 异常
     */
    default RuntimeException ioExceptionHandle(HttpRequest request, IOException cause) {
        return HttpExceptionCheck.INSTANCE.throwUnchecked(cause, "请求失败, 请求地址: {}", request.uri());
    }

    /**
     * 处理异常 (除IO异常之外的其他异常)
     *
     * @param request 请求
     * @param cause   异常
     * @return 异常
     */
    default RuntimeException exceptionHandle(HttpRequest request, Throwable cause) {
        return HttpExceptionCheck.INSTANCE.throwUnchecked(cause, "请求失败, 请求地址: {}", request.uri());
    }

    class DefaultExceptionProcessor implements ExceptionProcessor {}
}
