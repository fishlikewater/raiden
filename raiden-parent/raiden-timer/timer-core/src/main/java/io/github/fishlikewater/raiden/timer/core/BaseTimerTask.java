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

import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.references.org.springframework.scheduling.support.CronExpression;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * {@code BaseTimerTask}
 * 执行任务
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/04/03
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
    public long delayMs() {
        return 0;
    }

    /**
     * 设置corn表达式
     */
    public String cornExpression() {
        return null;
    }

    /**
     * 设置描述
     */
    public String desc() {
        return null;
    }

    public BaseTimerTask() {
        this.setDesc(this.desc());
        this.setDelayMs(this.delayMs());
        final String expression = this.cornExpression();
        if (ObjectUtils.isNotNullOrEmpty(expression)) {
            final boolean validExpression = CronExpression.isValidExpression(expression);
            if (!validExpression) {
                throw new IllegalArgumentException("cornExpression is not valid");
            }
            this.setCornExpression(expression);
        }
    }
}