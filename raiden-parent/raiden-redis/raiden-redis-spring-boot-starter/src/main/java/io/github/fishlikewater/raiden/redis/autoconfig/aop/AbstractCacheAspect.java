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
package io.github.fishlikewater.raiden.redis.autoconfig.aop;

import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.StringUtils;
import io.github.fishlikewater.raiden.core.constant.CommonConstants;
import io.github.fishlikewater.raiden.core.exception.RaidenExceptionCheck;
import io.github.fishlikewater.raiden.redis.autoconfig.CacheComposite;
import io.github.fishlikewater.raiden.redis.autoconfig.RedisProperties;
import io.github.fishlikewater.raiden.redis.autoconfig.processor.UpdateCacheProcessor;
import io.github.fishlikewater.raiden.redis.core.annotation.Cache;
import io.github.fishlikewater.spring.boot.raiden.core.ExpressionUtils;
import io.github.fishlikewater.spring.boot.raiden.core.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

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
        RaidenExceptionCheck.INSTANCE.isNotNull(key, "key is not found");
        // 判断key 是否为el表达式
        if (key.startsWith(CommonConstants.SYMBOL_EXPRESSION)) {
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
        RaidenExceptionCheck.INSTANCE.isNotNull(key, "key is not found");
        // 判断key 是否为el表达式
        if (key.startsWith(CommonConstants.SYMBOL_EXPRESSION)) {
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

    protected void removeTask(String cacheKey, String hashKey) {
        UpdateCacheProcessor processor = SpringUtils.getBean(UpdateCacheProcessor.class);
        if (ObjectUtils.isNullOrEmpty(processor)) {
            return;
        }
        String key = cacheKey;
        if (StringUtils.isNotBlank(hashKey)) {
            key = StringUtils.format("{}#{}", cacheKey, hashKey);
        }
        processor.remove(key);
    }

    protected void addUpdateTask(ProceedingJoinPoint pjp, Cache cache, String cacheKey, String hashKey) {
        if (cache.updateTime() <= 0) {
            return;
        }

        UpdateCacheProcessor processor = SpringUtils.getBean(UpdateCacheProcessor.class);
        if (ObjectUtils.isNullOrEmpty(processor)) {
            log.warn("cache.update.task.is.not.enable");
            return;
        }
        String key = cacheKey;
        if (StringUtils.isNotBlank(hashKey)) {
            key = StringUtils.format("{}#{}", cacheKey, hashKey);
        }
        UpdateCacheProcessor.UpdateCacheProcessorHolder holder = processor.get(key);
        if (ObjectUtils.isNotNullOrEmpty(holder)) {
            return;
        }

        Duration expirationTime = this.redisProperties().getCache().getExpirationTime();
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        UpdateCacheProcessor.UpdateCacheProcessorHolder processorHolder = UpdateCacheProcessor.UpdateCacheProcessorHolder.builder()
                .key(cacheKey)
                .hashKey(hashKey)
                .method(methodSignature.getMethod())
                .target(pjp.getTarget())
                .args(pjp.getArgs())
                .delayTime(cache.updateTime())
                .delayTimeUnit(cache.updatetimeUnit())
                .expireTime(cache.expire() <= 0 ? expirationTime.toSeconds() : cache.expire())
                .expireTimeUnit(cache.expire() <= 0 ? TimeUnit.SECONDS : cache.timeUnit())
                .type(cache.type())
                .build();
        processor.add(processorHolder);
    }
}
