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
package io.github.fishlikewater.raiden.http.core.degrade.sentinel;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * {@code SentinelDegradeRule}
 * sentinel规则定义
 *
 * @author zhangxiang
 * @version 1.1.0
 * @since 2024/12/12
 */
@Data
public class SentinelDegradeRule implements Serializable {

    @Serial
    private static final long serialVersionUID = 3459059170450357298L;

    private int grade = 0;

    private double count;

    private int timeWindow;

    private int minRequestAmount = 5;

    private double slowRatioThreshold = 1.0;

    private int statIntervalMs = 1000;
}
