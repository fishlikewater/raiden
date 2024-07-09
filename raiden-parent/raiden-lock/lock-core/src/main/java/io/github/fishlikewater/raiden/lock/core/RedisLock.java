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
package io.github.fishlikewater.raiden.lock.core;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.function.Supplier;

/**
 * {@code RedisLock}
 * Redis 锁
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/15
 */
public interface RedisLock {

    /**
     * 获取redis客户端
     *
     * @return redis客户端
     */
    RedissonClient redisClient();

    /**
     * 尝试获取锁 并执行逻辑
     *
     * @param key      锁的key
     * @param runnable 获取到锁后的执行逻辑
     */
    default void tryLock(String key, Runnable runnable) {
        RLock lock = this.redisClient().getLock(key);
        lock.lock();
        try {
            runnable.run();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 尝试获取锁 并执行逻辑
     *
     * @param key      锁的key
     * @param supplier 执行的逻辑
     * @return 返回数据
     */
    default <T> T tryLock(String key, Supplier<T> supplier) {
        RLock lock = this.redisClient().getLock(key);
        lock.lock();
        try {
            return supplier.get();
        } finally {
            lock.unlock();
        }
    }
}
