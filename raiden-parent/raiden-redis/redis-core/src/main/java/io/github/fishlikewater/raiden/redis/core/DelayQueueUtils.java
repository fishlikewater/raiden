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
package io.github.fishlikewater.raiden.redis.core;

import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.exception.RaidenExceptionCheck;
import io.github.fishlikewater.raiden.redis.core.delay.DelayQueue;
import io.github.fishlikewater.raiden.redis.core.delay.DelayTask;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@code DelayQueueUtils}
 * 延迟队列外部调用服务
 *
 * @author zhangxiang
 * @version 1.0.2
 * @since 2024/06/06
 */
public class DelayQueueUtils {

    private static final Map<String, DelayQueue> TOPIC_QUEUE = new ConcurrentHashMap<>(16);

    public static void publish(DelayTask<? extends Serializable> task) {
        String topic = task.getTopic();
        DelayQueue delayQueue = TOPIC_QUEUE.get(topic);
        if (ObjectUtils.isNotNullOrEmpty(delayQueue)) {
            delayQueue.add(task);
        } else {
            RaidenExceptionCheck.INSTANCE.throwUnchecked("topic: [{}].delay.queue.not.found", topic);
        }
    }

    public static Map<String, DelayQueue> getRegisters() {
        return TOPIC_QUEUE;
    }
}
