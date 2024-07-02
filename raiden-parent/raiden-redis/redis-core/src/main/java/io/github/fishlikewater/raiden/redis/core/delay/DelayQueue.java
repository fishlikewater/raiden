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
package io.github.fishlikewater.raiden.redis.core.delay;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.github.fishlikewater.raiden.core.DateUtils;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.exception.RaidenExceptionCheck;
import io.github.fishlikewater.raiden.json.core.JSONUtils;
import io.github.fishlikewater.raiden.redis.core.DelayQueueUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

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
@Slf4j
public class DelayQueue implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final RedissonClient redissonClient;

    @Getter
    private final String topic;

    private RDelayedQueue<String> delayedQueue;

    private final DelayQueueHandler handler;

    private int subscribeId;

    private RBlockingQueue<String> blockingQueue = null;

    public DelayQueue(String topic, RedissonClient redissonClient, DelayQueueHandler handler) {
        this.topic = topic;
        this.redissonClient = redissonClient;
        this.handler = handler;
        this.init();
    }

    private void init() {
        DelayQueueUtils.getRegisters().put(topic, this);
        this.blockingQueue = redissonClient.getBlockingQueue(topic, StringCodec.INSTANCE);
        delayedQueue = redissonClient.getDelayedQueue(this.blockingQueue);
        this.subscribeId = this.blockingQueue.subscribeOnElements(element -> {
            try {
                DelayTask<? extends Serializable> delayTask = JSONUtils.JACKSON.readValue(element, new TypeReference<>() {});
                if (ObjectUtils.isNotNullOrEmpty(handler)) {
                    handler.handle(delayTask);
                }
            } catch (JsonProcessingException e) {
                log.error("delay: add.delay.task.failed, error.msg: ", e);
            }
            return CompletableFuture.completedFuture(null);
        });
    }

    public <R extends Serializable> void add(DelayTask<R> delay) {
        try {
            delay.setPublishTime(DateUtils.current());
            // 避免序列化方式的差异 统一JSON 序列化
            String dequePayload = JSONUtils.JACKSON.writeValueAsString(delay);
            this.delayedQueue.offer(dequePayload, delay.getDelayTime(), delay.getTimeUnit());
        } catch (JsonProcessingException e) {
            log.error("delay: add.delay.task.failed, error.msg: ", e);
        }
    }

    public <R extends Serializable> Future<Void> addAsync(DelayTask<R> delay) {
        try {
            delay.setPublishTime(DateUtils.current());
            // 避免序列化方式的差异 统一JSON 序列化
            String dequePayload = JSONUtils.JACKSON.writeValueAsString(delay);
            return this.delayedQueue.offerAsync(dequePayload, delay.getDelayTime(), delay.getTimeUnit());
        } catch (JsonProcessingException e) {
            return RaidenExceptionCheck.INSTANCE.throwUnchecked("delay: add.delay.task.failed, error.msg: ", e);
        }
    }

    public void destroy() {
        if (this.delayedQueue != null) {
            this.delayedQueue.destroy();
        }
        if (this.subscribeId != 0) {
            this.blockingQueue.unsubscribe(this.subscribeId);
        }
    }
}
