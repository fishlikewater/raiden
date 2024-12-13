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
package io.github.fishlikewater.raiden.core;

import io.github.fishlikewater.raiden.core.constant.CommonConstants;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * {@code DateUtils}
 * 时间工具类
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/07
 */
@SuppressWarnings("unused")
public class DateUtils {

    /**
     * 毫秒或秒转LocalDateTime
     *
     * @param ts 时间戳
     * @return LocalDateTime
     */
    public static LocalDateTime transfer(Long ts) {
        if (null == ts || 0L == ts) {
            return null;
        }
        if (String.valueOf(ts).length() < CommonConstants.TIME_STAMP_LENGTH) {
            return transferShort(ts);
        }

        return of(ts);
    }

    /**
     * LocalDateTime转毫秒
     *
     * @param localDateTime LocalDateTime
     * @return 时间戳
     */
    public static long transfer(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return instant.toEpochMilli();
    }

    /**
     * 毫秒或秒转LocalDateTime
     *
     * @param ts 时间戳
     * @return LocalDateTime
     */
    public static LocalDateTime transferShort(Long ts) {
        if (null == ts || 0L == ts) {
            return null;
        }
        if (String.valueOf(ts).length() >= CommonConstants.TIME_STAMP_LENGTH) {
            return transfer(ts);
        }

        return transfer(ts * CommonConstants.MILLIS_UNIT);
    }

    /**
     * epochMilli转LocalDateTime
     *
     * @param epochMilli 时间戳
     * @return LocalDateTime
     */
    public static LocalDateTime of(long epochMilli) {
        return of(Instant.ofEpochMilli(epochMilli));
    }

    /**
     * Instant转LocalDateTime
     *
     * @param instant Instant
     * @return LocalDateTime
     */
    public static LocalDateTime of(Instant instant) {
        return of(instant, ZoneId.systemDefault());
    }

    /**
     * Instant转LocalDateTime
     *
     * @param instant Instant
     * @param zoneId  时区
     * @return LocalDateTime
     */
    public static LocalDateTime of(Instant instant, ZoneId zoneId) {
        return null == instant ? null : LocalDateTime.ofInstant(instant, ObjectUtils.defaultIfNullOrEmpty(zoneId, ZoneId.systemDefault()));
    }

    public static ChronoUnit convertToChronoUnit(TimeUnit timeUnit) {
        return switch (timeUnit) {
            case DAYS -> ChronoUnit.DAYS;
            case HOURS -> ChronoUnit.HOURS;
            case MINUTES -> ChronoUnit.MINUTES;
            case SECONDS -> ChronoUnit.SECONDS;
            case MICROSECONDS -> ChronoUnit.MICROS;
            case MILLISECONDS -> ChronoUnit.MILLIS;
            case NANOSECONDS -> ChronoUnit.NANOS;
        };
    }
}
