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
package io.github.fishlikewater.raiden.redis.autoconfig.processor;

import io.github.fishlikewater.raiden.core.StringUtils;
import io.github.fishlikewater.raiden.redis.core.constant.RedisConstants;
import io.github.fishlikewater.raiden.redis.core.delay.DelayQueue;
import io.github.fishlikewater.raiden.redis.core.delay.DelayTask;
import io.github.fishlikewater.raiden.redis.core.enums.DataTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * {@code UpdateCacheProcessor}
 * 缓存更新处理器
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/06/26
 */
public class UpdateCacheProcessor {

    private final DelayQueue delayQueue;

    private final Map<String, UpdateCacheProcessorHolder> map = new ConcurrentHashMap<>();

    public UpdateCacheProcessor(DelayQueue delayQueue) {
        this.delayQueue = delayQueue;
    }

    public void add(UpdateCacheProcessorHolder value) {
        String key = value.getKey();
        if (StringUtils.isNotBlank(value.getHashKey())) {
            key = StringUtils.format("{}#{}", value.getKey(), value.getHashKey());
        }
        if (!this.map.containsKey(key)) {
            this.map.put(key, value);
        }
        this.toDelayTask(value, false);
    }

    public void toDelayTask(UpdateCacheProcessorHolder value, boolean async) {
        DelayTask<Serializable> task = DelayTask.builder()
                .taskId(value.getKey())
                .topic(getTopic(value.getPrefix()))
                .payload(value.getKey())
                .delayTime(value.getDelayTime())
                .timeUnit(value.getDelayTimeUnit())
                .build();
        if (async) {
            Future<Void> ignore = this.delayQueue.addAsync(task);
        } else {
            this.delayQueue.add(task);
        }
    }

    public void remove(String key) {
        this.map.remove(key);
    }

    public UpdateCacheProcessorHolder get(String key) {
        return this.map.get(key);
    }

    private String getTopic(String prefix) {
        if (StringUtils.isBlank(prefix)) {
            return RedisConstants.UPDATE_TASK_SUFFIX;
        }
        return StringUtils.format("{}:{}", prefix, RedisConstants.UPDATE_TASK_SUFFIX);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateCacheProcessorHolder implements Serializable {

        private String key;

        private String hashKey;

        private String prefix;

        private DataTypeEnum type;

        private Long expireTime;

        private TimeUnit expireTimeUnit;

        private Method method;

        private Object[] args;

        private Object target;

        private long delayTime;

        private TimeUnit delayTimeUnit;
    }
}