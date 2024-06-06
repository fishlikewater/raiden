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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * {@code DelayTask}
 * 延时任务
 *
 * @author zhangxiang
 * @version 1.0.2
 * @since 2024/06/06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DelayTask<T extends Serializable> implements Serializable {
    @Serial
    private static final long serialVersionUID = -8605715954978186985L;

    private String topic;

    private String taskId;

    private Long delayTime;

    private TimeUnit timeUnit;

    private T payload;

    private Long publishTime;
}
