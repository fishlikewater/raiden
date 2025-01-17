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
package io.github.fishlikewater.raiden.timer.core;

import io.github.fishlikewater.raiden.core.references.org.springframework.scheduling.support.CronExpression;
import io.github.fishlikewater.raiden.timer.core.config.TimerConfig;
import io.github.fishlikewater.raiden.timer.core.timer.TimerLauncher;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author fishlikewater@126.com
 * @since 2024年04月04日 23:57
 **/
public class ScheduleTaskTest {

    TimerLauncher timerLauncher;

    @Before
    public void init() {
        final TimerConfig timerConfig = new TimerConfig();
        timerConfig.setTickMs(Duration.ofSeconds(1));
        timerConfig.setWheelSize(60);
        timerConfig.setClock(Duration.ofMillis(20));

        timerLauncher = new TimerLauncher(timerConfig);
    }

    @Test
    public void testCornSchedule() throws InterruptedException {
        final BaseTimerTask task = new BaseTimerTask() {

            @Override
            public String cornExpression() {
                return "0/1 * * * * ?";
            }

            @Override
            public void run() {
                System.out.println("这是corn测试");
            }
        };
        timerLauncher.add(task);
        Thread.sleep(15_000);
    }

    @Test
    public void testAddTask() throws InterruptedException {

        final BaseTimerTask task = new BaseTimerTask() {
            @Override
            public long delayMs() {
                return 5_000;
            }

            @Override
            public void run() {
                System.out.println("这是具体执行逻辑");
            }
        };
        final BaseTimerTask task2 = new BaseTimerTask() {
            @Override
            public long delayMs() {
                return 5_000;
            }

            @Override
            public void run() {
                System.out.println("这是具体执行逻辑222");
            }
        };
        timerLauncher.add(task);
        timerLauncher.add(task2);
        Thread.sleep(15_000);
    }

    @Test
    public void testCorn() {
        final String corn = "30 0/5 * * * ?";
        LocalDateTime next = CronExpression.parse(corn).next(LocalDateTime.now());
        System.out.println(next);
    }

}
