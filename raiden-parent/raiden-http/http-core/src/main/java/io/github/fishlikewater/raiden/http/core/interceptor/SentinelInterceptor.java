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

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.ResourceTypeConstants;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import io.github.fishlikewater.raiden.http.core.HttpBootStrap;
import io.github.fishlikewater.raiden.http.core.RequestWrap;
import io.github.fishlikewater.raiden.http.core.Response;
import io.github.fishlikewater.raiden.http.core.degrade.sentinel.SentinelDegradeRule;
import io.github.fishlikewater.raiden.http.core.degrade.sentinel.SentinelDegradeRuleRegistry;

import java.io.IOException;

/**
 * {@code SentinelInterceptor}
 * sentinel拦截器
 *
 * @author zhangxiang
 * @version 1.1.0
 * @since 2024/12/10
 */
public class SentinelInterceptor implements HttpInterceptor, DegradeInterceptor {

    @Override
    public Response intercept(Chain chain) throws IOException, InterruptedException {
        if (!chain.requestWrap().isDegrade()) {
            return chain.proceed();
        }
        return this.degrade(chain);
    }

    @Override
    public int order() {
        return 0;
    }

    private Response degrade(Chain chain) throws IOException, InterruptedException {
        RequestWrap requestWrap = chain.requestWrap();
        String configName = requestWrap.getCircuitBreakerConfigName();
        SentinelDegradeRuleRegistry registry = HttpBootStrap.getConfig().getSentDegradeRuleRegistry();
        SentinelDegradeRule rule = registry.get(configName);
        String resourceName = this.parseName(chain.requestWrap());
        boolean hasConfig = DegradeRuleManager.hasConfig(resourceName);
        if (!hasConfig) {
            this.loadSentinelConfig(resourceName, rule);
        }
        try (Entry ignored = SphU.entry(resourceName, ResourceTypeConstants.COMMON_WEB, EntryType.OUT)) {
            return chain.proceed();
        } catch (BlockException e) {
            return this.fallback(e, requestWrap);
        }
    }
}
