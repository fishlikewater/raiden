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
import io.github.fishlikewater.raiden.core.StringUtils;
import io.github.fishlikewater.raiden.redis.core.annotation.Cache;
import io.github.fishlikewater.raiden.redis.core.annotation.CacheInvalidate;
import io.github.fishlikewater.raiden.redis.core.annotation.CachePut;
import io.github.fishlikewater.raiden.redis.core.enums.DataTypeEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.expression.EvaluationContext;

import java.util.Objects;

/**
 * {@code AbstractHandler}
 * 处理器
 *
 * @author zhangxiang
 * @since 2025/10/17
 */
public abstract class AbstractCacheHandler extends AbstractCacheAspect {

    private final RedissonClient redissonClient;

    protected AbstractCacheHandler(RedissonClient redissonClient) {this.redissonClient = redissonClient;}

    // ---------------------------------------------------------------- 处理缓存

    /**
     * 处理缓存
     *
     * @param cache  缓存注解
     * @param pjp    切点
     * @param result 结果
     * @return Object
     */
    @SuppressWarnings("all")
    protected Object handleCache(Cache cache, ProceedingJoinPoint pjp, Object result) throws Throwable {
        if (this.determineCondition(cache.condition(), pjp)) {
            return pjp.proceed();
        }
        // 获取缓存key
        DataTypeEnum type = cache.type();
        if (Objects.requireNonNull(type) == DataTypeEnum.HASH) {
            return this.handleHash(pjp, cache, result);
        }
        return this.handleGeneral(pjp, cache, result);
    }

    // ---------------------------------------------------------------- 处理缓存更新

    /**
     * 处理缓存更新
     *
     * @param cachePut 缓存更新注解
     * @param result   结果
     * @param pjp      切点
     * @return Object
     */
    @SuppressWarnings("all")
    protected Object handleCachePut(CachePut cachePut, ProceedingJoinPoint pjp, Object result) throws Throwable {
        if (this.determineCondition(cachePut.condition(), pjp)) {
            return ObjectUtils.isNullOrEmpty(result) ? pjp.proceed() : result;
        }
        // 获取缓存key
        DataTypeEnum type = cachePut.type();
        if (Objects.requireNonNull(type) == DataTypeEnum.HASH) {
            return this.handleHash(pjp, cachePut, result);
        }
        return this.handleGeneral(pjp, cachePut, result);
    }

    // ---------------------------------------------------------------- 处理缓存清理

    /**
     * 清理缓存
     *
     * @param cacheInvalidate 缓存注解
     * @param pjp             ProceedingJoinPoint
     * @return Object
     * @throws Throwable 异常
     */
    protected Object cleanCache(CacheInvalidate cacheInvalidate, ProceedingJoinPoint pjp, boolean calculateResult) throws Throwable {
        // 获取缓存key
        DataTypeEnum type = cacheInvalidate.type();
        if (Objects.requireNonNull(type) == DataTypeEnum.HASH) {
            return this.cleanHash(pjp, cacheInvalidate, calculateResult);
        }
        return this.handleGeneral(pjp, cacheInvalidate, calculateResult);
    }

    // ---------------------------------------------------------------- private

    private Object handleGeneral(ProceedingJoinPoint pjp, Cache cache, Object result) throws Throwable {
        String cacheKey = this.populateCacheKey(cache.key(), cache.prefix(), pjp);
        RBucket<Object> bucket = this.redissonClient.getBucket(cacheKey);
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
            result = ObjectUtils.isNullOrEmpty(result) ? pjp.proceed() : result;
            return redisCacheObject(result, bucket, cache.expire(), cache.timeUnit());
        } finally {
            lock.unlock();
        }
    }

    private Object handleHash(ProceedingJoinPoint pjp, Cache cache, Object result) throws Throwable {
        EvaluationContext context = this.getContext(pjp);
        String hashKey = this.populateHashKey(cache.hashKey(), context);
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
            result = ObjectUtils.isNullOrEmpty(result) ? pjp.proceed() : result;
            return redisCacheObject(result, map, hashKey, cache.expire(), cache.timeUnit());
        } finally {
            lock.unlock();
        }
    }

    /**
     * 处理普通缓存更新
     *
     * @param pjp      切点
     * @param cachePut 缓存更新注解
     * @param result   结果
     * @return Object
     */
    private Object handleGeneral(ProceedingJoinPoint pjp, CachePut cachePut, Object result) throws Throwable {
        String cacheKey = this.populateCacheKey(cachePut.key(), cachePut.prefix(), pjp);
        RBucket<Object> bucket = redissonClient.getBucket(cacheKey);
        RLock lock = redissonClient.getLock(this.getLockKey(cacheKey));
        lock.lock();
        try {
            result = ObjectUtils.isNullOrEmpty(result) ? pjp.proceed() : result;
            return redisCacheObject(result, bucket, cachePut.expire(), cachePut.timeUnit());
        } finally {
            lock.unlock();
        }
    }

    /**
     * 处理hash缓存更新
     *
     * @param pjp      切点
     * @param cachePut 缓存更新注解
     * @param result   结果
     * @return Object
     */
    private Object handleHash(ProceedingJoinPoint pjp, CachePut cachePut, Object result) throws Throwable {
        EvaluationContext context = this.getContext(pjp);
        String hashKey = this.populateHashKey(cachePut.hashKey(), context);
        String cacheKey = this.populateCacheKey(cachePut.key(), cachePut.prefix(), context);
        RMapCache<String, Object> map = redissonClient.getMapCache(cacheKey);
        RLock lock = redissonClient.getLock(this.getLockKey(cacheKey));
        lock.lock();
        try {
            result = ObjectUtils.isNullOrEmpty(result) ? pjp.proceed() : result;
            return redisCacheObject(result, map, hashKey, cachePut.expire(), cachePut.timeUnit());
        } finally {
            lock.unlock();
        }
    }

    /**
     * 处理普通缓存
     *
     * @param pjp             ProceedingJoinPoint
     * @param cacheInvalidate 缓存注解
     * @param calculateResult 是否计算结果
     * @return Object
     * @throws Throwable 抛出异常
     */
    private Object handleGeneral(ProceedingJoinPoint pjp, CacheInvalidate cacheInvalidate, boolean calculateResult) throws Throwable {
        boolean allEntries = cacheInvalidate.allEntries();
        if (allEntries) {
            redissonClient.getKeys().deleteByPattern(StringUtils.format("{}:{}", this.getPrefix(cacheInvalidate.prefix()), "*"));
            return calculateResult ? pjp.proceed() : null;
        }
        String cacheKey = this.populateCacheKey(cacheInvalidate.key(), cacheInvalidate.prefix(), pjp);
        RBucket<Object> bucket = redissonClient.getBucket(cacheKey);
        if (bucket.isExists()) {
            bucket.delete();
        }
        return calculateResult ? pjp.proceed() : null;
    }

    /**
     * 处理hash缓存
     *
     * @param pjp             ProceedingJoinPoint
     * @param cacheInvalidate 缓存注解
     * @param calculateResult 是否计算结果
     * @return Object
     * @throws Throwable 抛出异常
     */
    private Object cleanHash(ProceedingJoinPoint pjp, CacheInvalidate cacheInvalidate, boolean calculateResult) throws Throwable {
        EvaluationContext context = this.getContext(pjp);
        String cacheKey = this.populateCacheKey(cacheInvalidate.key(), this.getPrefix(cacheInvalidate.prefix()), context);
        boolean allEntries = cacheInvalidate.allEntries();
        RMapCache<String, Object> map = redissonClient.getMapCache(cacheKey);
        if (allEntries) {
            map.delete();
            return calculateResult ? pjp.proceed() : null;

        }
        String hashKey = this.populateHashKey(cacheInvalidate.hashKey(), context);
        if (map.isExists()) {
            map.remove(hashKey);
        }
        return calculateResult ? pjp.proceed() : null;
    }
}
