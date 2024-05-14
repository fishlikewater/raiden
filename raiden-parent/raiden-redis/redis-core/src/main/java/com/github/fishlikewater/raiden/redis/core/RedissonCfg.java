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
package com.github.fishlikewater.raiden.redis.core;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * {@code RedissonCfg}
 * Redisson配置类
 * </p>
 *
 * @author fishlikewater@126.com
 * @version 1.0.0
 * @since 2024年05月13日 20:35
 **/
@Data
public class RedissonCfg implements Serializable {

    private String url;

    private String passWord;

    private int dataBase = 1;

    private int connectionMinimumIdleSize = 1;

    private int connectionPoolSize = 2;
}
