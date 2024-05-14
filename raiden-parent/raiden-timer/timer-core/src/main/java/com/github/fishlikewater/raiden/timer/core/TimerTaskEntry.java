/*
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

import com.github.fishlikewater.raiden.core.references.org.springframework.scheduling.support.CronExpression;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * {@code TimerTaskEntry}
 * 定时任务
 *
 * @author zhangxiang
 * @since 2024/04/03
 * @version 1.0.0
 */
@Data
@Slf4j
@SuppressWarnings("unused")
public class TimerTaskEntry implements Comparable<TimerTaskEntry> {

    volatile Bucket bucket;

    TimerTaskEntry next;

    TimerTaskEntry prev;

    private BaseTimerTask baseTimerTask;

    private CronExpression cronExpression;

    private long expireMs;

    public TimerTaskEntry() {}

    public TimerTaskEntry(BaseTimerTask baseTimerTask, long expireMs) {
        this.baseTimerTask = baseTimerTask;
        this.expireMs = expireMs;
        this.next = null;
        this.prev = null;
    }

    void remove() {
        Bucket currentBucket = bucket;
        while (currentBucket != null) {
            currentBucket.remove(this);
            currentBucket = bucket;
        }
    }

    @Override
    public int compareTo(TimerTaskEntry o) {
        return ((int) (this.expireMs - o.expireMs));
    }
}