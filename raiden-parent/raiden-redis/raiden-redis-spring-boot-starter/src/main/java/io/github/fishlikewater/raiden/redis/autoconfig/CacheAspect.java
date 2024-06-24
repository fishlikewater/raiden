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

import io.github.fishlikewater.raiden.redis.core.annotation.Cache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;

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
public class CacheAspect {

    private final RedissonClient redissonClient;

    public CacheAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Pointcut(value = "@annotation(io.github.fishlikewater.raiden.redis.core.annotation.Cache)")
    public void anyMethod() {
    }

    @Around(value = "anyMethod() && @annotation(cache)")
    public Object aroundAdvice4Method(ProceedingJoinPoint pjp, Cache cache) {
        return null;
    }
}
