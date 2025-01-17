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
package io.github.fishlikewater.raiden.redis.autoconfig;

import io.github.fishlikewater.raiden.redis.autoconfig.aop.CacheAspect;
import io.github.fishlikewater.raiden.redis.autoconfig.aop.CacheInvalidateAspect;
import io.github.fishlikewater.raiden.redis.core.RedisUtils;
import io.github.fishlikewater.raiden.redis.core.RedissonUtils;
import io.github.fishlikewater.raiden.redis.core.delay.DelayQueue;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterNameDiscoverer;

/**
 * {@code RedisAutoConfig}
 * redis 自动配置类
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/15
 */
@AutoConfiguration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisAutoConfig {

    @Bean
    @ConditionalOnProperty(prefix = "raiden.redis", name = "enabled", havingValue = "true")
    public RedissonClient redissonClient(RedisProperties properties) {
        RedissonClient redissonClient = RedissonUtils.redissonClient(properties);
        RedisUtils.init(redissonClient);
        return redissonClient;
    }

    // ---------------------------------------------------------------- delay

    @Bean
    @ConditionalOnProperty(prefix = "raiden.redis.delay", name = "enabled", havingValue = "true")
    public GlobalDelayQueueHandler globalDelayQueueHandler() {
        return new GlobalDelayQueueHandler();
    }

    @Bean
    @ConditionalOnProperty(prefix = "raiden.redis.delay", name = "enabled", havingValue = "true")
    public DelayQueue delayQueue(RedisProperties properties, RedissonClient redissonClient, GlobalDelayQueueHandler globalDelayQueueHandler) {
        return new DelayQueue(
                properties.getDelay().getTopic(),
                redissonClient,
                globalDelayQueueHandler
        );
    }

    // ---------------------------------------------------------------- cache aop

    @Bean
    @ConditionalOnProperty(prefix = "raiden.redis.cache", name = "enabled", havingValue = "true")
    public CacheAspect cacheAspect(RedisProperties properties, RedissonClient redissonClient, ParameterNameDiscoverer parameterNameDiscoverer) {
        return new CacheAspect(redissonClient, properties, parameterNameDiscoverer);
    }

    @Bean
    @ConditionalOnProperty(prefix = "raiden.redis.cache", name = "enabled", havingValue = "true")
    public CacheInvalidateAspect cacheInvalidateAspect(RedisProperties properties, RedissonClient redissonClient, ParameterNameDiscoverer parameterNameDiscoverer) {
        return new CacheInvalidateAspect(redissonClient, properties, parameterNameDiscoverer);
    }

    // ---------------------------------------------------------------- cache update
}
