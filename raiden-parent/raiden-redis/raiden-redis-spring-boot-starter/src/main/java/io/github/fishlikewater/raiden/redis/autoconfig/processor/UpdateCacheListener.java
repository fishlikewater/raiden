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

import io.github.fishlikewater.raiden.core.DateUtils;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.redis.core.constant.RedisConstants;
import io.github.fishlikewater.raiden.redis.core.delay.DelayQueueListener;
import io.github.fishlikewater.raiden.redis.core.delay.DelayTask;
import io.github.fishlikewater.raiden.redis.core.enums.DataTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * {@code UpdateCacheListener}
 * 缓存自动更新监听器
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/06/26
 */
@Slf4j
public class UpdateCacheListener implements DelayQueueListener {

    private final UpdateCacheProcessor updateCacheProcessor;

    private final RedissonClient redissonClient;

    public UpdateCacheListener(UpdateCacheProcessor updateCacheProcessor, RedissonClient redissonClient) {
        this.updateCacheProcessor = updateCacheProcessor;
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean support(String topic, String taskId) {
        return topic.contains(RedisConstants.UPDATE_TASK_SUFFIX);
    }

    @Override
    public void onMessage(DelayTask<? extends Serializable> delayTask) {
        String taskId = delayTask.getTaskId();
        UpdateCacheProcessor.UpdateCacheProcessorHolder holder = updateCacheProcessor.get(taskId);
        if (ObjectUtils.isNotNullOrEmpty(holder)) {
            Method method = holder.getMethod();
            Object target = holder.getTarget();
            Object[] args = holder.getArgs();
            Object obj;
            try {
                obj = method.invoke(target, args);
            } catch (Exception e) {
                log.error("Failed.to.perform.automatic.update", e);
                // 继续添加延迟任务
                updateCacheProcessor.toDelayTask(holder, true);
                return;
            }

            ChronoUnit chronoUnit = DateUtils.convertToChronoUnit(holder.getExpireTimeUnit());
            if (holder.getType() == DataTypeEnum.GENERAL) {
                RBucket<Object> bucket = redissonClient.getBucket(holder.getKey());
                bucket.setAsync(obj, Duration.of(holder.getExpireTime(), chronoUnit));
            }
            if (holder.getType() == DataTypeEnum.HASH) {
                RMapCache<String, Object> map = redissonClient.getMapCache(holder.getKey());
                map.putAsync(holder.getHashKey(), obj, holder.getExpireTime(), holder.getExpireTimeUnit());
            }
            updateCacheProcessor.toDelayTask(holder, true);
        }
    }
}
