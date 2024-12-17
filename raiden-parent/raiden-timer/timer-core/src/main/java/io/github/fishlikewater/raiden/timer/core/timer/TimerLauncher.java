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
package io.github.fishlikewater.raiden.timer.core.timer;

import io.github.fishlikewater.raiden.core.DateUtils;
import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.references.org.springframework.scheduling.support.CronExpression;
import io.github.fishlikewater.raiden.core.thread.NamedThreadFactory;
import io.github.fishlikewater.raiden.timer.core.BaseTimerTask;
import io.github.fishlikewater.raiden.timer.core.Bucket;
import io.github.fishlikewater.raiden.timer.core.TimeWheel;
import io.github.fishlikewater.raiden.timer.core.TimerTaskEntry;
import io.github.fishlikewater.raiden.timer.core.config.TimerConfig;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * {@code TimerLauncher}
 * 定时器实现
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/04/03
 */
@Slf4j
public class TimerLauncher implements Timer {

    /**
     * 底层时间轮
     */
    private TimeWheel timeWheel;
    /**
     * 一个Timer只有一个延时队列
     */
    private final DelayQueue<Bucket> delayQueue = new DelayQueue<>();
    /**
     * 过期任务执行线程
     */
    private final ExecutorService workerThreadPool;
    /**
     * 轮询delayQueue获取过期任务线程
     */
    private final ExecutorService bossThreadPool;

    @Getter
    private final TimerConfig timerConfig;

    public TimerLauncher(TimerConfig timerConfig) {
        this.timerConfig = timerConfig;
        this.timeWheel = new TimeWheel(timerConfig.getTickMs().toSeconds(), timerConfig.getWheelSize(), System.currentTimeMillis(), delayQueue);
        this.workerThreadPool = Executors.newVirtualThreadPerTaskExecutor();
        this.bossThreadPool = new ThreadPoolExecutor(
                1,
                1,
                0,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(),
                new NamedThreadFactory("timer-boss"));

        // 推动一次时间轮运转
        this.bossThreadPool.submit(() -> {
            while (true) {
                this.advanceClock(timerConfig.getClock().toMillis());
            }
        });
    }

    @Override
    public void add(BaseTimerTask baseTimerTask) {
        log.info("add task:{}", baseTimerTask.getDesc());
        TimerTaskEntry entry;
        if (ObjectUtils.isNotNullOrEmpty(baseTimerTask.getCornExpression())) {
            CronExpression cronExpression = CronExpression.parse(baseTimerTask.getCornExpression());
            long expireMs = DateUtils.transfer(cronExpression.next(LocalDateTime.now()));
            entry = new TimerTaskEntry(baseTimerTask, expireMs);
            entry.setCronExpression(cronExpression);
        } else {
            entry = new TimerTaskEntry(baseTimerTask, baseTimerTask.getDelayMs() + System.currentTimeMillis());
        }
        baseTimerTask.setTimerTaskEntry(entry);
        addTimerTaskEntry(entry);
    }

    /**
     * 推动指针运转获取过期任务
     *
     * @param timeout 时间间隔
     */
    @Override
    public synchronized void advanceClock(long timeout) {
        try {
            Bucket bucket = delayQueue.poll(timeout, TimeUnit.MILLISECONDS);
            if (bucket != null) {
                // 推进时间
                timeWheel.advanceLock(bucket.getExpiration());
                // 执行过期任务(包含降级)
                bucket.clear(this::addTimerTaskEntry);
            }
        } catch (InterruptedException e) {
            log.error("advanceClock error");
        }
    }

    @Override
    public int size() {
        return delayQueue.size();
    }

    @Override
    public void shutdown() {
        this.bossThreadPool.shutdown();
        this.workerThreadPool.shutdown();
        this.timeWheel = null;
    }

    private void addTimerTaskEntry(TimerTaskEntry entry) {
        if (!timeWheel.addTask(entry)) {
            // 任务已到期
            BaseTimerTask baseTimerTask = entry.getBaseTimerTask();
            log.info("handle task: {}", baseTimerTask.getDesc());
            workerThreadPool.submit(baseTimerTask);
            // corn 表达式任务添加下次时间
            if (Objects.nonNull(entry.getCronExpression())) {
                LocalDateTime next = entry.getCronExpression().next(LocalDateTime.now());
                entry.setExpireMs(DateUtils.transfer(next));
                this.addTimerTaskEntry(entry);
            }
        }
    }
}
