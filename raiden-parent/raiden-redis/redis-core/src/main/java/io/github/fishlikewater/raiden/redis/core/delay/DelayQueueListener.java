/*
 * Copyright (c) 2025 zhangxiang (fishlikewater@126.com)
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

import java.io.Serializable;

/**
 * {@code DelayQueueListener}
 * 延迟队列监听器
 *
 * @author zhangxiang
 * @version 1.0.2
 * @since 2024/06/06
 */
public interface DelayQueueListener {

    /**
     * 是否支持该消息
     *
     * @param topic  主题
     * @param taskId 消息id
     * @return true 支持
     */
    default boolean support(String topic, String taskId) {
        return false;
    }


    /**
     * 消费消息
     *
     * @param delayTask 消息
     */
    void onMessage(DelayTask<? extends Serializable> delayTask);
}
