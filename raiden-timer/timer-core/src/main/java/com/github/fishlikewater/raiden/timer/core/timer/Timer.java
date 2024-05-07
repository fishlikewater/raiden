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
package com.github.fishlikewater.raiden.timer.core.timer;

import com.github.fishlikewater.raiden.timer.core.BaseTimerTask;

/**
 * {@code Timer}
 * 定时器接口
 *
 * @author zhangxiang
 * @since 2024/04/03
 * @version  1.0.0
 */
@SuppressWarnings("unused")
public interface Timer {
    /**
     * 添加一个新任务
     *
     * @param baseTimerTask {@link BaseTimerTask}
     */
    void add(BaseTimerTask baseTimerTask);

    /**
     * 推动指针
     *
     * @param timeout 超时时间
     */
    void advanceClock(long timeout);

    /**
     * 等待执行的任务
     *
     * @return {@link Number}
     */
    int size();

    /**
     * 关闭服务,剩下的无法被执行
     */
    void shutdown();
}
