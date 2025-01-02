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

import io.github.fishlikewater.raiden.core.ObjectUtils;
import io.github.fishlikewater.raiden.core.id.Snowflake;

/**
 * {@code IdGenerate}
 * ID生成器
 * <p>调用Snowflake 生成</p>
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/30
 */
public class IdGenerate extends AbstractGenerate<Long> {

    private Snowflake snowflake;

    public IdGenerate(Snowflake snowflake) {
        this.snowflake = snowflake;
    }

    public IdGenerate() {
        this.init();
    }

    @Override
    public Long generate() {
        return snowflake.nextId();
    }

    public String generateString() {
        return snowflake.nextId().toString();
    }

    private void init() {
        if (ObjectUtils.isNullOrEmpty(snowflake)) {
            snowflake = new Snowflake(0L, 0L);
        }
    }
}
