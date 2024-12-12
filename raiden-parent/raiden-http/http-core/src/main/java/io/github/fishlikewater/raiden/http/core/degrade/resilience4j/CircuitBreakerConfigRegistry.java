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
package io.github.fishlikewater.raiden.http.core.degrade.resilience4j;

import io.github.fishlikewater.raiden.core.Assert;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.http.core.constant.DefaultConstants;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code CircuitBreakerConfigRegistry}
 * 注册配置
 *
 * @author zhangxiang
 * @version 1.1.0
 * @since 2024/12/11
 */
public class CircuitBreakerConfigRegistry {

    private final Map<String, CircuitBreakerConfig> circuitBreakerConfigMap;

    public CircuitBreakerConfigRegistry(List<CircuitBreakerConfigRegister> register) {
        this.circuitBreakerConfigMap = new HashMap<>(8);
        this.circuitBreakerConfigMap.put(DefaultConstants.DEFAULT_CIRCUIT_BREAKER_CONFIG, CircuitBreakerConfig.ofDefaults());
        if (ObjectUtils.isNotNullOrEmpty(register)) {
            register.forEach(registrar -> registrar.register(this));
        }
    }

    public void register(String name, CircuitBreakerConfig circuitBreakerConfig) {
        this.circuitBreakerConfigMap.put(name, circuitBreakerConfig);
    }

    public CircuitBreakerConfig get(String name) {
        CircuitBreakerConfig circuitBreakerConfig = this.circuitBreakerConfigMap.get(name);
        Assert.notNull(circuitBreakerConfig, "not.found! name: {}", name);
        return circuitBreakerConfig;
    }
}
