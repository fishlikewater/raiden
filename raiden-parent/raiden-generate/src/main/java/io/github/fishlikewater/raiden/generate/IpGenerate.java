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
package io.github.fishlikewater.raiden.generate;

import io.github.fishlikewater.raiden.core.RandomUtils;

/**
 * {@code IpGenerate}
 * IP生成器
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/07/18
 */
public class IpGenerate extends AbstractGenerate<String> {

    @Override
    public String generate() {
        return String.format(
                "%d.%d.%d.%d",
                RandomUtils.randomInt(100, 255),
                RandomUtils.randomInt(0, 255),
                RandomUtils.randomInt(0, 255),
                RandomUtils.randomInt(0, 255)
        );
    }
}
