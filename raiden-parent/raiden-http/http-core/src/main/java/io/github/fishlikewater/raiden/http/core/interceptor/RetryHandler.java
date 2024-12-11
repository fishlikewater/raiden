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

import io.github.fishlikewater.raiden.http.core.HttpBootStrap;
import io.github.fishlikewater.raiden.http.core.Response;
import io.github.fishlikewater.raiden.http.core.exception.HttpExceptionCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * {@code RetryHandler}
 * 重试处理器
 *
 * @author zhangxiang
 * @version 1.1.0
 * @since 2024/12/11
 */
public interface RetryHandler {

    Logger log = LoggerFactory.getLogger(RetryHandler.class);

    default Response retry(HttpInterceptor.Chain chain, Throwable e) throws IOException, InterruptedException {
        int retryCount = chain.requestWrap().getRetryCount();
        int maxRetryCount = HttpBootStrap.getConfig().getMaxRetryCount();
        if (retryCount > 0 && retryCount <= maxRetryCount) {
            log.error("raiden.http: request.failed, will.retry.request,wait.{}ms", HttpBootStrap.getConfig().getRetryInterval());
            this.sleep();
            chain.requestWrap().setRetryCount(--retryCount);
            chain.reset();
            return chain.proceed();
        } else {
            return HttpExceptionCheck.INSTANCE.throwUnchecked(e);
        }
    }

    default void sleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(HttpBootStrap.getConfig().getRetryInterval());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
