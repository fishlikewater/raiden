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

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.constant.CommonConstants;
import io.github.fishlikewater.raiden.http.core.RequestWrap;
import io.github.fishlikewater.raiden.http.core.Response;
import io.github.fishlikewater.raiden.http.core.degrade.FallbackFactory;
import io.github.fishlikewater.raiden.http.core.degrade.sentinel.SentinelDegradeRule;
import io.github.fishlikewater.raiden.http.core.exception.DegradeException;
import io.github.fishlikewater.raiden.http.core.exception.HttpExceptionCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@code DegradeInterceptor}
 *
 * @author zhangxiang
 * @version 1.1.0
 * @since 2024/12/11
 */
public interface DegradeInterceptor {

    ConcurrentHashMap<String, Object> fallbackFactoryObjects = new ConcurrentHashMap<>();
    Logger log = LoggerFactory.getLogger(DegradeInterceptor.class);

    default Object get(String name, FallbackFactory<?> factory, Throwable cause) {
        Object o = fallbackFactoryObjects.get(name);
        if (ObjectUtils.isNotNullOrEmpty(o)) {
            return o;
        }
        synchronized (this) {
            o = fallbackFactoryObjects.get(name);
            if (ObjectUtils.isNullOrEmpty(o)) {
                o = factory.create(cause);
                fallbackFactoryObjects.put(name, o);
            }
        }

        return o;
    }

    default String parseName(RequestWrap requestWrap) {
        return requestWrap.getHttpMethod().name() + CommonConstants.Symbol.SYMBOL_COLON + requestWrap.getUrl();
    }

    default Response fallback(Exception e, RequestWrap requestWrap) {
        log.error("degrade.trigger: type: [{}]", requestWrap.getDegradeType().name());
        FallbackFactory<?> fallbackFactory = requestWrap.getFallbackFactory();
        if (ObjectUtils.isNullOrEmpty(fallbackFactory)) {
            throw new DegradeException(e);
        }
        Object o = this.get(fallbackFactory.getClass().getName(), fallbackFactory, e);
        Object invoke;
        try {
            invoke = requestWrap.getMethod().invoke(o, requestWrap.getArgs());
        } catch (IllegalAccessException | InvocationTargetException ex) {
            return HttpExceptionCheck.INSTANCE.throwUnchecked(ex);
        }
        return Response.ofFallback(invoke);
    }

    default void loadSentinelConfig(String resourceName, SentinelDegradeRule rule) {
        synchronized (this) {
            if (DegradeRuleManager.hasConfig(resourceName)) {
                return;
            }
            DegradeRule degradeRule = new DegradeRule(resourceName);
            degradeRule.setGrade(rule.getGrade());
            degradeRule.setCount(rule.getCount());
            degradeRule.setTimeWindow(rule.getTimeWindow());
            degradeRule.setMinRequestAmount(rule.getMinRequestAmount());
            degradeRule.setSlowRatioThreshold(rule.getSlowRatioThreshold());
            degradeRule.setStatIntervalMs(rule.getStatIntervalMs());
            DegradeRuleManager.loadRules(Collections.singletonList(degradeRule));
        }
    }
}
