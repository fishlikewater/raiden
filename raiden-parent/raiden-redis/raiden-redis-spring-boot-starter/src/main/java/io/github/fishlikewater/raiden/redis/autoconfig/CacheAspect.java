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
package io.github.fishlikewater.raiden.redis.autoconfig;

import io.github.fishlikewater.raiden.core.DateUtils;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.redis.core.DataTypeEnum;
import io.github.fishlikewater.raiden.redis.core.annotation.Cache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * {@code CacheAspect}
 * 缓存切面
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/06/24
 */
@Aspect
@Order(1)
@ConditionalOnBean(RedissonClient.class)
public class CacheAspect extends AbstractCacheAspect {

    private final RedissonClient redissonClient;

    private final RedisProperties redisProperties;

    private final ParameterNameDiscoverer parameterNameDiscoverer;

    public CacheAspect(RedissonClient redissonClient, RedisProperties redisProperties, ParameterNameDiscoverer parameterNameDiscoverer) {
        this.redissonClient = redissonClient;
        this.redisProperties = redisProperties;
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    @Pointcut(value = "@annotation(io.github.fishlikewater.raiden.redis.core.annotation.Cache)")
    public void anyMethod() {
    }

    @Around(value = "anyMethod() && @annotation(cache)")
    public Object aroundAdvice4Method(ProceedingJoinPoint pjp, Cache cache) throws Throwable {
        return this.handleCache(cache, pjp);
    }

    private Object handleCache(Cache cache, ProceedingJoinPoint pjp) throws Throwable {
        // 获取缓存key
        DataTypeEnum type = cache.type();
        if (Objects.requireNonNull(type) == DataTypeEnum.HASH) {
            return this.handleHash(pjp, cache);
        }
        return this.handleGeneral(pjp, cache);
    }

    private Object handleGeneral(ProceedingJoinPoint pjp, Cache cache) throws Throwable {
        String cacheKey = this.populateCacheKey(cache.key(), cache.prefix(), pjp);
        RBucket<Object> bucket = redissonClient.getBucket(cacheKey);
        Object obj = bucket.get();
        if (ObjectUtils.isNotNullOrEmpty(obj)) {
            return obj;
        }

        RLock lock = redissonClient.getLock(this.getLockKey(cacheKey));
        lock.lock();
        try {
            Object object = bucket.get();
            if (ObjectUtils.isNotNullOrEmpty(object)) {
                return object;
            }
            Object result = pjp.proceed();
            ChronoUnit chronoUnit = DateUtils.convertToChronoUnit(cache.timeUnit());
            bucket.set(result, Duration.of(cache.expire(), chronoUnit));
            return result;
        } finally {
            lock.unlock();
        }
    }

    private Object handleHash(ProceedingJoinPoint pjp, Cache cache) throws Throwable {
        EvaluationContext context = this.getContext(pjp);
        String hashKey = this.populateCacheKey(cache.hashKey(), null, pjp);
        String cacheKey = this.populateCacheKey(cache.key(), cache.prefix(), context);
        RMapCache<String, Object> map = redissonClient.getMapCache(cacheKey);
        Object obj = map.get(hashKey);
        if (ObjectUtils.isNotNullOrEmpty(obj)) {
            return obj;
        }
        RLock lock = redissonClient.getLock(this.getLockKey(cacheKey));
        lock.lock();
        try {
            obj = map.get(hashKey);
            if (ObjectUtils.isNotNullOrEmpty(obj)) {
                return obj;
            }
            Object result = pjp.proceed();
            map.put(hashKey, result, cache.expire(), cache.timeUnit());
            return result;
        } finally {
            lock.unlock();
        }
    }

    @Override
    protected ParameterNameDiscoverer parameterNameDiscoverer() {
        return this.parameterNameDiscoverer;
    }

    @Override
    protected RedisProperties redisProperties() {
        return this.redisProperties;
    }
}
