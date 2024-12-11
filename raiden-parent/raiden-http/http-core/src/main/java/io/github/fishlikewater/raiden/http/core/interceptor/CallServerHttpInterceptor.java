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
package io.github.fishlikewater.raiden.http.core.interceptor;

import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.http.core.RequestWrap;
import io.github.fishlikewater.raiden.http.core.Response;
import io.github.fishlikewater.raiden.http.core.client.AbstractHttpRequestClient;
import io.github.fishlikewater.raiden.http.core.exception.HttpExceptionCheck;
import io.github.fishlikewater.raiden.http.core.processor.ExceptionProcessor;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * {@code CallServerInterceptor}
 * 真正发起服务调用
 *
 * @author zhangxiang
 * @version 1.1.0
 * @since 2024/12/09
 */
public class CallServerHttpInterceptor implements HttpInterceptor {

    private final AbstractHttpRequestClient client;

    public CallServerHttpInterceptor(AbstractHttpRequestClient client) {
        this.client = client;
    }

    @Override
    public Response intercept(Chain chain) {
        RequestWrap requestWrap = chain.requestWrap();
        if (requestWrap.isSync()) {
            return this.sync(requestWrap);
        } else {
            return this.async(requestWrap);
        }
    }

    private Response sync(RequestWrap requestWrap) {
        //同步
        HttpResponse<Object> response = null;
        ExceptionProcessor exceptionProcessor = requestWrap.getExceptionProcessor();
        try {
            response = this.client.requestSync(requestWrap);
            if (ObjectUtils.isNotNullOrEmpty(exceptionProcessor)) {
                exceptionProcessor.invalidRespHandle(requestWrap, response);
            }
            return Response.ofSync(response);
        } catch (Exception e) {
            if (ObjectUtils.isNotNullOrEmpty(exceptionProcessor)) {
                exceptionProcessor.exceptionHandle(requestWrap, response, e);
                return Response.ofSync(response);
            } else {
                return HttpExceptionCheck.INSTANCE.throwUnchecked(e);
            }
        }
    }

    private Response async(RequestWrap requestWrap) {
        //异步
        CompletableFuture<HttpResponse<Object>> future = this.client
                .requestAsync(requestWrap)
                .handleAsync(this.throwableFunction(requestWrap))
                .thenCompose(Function.identity());
        return Response.ofAsync(future);
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }

    private <T> BiFunction<HttpResponse<T>, Throwable, CompletableFuture<HttpResponse<T>>> throwableFunction(RequestWrap requestWrap) {
        return (res, ex) -> {
            if (ObjectUtils.isNullOrEmpty(ex)) {
                return CompletableFuture.completedFuture(res);
            }
            requestWrap.getExceptionProcessor().exceptionHandle(requestWrap, res, ex);
            return ObjectUtils.isNotNullOrEmpty(res) ? CompletableFuture.completedFuture(res) : null;
        };
    }
}
