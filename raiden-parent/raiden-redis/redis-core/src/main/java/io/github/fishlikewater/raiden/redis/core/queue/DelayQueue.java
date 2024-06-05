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
package io.github.fishlikewater.raiden.redis.core.queue;

import org.redisson.api.RMapCache;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.map.event.EntryExpiredListener;
import org.redisson.client.codec.Codec;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * {@code DelayQueue}
 * 延迟队列
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.2
 * @since 2024年06月05日 21:09
 **/
public class DelayQueue<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final RedissonClient redissonClient;

    private final String topic;

    private final String taskKey;

    private RMapCache<String, T> mapCache;

    private RTopic rTopic;

    private final Codec codec;

    private Class<T> clazz;

    public DelayQueue(String topic, String taskKey, RedissonClient redissonClient, Codec codec) {
        this.topic = topic;
        this.taskKey = taskKey;
        this.redissonClient = redissonClient;
        this.codec = codec;
        this.init();
    }

    public void registerListener() {
        rTopic.addListenerAsync(clazz, (channel, msg) -> {
            System.out.println("Received message: " + msg);
            // 在这里处理你的业务逻辑
        });
    }

    private void init() {
        rTopic = redissonClient.getTopic(topic, codec);
        mapCache = redissonClient.getMapCache(taskKey, codec);
        mapCache.addListenerAsync((EntryExpiredListener<String, T>) event -> {
            final T value = event.getValue();
            rTopic.publish(value);
        });
    }

    public void add(String taskId, T t, long delay, TimeUnit timeUnit) {
        mapCache.put(taskId, t, delay, timeUnit);
    }
}
