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
import io.github.fishlikewater.raiden.http.core.Response;
import io.github.fishlikewater.raiden.http.core.exception.DegradeException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * {@code RetryInterceptor}
 * 重试拦截器
 *
 * @author zhangxiang
 * @version 1.1.0
 * @since 2024/12/10
 */
@Slf4j
public class RetryInterceptor implements HttpInterceptor, RetryHandler {

    @Override
    public Response intercept(Chain chain) throws IOException, InterruptedException {
        try {
            Response response = chain.proceed();
            return this.determineAsyncResponse(response, chain);
        } catch (Exception e) {
            if (e instanceof DegradeException) {
                throw e;
            }
            return this.retry(chain, e);
        }
    }

    @Override
    public int order() {
        return 0;
    }

    private Response determineAsyncResponse(Response response, Chain chain) {
        if (ObjectUtils.isNotNullOrEmpty(response.getAsyncResponse())) {
            CompletableFuture<HttpResponse<Object>> future = response.getAsyncResponse().handleAsync((res, ex) -> {
                        if (ObjectUtils.isNullOrEmpty(ex)) {
                            return CompletableFuture.completedFuture(res);
                        }
                        try {
                            return this.retry(chain, ex).getAsyncResponse();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .thenCompose(Function.identity());
            return Response.ofAsync(future);
        }
        return response;
    }
}
