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

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.DelayQueue;

/**
 * {@code TimeWheel}
 * 时间轮定义
 *
 * @author zhangxiang
 * @since 2024/04/03
 * @version 1.0.0
 */
@Data
@Builder
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TimeWheel {

    /**
     * 一个时间槽的时间
     */
    private long tickMs;

    /**
     * 时间轮大小
     */
    private int wheelSize;

    /**
     * 时间跨度
     */
    private long interval;

    /**
     * 槽
     */
    private Bucket[] buckets;

    /**
     * 时间轮指针
     */
    private long currentTime;

    /**
     * 上层时间轮
     */
    private volatile TimeWheel overflowWheel;

    /**
     * 延迟队列 协助推进时间轮
     */
    private DelayQueue<Bucket> delayQueue;

    public TimeWheel(long tickMs, int wheelSize, long currentTime, DelayQueue<Bucket> delayQueue) {
        this.tickMs = tickMs;
        this.wheelSize = wheelSize;
        this.interval = tickMs * wheelSize;
        this.buckets = new Bucket[wheelSize];
        this.currentTime = currentTime - (currentTime % tickMs);
        this.delayQueue = delayQueue;

        for (int i = 0; i < wheelSize; i++) {
            buckets[i] = new Bucket();
        }
    }

    /**
     * 推进指针
     *
     * @param timestamp 时间戳 {@link  Long}
     */
    public void advanceLock(long timestamp) {
        if (timestamp >= currentTime + tickMs) {
            currentTime = timestamp - (timestamp % tickMs);
            this.determineOverflowWheel(timestamp);
        }
    }

    /**
     * 添加任务
     *
     * @param entry 定时任务
     * @return 是否添加成功
     */
    public boolean addTask(TimerTaskEntry entry) {
        long expireMs = entry.getExpireMs();
        long delayMs = expireMs - currentTime;
        if (delayMs < tickMs) {
            return false;
        } else {
            // 扔进当前时间轮的某个槽中，只有时间【大于某个槽】，才会放进去
            this.addTaskToBucket(entry, expireMs, delayMs);
        }
        return true;
    }

    private TimeWheel getOverflowWheel() {
        if (Objects.nonNull(this.overflowWheel)) {
            return this.overflowWheel;
        }

        synchronized (this) {
            this.buildOverflowWheel();
        }

        return overflowWheel;
    }

    // ---------------------------------------------------------------- PRIVATE

    private void addTaskToBucket(TimerTaskEntry entry, long expireMs, long delayMs) {
        if (delayMs < interval) {
            long virtualId = (expireMs / tickMs);
            int index = (int) (virtualId % wheelSize);
            Bucket bucket = buckets[index];
            boolean b = bucket.addTask(entry);
            if (!b) {
                log.error("addTaskToBucket error");
                return;
            }
            if (bucket.setExpiration(virtualId * tickMs)) {
                delayQueue.offer(bucket);
            }
        } else {
            TimeWheel timeWheel = this.getOverflowWheel();
            timeWheel.addTask(entry);
        }
    }

    private void determineOverflowWheel(long timestamp) {
        if (this.getOverflowWheel() != null) {
            this.getOverflowWheel().advanceLock(timestamp);
        }
    }

    private void buildOverflowWheel() {
        if (overflowWheel == null) {
            overflowWheel = new TimeWheel(interval, wheelSize, currentTime, delayQueue);
        }
    }
}
