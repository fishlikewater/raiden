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
package com.github.fishlikewater;

import com.github.fishlikewater.raiden.core.references.org.springframework.scheduling.support.CronExpression;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * {@code CornTest}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/07
 */
public class CornTest {

    @Test
    public void testCorn() {
        LocalDateTime now = LocalDateTime.now();
        CronExpression expression1 = CronExpression.parse("0 0 0/1 * * *");
        LocalDateTime nextTime = expression1.next(now);
        System.out.println(STR."每小时执行一次 -> 下次执行时间: \{nextTime}");
    }
}
