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
package io.github.fishlikewater.raiden.redis.core.enums;

import lombok.Getter;

/**
 * {@code ServerPattern}
 * 服务模式
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/05/15
 */
@Getter
public enum ServerPattern {

    // 单机模式  集群模式 哨兵模式 主从模式 云托管模式
    SINGLE("单机模式"),

    CLUSTER("集群模式"),

    SENTINEL("哨兵模式"),

    MASTER_SLAVE("主从模式"),

    REPLICATED("云托管模式");

    private final String desc;

    ServerPattern(String desc) {
        this.desc = desc;
    }
}
