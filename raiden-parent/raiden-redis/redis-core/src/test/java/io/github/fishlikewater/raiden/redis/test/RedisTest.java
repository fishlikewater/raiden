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
package io.github.fishlikewater.raiden.redis.test;

import io.github.fishlikewater.raiden.core.RandomUtils;
import io.github.fishlikewater.raiden.core.StringUtils;
import io.github.fishlikewater.raiden.redis.core.RedissonPatternCfg;
import io.github.fishlikewater.raiden.redis.core.RedissonUtils;
import io.github.fishlikewater.raiden.redis.core.ServerPattern;
import io.github.fishlikewater.raiden.redis.core.delay.DelayQueue;
import io.github.fishlikewater.raiden.redis.core.delay.DelayTask;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * {@code RedisTest}
 *
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2024年05月14日 20:02
 **/
public class RedisTest {

    @Test
    public void testRedis() {
        final RedissonPatternCfg cfg = new RedissonPatternCfg();
        cfg.setServerPattern(ServerPattern.SINGLE);
        cfg.getSingle().setAddress("redis://127.0.0.1:6379");
        cfg.getSingle().setDatabase(1);
        final RedissonClient redissonClient = RedissonUtils.redissonClient(cfg);
        final RLock lock = redissonClient.getLock("com:github:fishlikewater:redis:lock:test");
        lock.lock();
        try {
            final RBucket<Object> bucket1 = redissonClient.getBucket("com:github:fishlikewater:redis:test:buck1");
            final RBucket<Object> bucket2 = redissonClient.getBucket("com:github:fishlikewater:redis:test:buck2");
            bucket1.set("123456", Duration.of(1, ChronoUnit.MINUTES));
            bucket2.set(new RedisJsonTest("fishlikewater", 18), Duration.of(1, ChronoUnit.HOURS));
            System.out.println("com:github:fishlikewater:redis:lock:test");
        } finally {
            lock.unlock();
        }
    }

    @Test
    public void testRedisDelayQueue() throws InterruptedException {
        final RedissonPatternCfg cfg = new RedissonPatternCfg();
        cfg.setServerPattern(ServerPattern.SINGLE);
        cfg.getSingle().setAddress("redis://127.0.0.1:6379");
        cfg.getSingle().setDatabase(1);
        final RedissonClient redissonClient = RedissonUtils.redissonClient(cfg);

        final DelayQueue delayQueue = new DelayQueue(
                "io:github:fishlikewater:test",
                redissonClient,
                task -> {
                    String taskId = task.getTaskId();
                    Long publishTime = task.getPublishTime();
                    System.out.println(StringUtils.format("taskId:{}, publishTime:{}", taskId, publishTime));
                });
        DelayTask<RedisJsonTest> task = DelayTask.<RedisJsonTest>builder()
                .taskId(RandomUtils.randNum(5))
                .payload(new RedisJsonTest("fishlikewater", 18))
                .delayTime(3L)
                .timeUnit(TimeUnit.SECONDS)
                .build();
        delayQueue.add(task);
        Thread.sleep(10_000L);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RedisJsonTest implements Serializable {

        @Serial
        private static final long serialVersionUID = -7960401915118169370L;

        private String name;

        private Integer age;
    }
}
