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
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

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
    public Response<?> intercept(Chain chain) throws IOException, InterruptedException {
        try {
            Response<?> response = chain.proceed();
            this.determineAsyncResponse(response, chain);
            return response;
        } catch (Exception e) {
            return this.retry(chain, e);
        }
    }


    @Override
    public int order() {
        return 0;
    }

    private void determineAsyncResponse(Response<?> response, Chain chain) {
        if (ObjectUtils.isNotNullOrEmpty(response.getAsyncResponse())) {
            response.getAsyncResponse().thenApply(t -> {
                try {
                    return this.retry(chain, null).getAsyncResponse();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
