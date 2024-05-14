/*
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
package com.github.fishlikewater.raiden.core;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.github.fishlikewater.raiden.core.constant.CommonConstants;

import java.time.LocalDateTime;

/**
 * {@code DateUtils}
 * 时间工具类
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/07
 */
@SuppressWarnings("unused")
public class DateUtils extends DateUtil {

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

        return LocalDateTimeUtil.of(ts);
    }

    /**
     * LocalDateTime转毫秒
     *
     * @param time LocalDateTime
     * @return 时间戳
     */
    public static long transfer(LocalDateTime time) {
        return LocalDateTimeUtil.toEpochMilli(time);
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
}
