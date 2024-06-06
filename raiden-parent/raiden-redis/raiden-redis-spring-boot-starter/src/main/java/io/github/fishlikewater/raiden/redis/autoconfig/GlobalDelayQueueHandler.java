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
package io.github.fishlikewater.raiden.redis.autoconfig;

import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.redis.core.delay.DelayQueueHandler;
import io.github.fishlikewater.raiden.redis.core.delay.DelayQueueListener;
import io.github.fishlikewater.raiden.redis.core.delay.DelayTask;
import io.github.fishlikewater.spring.boot.raiden.core.SpringUtil;

import java.io.Serializable;
import java.util.List;

/**
 * {@code GlobalDelayQueueHandler}
 * 默认全局处理器
 *
 * @author zhangxiang
 * @version 1.0.2
 * @since 2024/06/06
 */
public class GlobalDelayQueueHandler implements DelayQueueHandler {

    private List<DelayQueueListener> subscribers;

    @Override
    public void handle(DelayTask<? extends Serializable> delayTask) {
        this.init();
        for (DelayQueueListener subscriber : subscribers) {
            if (subscriber.support(delayTask.getTopic(), delayTask.getTaskId())) {
                subscriber.onMessage(delayTask);
            }
        }
    }

    private synchronized void init() {
        if (ObjectUtils.isNullOrEmpty(subscribers)) {
            subscribers = SpringUtil.getBeanListOfType(DelayQueueListener.class);
        }
    }
}
