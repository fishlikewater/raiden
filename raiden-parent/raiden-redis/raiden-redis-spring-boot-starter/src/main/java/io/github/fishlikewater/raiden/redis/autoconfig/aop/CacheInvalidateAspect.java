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
import io.github.fishlikewater.raiden.redis.core.annotation.CacheInvalidate;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;

/**
 * {@code CacheInvalidateAspect}
 * 缓存清理切面
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/06/24
 */
@Aspect
@Order(2)
@ConditionalOnBean(RedissonClient.class)
public class CacheInvalidateAspect extends AbstractCacheHandler {

    private final RedisProperties redisProperties;

    private final ParameterNameDiscoverer parameterNameDiscoverer;

    public CacheInvalidateAspect(RedissonClient redissonClient, RedisProperties redisProperties, ParameterNameDiscoverer parameterNameDiscoverer) {
        super(redissonClient);
        this.redisProperties = redisProperties;
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    @Pointcut(value = "@annotation(io.github.fishlikewater.raiden.redis.core.annotation.CacheInvalidate)")
    public void anyMethod() {
    }

    @Around(value = "anyMethod() && @annotation(cacheInvalidate)")
    public Object aroundAdvice4Method(ProceedingJoinPoint pjp, CacheInvalidate cacheInvalidate) throws Throwable {
        return this.cleanCache(cacheInvalidate, pjp, true);
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
