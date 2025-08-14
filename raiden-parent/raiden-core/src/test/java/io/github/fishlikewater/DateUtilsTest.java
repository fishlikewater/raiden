/*
 * Copyright (c) 2023-2025 zhangxiang (fishlikewater@126.com)
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
package io.github.fishlikewater;

import io.github.fishlikewater.raiden.core.DateUtils;
import io.github.fishlikewater.raiden.core.constant.DatePatternConstants;
import org.junit.Test;

/**
 * {@code DateUtilsTest}
 * 时间工具类测试类
 *
 * @author zhangxiang
 * @version 1.1.3
 * @since 2025/01/22
 */
public class DateUtilsTest {

    @Test
    public void testLongConvert() {
        System.out.println(DateUtils.transfer(System.currentTimeMillis()));
    }

    @Test
    public void testFormat() {
        System.out.println(DateUtils.toLocalDateTime("2025-01-22 14:06:00", DatePatternConstants.NORM_DATETIME_FORMATTER));
    }
}
