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
package io.github.fishlikewater.raiden.generate;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import io.github.fishlikewater.raiden.core.RandomUtils;
import io.github.fishlikewater.raiden.core.StringUtils;

import java.time.LocalDateTime;

/**
 * {@code DateTimeGenerate}
 * 日期生成器
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/07/17
 */
public class DateTimeGenerate {

    private static final Long MS = 70L * 365 * 24 * 60 * 60 * 1000;

    /**
     * 生成完整格式时间
     */
    public static class FullDateTimeGenerate extends AbstractGenerate<String> {

        @Override
        public String generate() {
            return DateUtil.format(DateTimeGenerate.tryAcquireDateTime(), "yyyy-MM-dd HH:mm:ss");
        }

        public String generate(String format) {
            return DateUtil.format(DateTimeGenerate.tryAcquireDateTime(), format);
        }
    }

    /**
     * 生成日期
     */
    public static class DateGenerate extends AbstractGenerate<String> {

        @Override
        public String generate() {
            return DateUtil.format(DateTimeGenerate.tryAcquireDateTime(), "yyyy-MM-dd");
        }

        public String generate(String format) {
            return DateUtil.format(DateTimeGenerate.tryAcquireDateTime(), format);
        }
    }

    /**
     * 生成时间
     */
    public static class TimeGenerate extends AbstractGenerate<String> {

        @Override
        public String generate() {
            String hour = RandomUtils.randomInt(0, 12, true);
            String minute = RandomUtils.randomInt(0, 59, true);
            String second = RandomUtils.randomInt(0, 59, true);

            return StringUtils.format("{}:{}:{}", hour, minute, second);
        }

        public String generate(String format) {
            int hour = RandomUtils.randomInt(0, 12);
            int minute = RandomUtils.randomInt(0, 59);
            int second = RandomUtils.randomInt(0, 59);
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime localDateTime = now.withHour(hour).withMinute(minute).withSecond(second);
            return DateUtil.format(localDateTime, format);
        }
    }

    /**
     * 生成时间戳
     */
    public static class TimeStampGenerate extends AbstractGenerate<Long> {

        @Override
        public Long generate() {
            return DateUtil.current() - DateTimeGenerate.tryAcquireRandomLong();
        }
    }

    public static LocalDateTime tryAcquireDateTime() {
        long current = DateUtil.current();
        long randomLong = RandomUtils.randomLong(0, MS);
        current -= randomLong;
        return LocalDateTimeUtil.of(current);
    }

    public static long tryAcquireRandomLong() {
        return RandomUtils.randomLong(0, MS);
    }
}
