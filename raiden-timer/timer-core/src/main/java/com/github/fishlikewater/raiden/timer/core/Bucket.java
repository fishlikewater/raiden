/*
 * Copyright © 2024 zhangxiang (fishlikewater@126.com)
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
package com.github.fishlikewater.raiden.timer.core;

import lombok.NonNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * {@code Bucket}
 * 任务桶
 *
 * @author zhangxiang
 * @since 2024/04/03
 * @version 1.0.0
 */
public class Bucket implements Serializable, Delayed {

    @Serial
    private static final long serialVersionUID = -54685076223584564L;
    /**
     * 虚拟根节点root
     */
    private final TimerTaskEntry root = new TimerTaskEntry(null, -1);
    /**
     * bucket的过期时间
     */
    private final AtomicLong expiration = new AtomicLong(-1L);

    ReentrantLock removeLock = new ReentrantLock();
    ReentrantLock addLock = new ReentrantLock();

    {
        root.next = root;
        root.prev = root;
    }

    public long getExpiration() {
        return expiration.get();
    }

    /**
     * 设置bucket的过期时间,设置成功返回true
     *
     * @param expirationMs 到期时间
     * @return boolean {@link Boolean}
     */
    boolean setExpiration(long expirationMs) {
        return expiration.getAndSet(expirationMs) != expirationMs;
    }

    /**
     * 添加任务
     *
     * @param entry 定时任务
     * @return boolean {@link Boolean}
     */
    public boolean addTask(TimerTaskEntry entry) {
        boolean done = false;
        while (!done) {
            entry.remove();
            addLock.lock();
            try {
                if (entry.bucket == null) {
                    entry.bucket = this;
                    entry.prev = root.prev;
                    entry.next = root;
                    root.prev.next = entry;
                    root.prev = entry;
                    done = true;
                }
            } finally {
                addLock.unlock();
            }
        }
        return true;
    }

    /**
     * 从 Bucket 移除指定的 timerTaskEntry
     *
     * @param entry 定时任务
     */
    public void remove(TimerTaskEntry entry) {
        removeLock.lock();
        try {
            if (entry.getBucket().equals(this)) {
                entry.next.prev = entry.prev;
                entry.prev.next = entry.next;
                entry.next = null;
                entry.prev = null;
                entry.bucket = null;
            }
        } finally {
            removeLock.unlock();
        }
    }

    /**
     * 移除所有
     */
    public synchronized void clear(Consumer<TimerTaskEntry> entry) {
        TimerTaskEntry head = root.next;
        while (!head.equals(root)) {
            remove(head);
            entry.accept(head);
            head = root.next;
        }
        expiration.set(-1L);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return Math.max(0, unit.convert(expiration.get() - System.currentTimeMillis(), TimeUnit.MILLISECONDS));
    }

    @Override
    public int compareTo(@NonNull Delayed o) {
        if (o instanceof Bucket bucket) {
            return Long.compare(expiration.get(), bucket.expiration.get());
        }
        return 0;
    }
}
