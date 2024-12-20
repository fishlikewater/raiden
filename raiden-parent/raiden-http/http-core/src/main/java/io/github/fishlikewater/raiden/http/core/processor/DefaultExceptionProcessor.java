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
import io.github.fishlikewater.raiden.http.core.exception.RaidenHttpException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.http.HttpResponse;

/**
 * DefaultExceptionProcessor
 *
 * @author zhangxiang
 * @version 1.0.8
 * @since 2024/11/10
 **/
@Slf4j
public class DefaultExceptionProcessor implements ExceptionProcessor {

    @Override
    public <T> void invalidRespHandle(RequestWrap requestWrap, HttpResponse<T> response) {
        log.error("request failed, response status code: {}", response.statusCode());
    }

    @Override
    public <T> void ioExceptionHandle(RequestWrap requestWrap, HttpResponse<T> response, IOException cause) {
        log.error("request failed, request address url: {}", requestWrap.getHttpRequest().uri(), cause);
        throw new RaidenHttpException("request failed, request address url: {}", requestWrap.getHttpRequest().uri(), cause);
    }

    @Override
    public <T> void otherExceptionHandle(RequestWrap requestWrap, HttpResponse<T> response, Throwable cause) {
        log.error("request failed, request address url: {}", requestWrap.getHttpRequest().uri(), cause);
        throw new RaidenHttpException("request failed, request address url: {}", requestWrap.getHttpRequest().uri(), cause);
    }
}