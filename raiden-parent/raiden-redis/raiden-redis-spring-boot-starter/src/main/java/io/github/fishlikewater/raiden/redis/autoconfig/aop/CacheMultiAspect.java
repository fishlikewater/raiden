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

import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.redis.autoconfig.RedisProperties;
import io.github.fishlikewater.raiden.redis.core.annotation.Cache;
import io.github.fishlikewater.raiden.redis.core.annotation.CacheInvalidate;
import io.github.fishlikewater.raiden.redis.core.annotation.CacheMulti;
import io.github.fishlikewater.raiden.redis.core.annotation.CachePut;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;

/**
 * {@code CacheMultiAspect}
 * 缓存多步操作
 *
 * @author zhangxiang
 * @since 2025/10/17
 */
@Aspect
@Order(4)
@ConditionalOnBean(RedissonClient.class)
public class CacheMultiAspect extends AbstractCacheHandler {

    private final RedisProperties redisProperties;

    private final ParameterNameDiscoverer parameterNameDiscoverer;

    public CacheMultiAspect(RedissonClient redissonClient, RedisProperties redisProperties, ParameterNameDiscoverer parameterNameDiscoverer) {
        super(redissonClient);
        this.redisProperties = redisProperties;
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    @Pointcut(value = "@annotation(io.github.fishlikewater.raiden.redis.core.annotation.CacheMulti)")
    public void anyMethod() {
    }

    @Around(value = "anyMethod() && @annotation(cacheMulti)")
    public Object aroundAdvice4Method(ProceedingJoinPoint pjp, CacheMulti cacheMulti) throws Throwable {
        return this.handleCacheMulti(cacheMulti, pjp);
    }

    @Override
    protected ParameterNameDiscoverer parameterNameDiscoverer() {
        return this.parameterNameDiscoverer;
    }

    @Override
    protected RedisProperties redisProperties() {
        return this.redisProperties;
    }

    private Object handleCacheMulti(CacheMulti cacheMulti, ProceedingJoinPoint pjp) throws Throwable {
        CacheInvalidate[] cacheInvalidates = cacheMulti.cacheInvalidate();
        Cache[] caches = cacheMulti.cache();
        CachePut[] puts = cacheMulti.put();
        if (ObjectUtils.isNotNullOrEmpty(cacheInvalidates)) {
            for (CacheInvalidate cacheInvalidate : cacheInvalidates) {
                this.cleanCache(cacheInvalidate, pjp, false);
            }
        }

        if (ObjectUtils.isNullOrEmpty(caches) && ObjectUtils.isNullOrEmpty(puts)) {
            return pjp.proceed();
        }

        Object result = null;
        if (ObjectUtils.isNotNullOrEmpty(puts)) {
            result = pjp.proceed();
            for (CachePut cachePut : puts) {
                this.handleCachePut(cachePut, pjp, result);
            }
        }

        if (ObjectUtils.isNotNullOrEmpty(caches)) {
            for (Cache cache : caches) {
                this.handleCache(cache, pjp, result);
            }
        }

        return result;
    }
}
