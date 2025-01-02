/*
 * Copyright (c) 2025 zhangxiang (fishlikewater@126.com)
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
package io.github.fishlikewater.raiden.redis.autoconfig.aop;

import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.StringUtils;
import io.github.fishlikewater.raiden.core.constant.CommonConstants;
import io.github.fishlikewater.raiden.core.exception.RaidenExceptionCheck;
import io.github.fishlikewater.raiden.redis.autoconfig.CacheComposite;
import io.github.fishlikewater.raiden.redis.autoconfig.RedisProperties;
import io.github.fishlikewater.spring.boot.raiden.core.ExpressionUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * {@code AbstractCacheAspect}
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/06/25
 */
@Slf4j
public abstract class AbstractCacheAspect implements CacheComposite {

    /**
     * 获取参数名称解析器
     *
     * @return 参数名称
     */
    protected abstract ParameterNameDiscoverer parameterNameDiscoverer();

    /**
     * 获取redis配置
     *
     * @return redis配置
     */
    protected abstract RedisProperties redisProperties();

    protected String populateCacheKey(String key, String prefix, ProceedingJoinPoint pjp) {
        RaidenExceptionCheck.INSTANCE.isNotNull(key, "key.is.not.found");
        // 判断key 是否为el表达式
        if (key.startsWith(CommonConstants.Symbol.SYMBOL_EXPRESSION)) {
            EvaluationContext context = this.getContext(pjp);
            key = ExpressionUtils.getExpressionValue(context, key, String.class);
        }

        if (StringUtils.isBlank(prefix)) {
            prefix = this.redisProperties().getCache().getPrefix();
        }
        String cacheKey = key;
        if (StringUtils.isNotBlank(prefix)) {
            cacheKey = StringUtils.format("{}:{}", prefix, key);
        }
        return cacheKey;
    }

    protected String populateCacheKey(String key, String prefix, EvaluationContext context) {
        RaidenExceptionCheck.INSTANCE.isNotNull(key, "key.is.not.found");
        // 判断key 是否为el表达式
        if (key.startsWith(CommonConstants.Symbol.SYMBOL_EXPRESSION)) {
            key = ExpressionUtils.getExpressionValue(context, key, String.class);
        }

        if (StringUtils.isBlank(prefix)) {
            prefix = this.redisProperties().getCache().getPrefix();
        }
        String cacheKey = key;
        if (StringUtils.isNotBlank(prefix)) {
            cacheKey = StringUtils.format("{}:{}", prefix, key);
        }
        return cacheKey;
    }

    protected String populateHashKey(String hashKey, EvaluationContext context) {
        RaidenExceptionCheck.INSTANCE.isNotNull(hashKey, "hashKey.is.not.found");
        // 判断key 是否为el表达式
        if (hashKey.startsWith(CommonConstants.Symbol.SYMBOL_EXPRESSION)) {
            hashKey = ExpressionUtils.getExpressionValue(context, hashKey, String.class);
        }
        return hashKey;
    }

    protected EvaluationContext getContext(ProceedingJoinPoint pjp) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String[] parameterNames = this.parameterNameDiscoverer().getParameterNames(methodSignature.getMethod());
        Object[] args = pjp.getArgs();
        if (ObjectUtils.isNullOrEmpty(parameterNames) || ObjectUtils.isNullOrEmpty(args)) {
            return RaidenExceptionCheck.INSTANCE.throwUnchecked("args.is.null");
        }
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < args.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        return context;
    }
}
