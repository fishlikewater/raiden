/*
 * Copyright (c) 2023-2025 zhangxiang (fishlikewater@126.com)
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

import io.github.fishlikewater.raiden.redis.autoconfig.RedisProperties;
import io.github.fishlikewater.raiden.redis.core.annotation.CachePut;
import io.github.fishlikewater.raiden.redis.core.enums.DataTypeEnum;
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

import java.util.Objects;

/**
 * {@code CachePutAspect}
 * 缓存更新切面
 *
 * @author zhangxiang
 * @since 2025/10/14
 */
@Aspect
@Order(3)
@ConditionalOnBean(RedissonClient.class)
public class CachePutAspect extends AbstractCacheAspect {

    private final RedissonClient redissonClient;

    private final RedisProperties redisProperties;

    private final ParameterNameDiscoverer parameterNameDiscoverer;

    public CachePutAspect(RedissonClient redissonClient, RedisProperties redisProperties, ParameterNameDiscoverer parameterNameDiscoverer) {
        this.redissonClient = redissonClient;
        this.redisProperties = redisProperties;
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    @Pointcut(value = "@annotation(io.github.fishlikewater.raiden.redis.core.annotation.CachePut)")
    public void anyMethod() {
    }

    @Around(value = "anyMethod() && @annotation(cachePut)")
    public Object aroundAdvice4Method(ProceedingJoinPoint pjp, CachePut cachePut) throws Throwable {
        return this.handleCachePut(cachePut, pjp);
    }

    @Override
    protected ParameterNameDiscoverer parameterNameDiscoverer() {
        return this.parameterNameDiscoverer;
    }

    @Override
    protected RedisProperties redisProperties() {
        return this.redisProperties;
    }

    /**
     * 处理缓存更新
     *
     * @param cachePut 缓存更新注解
     * @param pjp      切点
     * @return Object
     */
    @SuppressWarnings("all")
    private Object handleCachePut(CachePut cachePut, ProceedingJoinPoint pjp) throws Throwable {
        if (this.determineCondition(cachePut.condition(), pjp)) {
            return pjp.proceed();
        }
        // 获取缓存key
        DataTypeEnum type = cachePut.type();
        if (Objects.requireNonNull(type) == DataTypeEnum.HASH) {
            return this.handleHash(pjp, cachePut);
        }
        return this.handleGeneral(pjp, cachePut);
    }

    /**
     * 处理普通缓存更新
     *
     * @param pjp      切点
     * @param cachePut 缓存更新注解
     * @return Object
     */
    private Object handleGeneral(ProceedingJoinPoint pjp, CachePut cachePut) throws Throwable {
        String cacheKey = this.populateCacheKey(cachePut.key(), cachePut.prefix(), pjp);
        RBucket<Object> bucket = redissonClient.getBucket(cacheKey);
        RLock lock = redissonClient.getLock(this.getLockKey(cacheKey));
        lock.lock();
        try {
            return redisCacheObject(pjp, bucket, cachePut.expire(), cachePut.timeUnit());
        } finally {
            lock.unlock();
        }
    }

    /**
     * 处理hash缓存更新
     *
     * @param pjp      切点
     * @param cachePut 缓存更新注解
     * @return Object
     */
    private Object handleHash(ProceedingJoinPoint pjp, CachePut cachePut) throws Throwable {
        EvaluationContext context = this.getContext(pjp);
        String hashKey = this.populateHashKey(cachePut.hashKey(), context);
        String cacheKey = this.populateCacheKey(cachePut.key(), cachePut.prefix(), context);
        RMapCache<String, Object> map = redissonClient.getMapCache(cacheKey);
        RLock lock = redissonClient.getLock(this.getLockKey(cacheKey));
        lock.lock();
        try {
            return redisCacheObject(pjp, map, hashKey, cachePut.expire(), cachePut.timeUnit());
        } finally {
            lock.unlock();
        }
    }
}
