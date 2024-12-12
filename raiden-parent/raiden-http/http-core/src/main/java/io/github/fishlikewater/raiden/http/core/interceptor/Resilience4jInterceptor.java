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
import io.github.fishlikewater.raiden.http.core.RequestWrap;
import io.github.fishlikewater.raiden.http.core.Response;
import io.github.fishlikewater.raiden.http.core.degrade.resilience4j.CircuitBreakerConfigRegistry;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.StopWatch;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * {@code Resilience4jInterceptor}
 * resilience4j 熔断拦截器
 *
 * @author zhangxiang
 * @version 1.1.0
 * @since 2024/12/10
 */
public class Resilience4jInterceptor implements HttpInterceptor, DegradeInterceptor {

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public Resilience4jInterceptor(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    public Resilience4jInterceptor() {
        this.circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
    }

    @Override
    public Response intercept(Chain chain) throws IOException, InterruptedException {
        if (!chain.requestWrap().isDegrade()) {
            return chain.proceed();
        }
        return this.degrade(chain);
    }

    private Response degrade(Chain chain) throws IOException, InterruptedException {
        RequestWrap requestWrap = chain.requestWrap();
        String configName = requestWrap.getCircuitBreakerConfigName();
        CircuitBreakerConfigRegistry registry = HttpBootStrap.getConfig().getBreakerConfigRegistry();
        CircuitBreakerConfig breakerConfig = registry.get(configName);
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(this.parseName(requestWrap), breakerConfig);
        StopWatch stopWatch = StopWatch.start();
        try {
            circuitBreaker.acquirePermission();
            Response response = chain.proceed();
            circuitBreaker.onResult(stopWatch.stop().toNanos(), TimeUnit.NANOSECONDS, response);
            return response;
        } catch (CallNotPermittedException e) {
            return this.fallback(e, requestWrap);
        } catch (Throwable throwable) {
            circuitBreaker.onError(stopWatch.stop().toNanos(), TimeUnit.NANOSECONDS, throwable);
            throw throwable;
        }
    }

    @Override
    public int order() {
        return 0;
    }
}
