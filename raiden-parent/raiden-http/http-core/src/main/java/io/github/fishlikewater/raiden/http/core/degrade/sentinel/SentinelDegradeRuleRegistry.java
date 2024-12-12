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
package io.github.fishlikewater.raiden.http.core.degrade.sentinel;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import io.github.fishlikewater.raiden.core.Assert;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.http.core.constant.DefaultConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code SentinelDegradeRuleRegistry}
 * sentinel规则注册器
 *
 * @author zhangxiang
 * @version 1.1.0
 * @since 2024/12/12
 */
public class SentinelDegradeRuleRegistry {

    private final Map<String, SentinelDegradeRule> sentinelDegradeRuleMap;

    public SentinelDegradeRuleRegistry(List<SentinelDegradeRuleRegister> register) {
        this.sentinelDegradeRuleMap = new HashMap<>(8);
        this.sentinelDegradeRuleMap.put(DefaultConstants.DEFAULT_CIRCUIT_BREAKER_CONFIG, buildDefault());
        if (ObjectUtils.isNotNullOrEmpty(register)) {
            register.forEach(registrar -> registrar.register(this));
        }
    }

    public void register(String name, SentinelDegradeRule sentinelDegradeRule) {
        this.sentinelDegradeRuleMap.put(name, sentinelDegradeRule);
    }

    public SentinelDegradeRule get(String name) {
        SentinelDegradeRule sentinelDegradeRule = this.sentinelDegradeRuleMap.get(name);
        Assert.notNull(sentinelDegradeRule, "not.found! name: {}", name);
        return sentinelDegradeRule;
    }

    private SentinelDegradeRule buildDefault() {
        SentinelDegradeRule sentinelDegradeRule = new SentinelDegradeRule();
        sentinelDegradeRule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO);
        sentinelDegradeRule.setCount(0.5);
        sentinelDegradeRule.setMinRequestAmount(5);
        sentinelDegradeRule.setTimeWindow(10);
        sentinelDegradeRule.setSlowRatioThreshold(1.0);
        return sentinelDegradeRule;
    }
}
