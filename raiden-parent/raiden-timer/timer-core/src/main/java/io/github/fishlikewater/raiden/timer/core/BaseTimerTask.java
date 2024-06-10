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
package io.github.fishlikewater.raiden.timer.core;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * {@code BaseTimerTask}
 * 执行任务
 *
 * @author zhangxiang
 * @since  2024/04/03
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class BaseTimerTask implements Runnable {

    /**
     * 延时时间
     */
    private long delayMs;

    /**
     * corn表达式
     */
    private String cornExpression;

    private String desc;

    /**
     * 任务所在的entry 用来获取一些执行分配参数
     */
    private TimerTaskEntry timerTaskEntry;

    /**
     * 设置延迟时间 ms
     */
    public abstract void delayMs();

    /**
     * 设置corn表达式
     */
    public abstract void cornExpression();

    /**
     * 设置描述
     */
    public void desc() {

    };
}
